package com.nulp.dss.docxmanaging.container.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.dao.GroupDao;
import com.nulp.dss.dao.ProtectionDayDao;
import com.nulp.dss.docxmanaging.container.DataContainer;
import com.nulp.dss.docxmanaging.container.ScheduleFormContainer;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.Group;
import com.nulp.dss.model.ProtectionDay;
import com.nulp.dss.model.enums.QuarterEnum;
import com.nulp.dss.util.GroupNameManager;
import com.nulp.dss.util.HibernateUtil;
import com.nulp.dss.util.enums.DiplomaTypeEnum;

public class ScheduleFormContainerGenerator {
	private static final Logger LOG = Logger.getLogger(PaymentFormContainerGenerator.class);
	private GroupDao groupDao = new GroupDao();

	/**
	 * 
	 * @param graduationId
	 * @return if null - than graduation do not exists
	 */
	public DataContainer getPaymentFormContainer(int graduationId){
		Graduation graduation = new GraduationDao().getById(graduationId);

		if (graduation == null){
			LOG.warn("Graduation do not exists! Graduation id - " + graduation);
			return null;
		}
		HibernateUtil.lazyInitialize(graduation, graduation.getGroups());
		
		Map<String, String> tagsMap = getTagsMap(graduation);
		List<ProtectionDay> days = new ProtectionDayDao().getOrderedGraduationDays(graduation);
		List<ArrayList<String>> table = getProtectionDayList(days);
		DataContainer container = new ScheduleFormContainer(tagsMap, table);

		return container;
	}
	
	private List<ArrayList<String>> getProtectionDayList(List<ProtectionDay> days){
		List<ArrayList<String>> table = new ArrayList<ArrayList<String>>(days.size());
		
		boolean isLastDay = false;
		for (int i = 0; i < days.size(); i++){
			if (i == (days.size() - 1)){
				isLastDay = true;
			}
			table.add(getRow(i, days.get(i), isLastDay));
		}
		
		return table;
	}
	
	private ArrayList<String> getRow(int index, ProtectionDay day, boolean isLastDay){
		ArrayList<String> row = new ArrayList<String>(5);
		
		row.add((index + 1) + "");
		row.add(getDateString(index, day));
		row.add(getNumberOfStudents(index, day, isLastDay));
		row.add(getGraduationTimeString(index, day, isLastDay));
		row.add(day.getAuditorium());
		
		return row;
	}
	
	private String getGraduationTimeString(int index, ProtectionDay day, boolean isLastDay){
		if (isLastDay && day.getEndTime() != null){
			Calendar calendar = Calendar.getInstance();
			StringBuilder builder = new StringBuilder();

			builder.append("з ");
			calendar.setTime(day.getEndTime());
			builder.append(getNormalizedTime(calendar.get(Calendar.HOUR_OF_DAY)));
			builder.append(":");
			builder.append(getNormalizedTime(calendar.get(Calendar.MINUTE)));

			return builder.toString();
		}
		else if (day.getStartTime() != null && day.getEndTime() != null){
			Calendar calendar = Calendar.getInstance();
			StringBuilder builder = new StringBuilder();

			calendar.setTime(day.getStartTime());
			builder.append(getNormalizedTime(calendar.get(Calendar.HOUR_OF_DAY)));
			builder.append(":");
			builder.append(getNormalizedTime(calendar.get(Calendar.MINUTE)));
				
			builder.append("-");

			calendar.setTime(day.getEndTime());
			builder.append(getNormalizedTime(calendar.get(Calendar.HOUR_OF_DAY)));
			builder.append(":");
			builder.append(getNormalizedTime(calendar.get(Calendar.MINUTE)));

			return builder.toString();
		}
		else{
			return null;
		}
	}
	
	private String getNumberOfStudents(int index, ProtectionDay day, boolean isLastDay){
		if (isLastDay){
			return "Резервний день";
		} 
		else if (day.getStartTime() != null && day.getEndTime() != null){
			Calendar calendar = Calendar.getInstance();

			calendar.setTime(day.getStartTime());
			int startHour = calendar.get(Calendar.HOUR_OF_DAY);
			int startMinute = calendar.get(Calendar.MINUTE);
				
			calendar.setTime(day.getEndTime());
			int endHour = calendar.get(Calendar.HOUR_OF_DAY);
			int endMinute = calendar.get(Calendar.MINUTE);

			int totalStudents = (endHour - startHour) * 2;
			if (startMinute != 0){
				totalStudents--;
			}
			if (endMinute != 0){
				totalStudents++;
			}
			
			return totalStudents + "";
		}
		else{
			return null;
		}
	}
	
