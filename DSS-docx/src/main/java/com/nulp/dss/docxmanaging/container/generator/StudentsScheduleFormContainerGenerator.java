package com.nulp.dss.docxmanaging.container.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.dao.ProtectionDayDao;
import com.nulp.dss.docxmanaging.container.DataContainer;
import com.nulp.dss.docxmanaging.container.ScheduleFormContainer;
import com.nulp.dss.docxmanaging.container.StudentsScheduleFormContainer;
import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.ProtectionDay;
import com.nulp.dss.util.HibernateUtil;
import com.nulp.dss.util.StringManager;

public class StudentsScheduleFormContainerGenerator {
	private static final Logger LOG = Logger.getLogger(StudentsScheduleFormContainerGenerator.class);

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
		
		Map<String, String> tagsMap = new HashMap<String, String>();
		List<ProtectionDay> days = new ProtectionDayDao().getOrderedGraduationDays(graduation);
		List<ArrayList<String>> table = getProtectionDayList(days);
		DataContainer container = new StudentsScheduleFormContainer(tagsMap, table);

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
		row.add(getThisDayStudentsGraduated(day));
		
		return row;
	}
	
	private String getThisDayStudentsGraduated(ProtectionDay day) {
		List<String> list = new ArrayList<String>();
		HibernateUtil.lazyInitialize(day, day.getDiplomas());
		for (Diploma diploma: day.getDiplomas()){
			list.add(diploma.getStudent().getlName() + " " + diploma.getStudent().getfName());
		}
		return StringManager.formateSequenceString(list);
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
}