	private String getDateString(int index, ProtectionDay day){
		if (day.getDay() != null){
			Calendar calendar = Calendar.getInstance();
			StringBuilder builder = new StringBuilder();
			
			calendar.setTime(day.getDay());
			builder.append(getNormalizedTime(calendar.get(Calendar.DAY_OF_MONTH)));
			builder.append(".");
			builder.append(getNormalizedTime(calendar.get(Calendar.MONTH) + 1));
			builder.append(".");
			builder.append(calendar.get(Calendar.YEAR));
			if (index == 0){
				builder.append("\n(достроковий захист)");
			}
			return builder.toString();
		}
		else{
			return null;
		}
	}
	
	private String getNormalizedTime(int time){
		String timeString = time + "";
		if (timeString.length() == 1){
			return "0" + timeString;
		} else{
			return timeString;
		}
	}
	
	private Map<String, String> getTagsMap(Graduation graduation){
		Map<String, String> tagsMap = new HashMap<String, String>();
		
		tagsMap.put("protection", getProtectionString(graduation));
		tagsMap.put("speciality", getSpecialityString(graduation));
		tagsMap.put("year", getYear(graduation));
		
		tagsMap.put("groups", getGroupsString(graduation));
		tagsMap.put("total_students", getTotalStudentsString(graduation));

		return tagsMap;
	}
	
	private String getTotalStudentsString(Graduation graduation){
		if (!graduation.getGroups().isEmpty()){
			if (graduation.getQuarter().equals(QuarterEnum.summer)){
				return getTotalStudentsStringForSummer(graduation);
			} else{
				return getTotalStudentsStringForWinter(graduation);
			}
		} else{
			return "-";
		}
	}
	
	private String getTotalStudentsStringForSummer(Graduation graduation){
		int total = 0;
		for (Group g: graduation.getGroups()){
			total += groupDao.getGroupCount(g.getId());
		}
		return total + " студенти";
	}
	
	private String getTotalStudentsStringForWinter(Graduation graduation){
		CountDiplomasTypesContainer container = new CountDiplomasTypesContainer();
		
		for (Group g: graduation.getGroups()){
			if (GroupNameManager.getGroupDiplomasType(g.getName()).equals(DiplomaTypeEnum.SPECIALIST)){
				container.specialist += groupDao.getGroupCount(g.getId());
			}
			else if (GroupNameManager.getGroupDiplomasType(g.getName()).equals(DiplomaTypeEnum.MASTER)){
				container.master += groupDao.getGroupCount(g.getId());
			}
			else{
				container.other += groupDao.getGroupCount(g.getId());
			}
		}
		
		List<String> textList = new ArrayList<String>();
		addToListIfNotZero(textList, container.specialist, "", " спеціалістів");
		addToListIfNotZero(textList, container.master, "", " магістрів");
		addToListIfNotZero(textList, container.other, "", " студентів з НКЦ");

		int total = container.specialist + container.master + container.other;
		return total + " студенти - " + formateSequenceString(textList);
	}
	
	private void addToListIfNotZero(List<String> textList, int value, String startText, String endText){
		if (value != 0){
			textList.add(startText + value + endText);
		}
	}
	
	private String getGroupsString(Graduation graduation){
		if (!graduation.getGroups().isEmpty()){
			if (graduation.getQuarter().equals(QuarterEnum.summer)){
				return getGroupsStringForSummer(graduation);
			} else{
				return getGroupsStringForWinter(graduation);
			}
		} else{
			return "-";
		}
	}
	
	private String getGroupsStringForWinter(Graduation graduation){
		List<Group> groups = new ArrayList<Group>(graduation.getGroups());
		List<String> textList = new ArrayList<String>();
		
		addToListIfNotEmpty(textList, getMastersString(groups));
		addToListIfNotEmpty(textList, getSpesialistsString(groups));
		addToListIfNotEmpty(textList, getAbsentiaString(groups));
		addToListIfNotEmpty(textList, getOthersString(groups));

		return formateSequenceString(textList);
	}
	
	private void addToListIfNotEmpty(List<String> textList, String text){
		if (!text.isEmpty()){
			textList.add(text);
		}
	}
	
	private String getMastersString(List<Group> groups){
		List<String> masters = new ArrayList<String>();
		for (Group g: groups){
			if (!GroupNameManager.isAbsentia(g.getName()) && GroupNameManager.getGroupDiplomasType(g.getName()).equals(DiplomaTypeEnum.MASTER)){
				masters.add(g.getName());
			}
		}

		return formateSequenceString(masters, "магістри ");
	}
	
	private String getSpesialistsString(List<Group> groups){
		List<String> spesialists = new ArrayList<String>();
		for (Group g: groups){
			if (!GroupNameManager.isAbsentia(g.getName()) && GroupNameManager.getGroupDiplomasType(g.getName()).equals(DiplomaTypeEnum.SPECIALIST)){
				spesialists.add(g.getName());
			}
		}

		return formateSequenceString(spesialists, "спеціалісти ");
	}
	
	private String getAbsentiaString(List<Group> groups){
		List<String> absentia = new ArrayList<String>();
		for (Group g: groups){
			if (GroupNameManager.isAbsentia(g.getName()) &&
					( GroupNameManager.getGroupDiplomasType(g.getName()).equals(DiplomaTypeEnum.SPECIALIST)
					|| GroupNameManager.getGroupDiplomasType(g.getName()).equals(DiplomaTypeEnum.MASTER)) ){
				absentia.add(g.getName());
			}
		}
		
		return formateSequenceString(absentia, "магістри та спеціалісти заочної форми (", ")");
	}
	
	private String formateSequenceString(List<String> textList, String startText, String endText){
		String text = formateSequenceString(textList);
		if (!text.isEmpty()){
			return startText + text + endText;
		} else{
			return "";
		}
	}
	
	private String formateSequenceString(List<String> textList, String startText){
		String text = formateSequenceString(textList);
		if (!text.isEmpty()){
			return startText + text;
		} else{
			return "";
		}
	}
	
	private String formateSequenceString(List<String> textList){
		StringBuilder builder = new StringBuilder();
		
		if (textList.isEmpty()){
			return "";
		}
		
		builder.append(textList.get(0));
		if (textList.size() >= 2){
			for (int i = 1; i < (textList.size() - 1); i++){
				builder.append(", ");
				builder.append(textList.get(i));
			}
			builder.append(" та ");
			builder.append(textList.get(textList.size() - 1));
		}
		
		return builder.toString();
	}
	
	private String getOthersString(List<Group> groups){
		for (Group g: groups){
			if (GroupNameManager.getGroupDiplomasType(g.getName()).equals(DiplomaTypeEnum.OTHER)){
				return "студенти з НКЦ";
			}
		}
		return "";
	}
	
	private String getGroupsStringForSummer(Graduation graduation){
		List<Group> groups = new ArrayList<Group>(graduation.getGroups());
		StringBuilder builder = new StringBuilder();
		
		builder.append("бакалаври - групи ");
		builder.append(groups.get(0).getName());
		if (groups.size() >= 2){
			for (int i = 1; i < (groups.size() - 1); i++){
				builder.append(", ");
				builder.append(groups.get(i).getName());
			}
			builder.append(" та ");
			builder.append(groups.get(groups.size() - 1).getName());
		}
		return builder.toString();
	}
	
	private String getYear(Graduation graduation){
		if (graduation.getYear() != null){
			Calendar calendar = Calendar.getInstance();
			
			calendar.setTime(graduation.getYear());
			return calendar.get(Calendar.YEAR) + "";
		}
		else{
			return "-";
		}
	}
	
	private String getSpecialityString(Graduation graduation){
		return "Інформаційні управляючі системи та технології";
	}
	
	private String getProtectionString(Graduation graduation){
		if (graduation.getQuarter().equals(QuarterEnum.summer)){
			return "бакалаврських кваліфікаційних робіт";
		} else{
			return "дипломних проектів та магістерських робіт";
		}
	}
	
	private class CountDiplomasTypesContainer{
		int specialist;
		int master;
		int other;
	}
	
}
