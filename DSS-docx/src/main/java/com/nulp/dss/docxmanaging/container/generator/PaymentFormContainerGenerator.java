package com.nulp.dss.docxmanaging.container.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.nulp.dss.dao.DiplomaDao;
import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.dao.GroupDao;
import com.nulp.dss.dao.ReviewerDao;
import com.nulp.dss.docxmanaging.container.DataContainer;
import com.nulp.dss.docxmanaging.container.PaymentFormContainer;
import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.Review;
import com.nulp.dss.model.Reviewer;
import com.nulp.dss.model.Student;
import com.nulp.dss.util.GroupNameManager;
import com.nulp.dss.util.enums.DiplomaTypeEnum;

public class PaymentFormContainerGenerator {
	private static final Logger LOG = Logger.getLogger(PaymentFormContainerGenerator.class);
	private static Map<DiplomaTypeEnum, String> disciplinesMap;
	private GroupDao groupDao = new GroupDao();

	static {
		disciplinesMap = new HashMap<DiplomaTypeEnum, String>();
		disciplinesMap.put(DiplomaTypeEnum.MASTER, "магістерських кваліфікаційних робіт");
		disciplinesMap.put(DiplomaTypeEnum.SPECIALIST, "дипломних проектів");
		disciplinesMap.put(DiplomaTypeEnum.BACHELOR, "бакалавратських кваліфікаційних робіт");
		disciplinesMap.put(DiplomaTypeEnum.OTHER, "-");
	}

	/**
	 * 
	 * @param graduationId
	 * @param reviewerId
	 * @return if null - than graduation or reviewer do not exists
	 */
	public List<DataContainer> getPaymentFormContainer(int graduationId, int reviewerId){
		List<DataContainer> containerList = new ArrayList<DataContainer>();
		
		Graduation graduation = new GraduationDao().getById(graduationId);
		Reviewer reviewer = new ReviewerDao().getById(reviewerId);
		
		if (graduation == null || reviewer == null){
			LOG.warn("Graduation or reviewer do not exists! Reviewer id - " + reviewerId + ", graduation id - " + graduationId);
			return null;
		}
		Map<String, String> tagsMap = getTagsMap(reviewer);
		List<Diploma> diplomas = new DiplomaDao().getDiplomasByReviewerAndGraduation(reviewer, graduation);
		
		PaymentFormContainer dataContainer;
		Map<String, String> margedTagsMap;
		for (int i = 0; i < diplomas.size(); i+=10){
			margedTagsMap = getMargedTagsMap(tagsMap, diplomas, i);
			dataContainer = new PaymentFormContainer(margedTagsMap, getStudentsTableList(diplomas, i));
			containerList.add(dataContainer);
		}

		return containerList;
	}

	private Map<String, String> getMargedTagsMap(Map<String, String> tagsMap, List<Diploma> diplomas, int startElement){
		Map<String, String> margedTagsMap = new HashMap<String, String>(tagsMap);
		Date startDate = diplomas.get(startElement).getReview().getDate();
		Date endDate = diplomas.get(startElement).getReview().getDate();
		Set<DiplomaTypeEnum> diplomaTypes = new HashSet<DiplomaTypeEnum>();
		Set<String> groups = new HashSet<String>();
		
		String groupName;
		for (int i = startElement; i < (startElement + 10); i++){
			if (i < diplomas.size()){
				startDate = minDate(startDate, diplomas.get(i).getReview().getDate());
				endDate = maxDate(endDate, diplomas.get(i).getReview().getDate());
				groupName = groupDao.getByStudentId(diplomas.get(i).getStudent().getId()).getName();
				diplomaTypes.add(GroupNameManager.getGroupDiplomasType(groupName));
				groups.add(groupName);
			} else{
				break;
			}
		}
		
		margedTagsMap.put("from_date", getFullDateString(startDate));
		margedTagsMap.put("to_date", getFullDateString(endDate));
		margedTagsMap.put("discipline", getDisciplineString(diplomaTypes));
		margedTagsMap.put("with students", getGroupsString(groups));
		
		return margedTagsMap;
	}
	
	private String getGroupsString(Set<String> groups){
		if (!groups.isEmpty()){
			List<String> groupsList = new ArrayList<String>(groups);
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < (groups.size() - 1); i++){
				builder.append(groupsList.get(i));
				builder.append(", ");
			}
			builder.append(groupsList.get(groupsList.size() - 1));
			return builder.toString();
		}
		else{
			return "-";
		}
	}
	
	private String getDisciplineString(Set<DiplomaTypeEnum> diplomaTypes){
		if (!diplomaTypes.isEmpty()){
			List<DiplomaTypeEnum> diplomaTypesList = new ArrayList<DiplomaTypeEnum>(diplomaTypes);
			StringBuilder builder = new StringBuilder("рецензування ");
			builder.append(disciplinesMap.get(diplomaTypesList.get(0)));
			for (int i = 1; i < diplomaTypesList.size(); i++){
				builder.append(" та ");
				builder.append(disciplinesMap.get(diplomaTypesList.get(i)));
			}
			return builder.toString();
		}
		else{
			return "-";
		}
	}
	
	private Date minDate(Date firstDate, Date secondDate){
		if (firstDate.before(secondDate)){
			return firstDate;
		} else{
			return secondDate;
		}
	}
	
	private Date maxDate(Date firstDate, Date secondDate){
		if (firstDate.after(secondDate)){
			return firstDate;
		} else{
			return secondDate;
		}
	}
	
	private String getFullDateString(Date date){
		if (date != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return getNormalizedTime(calendar.get(Calendar.DAY_OF_MONTH)) + "." + (getNormalizedTime(calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR) + " р.");
		} else{
			return "";
		}
	}
	
	private List<ArrayList<String>> getStudentsTableList(List<Diploma> diplomas, int startElement){
		List<ArrayList<String>> table = new ArrayList<ArrayList<String>>(5);
		
		int elementIndex = startElement;
		for (int i = 0; i < 5; i++, elementIndex++){
			table.add(new ArrayList<String>(10));
			if (elementIndex < diplomas.size()){
				addStudentsTableFragment(table.get(i), diplomas.get(elementIndex));
			} else{
				addEmptyStudentsTableFragment(table.get(i));
			}
		}
		
		for (int i = 0; i < 5; i++, elementIndex++){
			if (elementIndex < diplomas.size()){
				addStudentsTableFragment(table.get(i), diplomas.get(elementIndex));
			} else{
				addEmptyStudentsTableFragment(table.get(i));
			}
		}
		
		return table;
	}
	
	private void addStudentsTableFragment(List<String> table, Diploma diploma){
		Review review = diploma.getReview();
		Student student = diploma.getStudent();
		
		table.add(getReviewingDate(review));
		table.add(getStudentName(student));
		table.add(groupDao.getByStudentId(student.getId()).getName());
		table.add(getReviewingHours(review));
		table.add(getReviewingTimeString(review));
	}
	
	private String getReviewingDate(Review review){
		if (review.getDate() != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(review.getDate());
			return getNormalizedTime(calendar.get(Calendar.DAY_OF_MONTH)) + "." + (getNormalizedTime(calendar.get(Calendar.MONTH) + 1));
		} else{
			return "";
		}
	}
	
	private String getStudentName(Student student){
		if (student.getfName().length() >= 1 && student.getmName().length() >= 1){
			return student.getlName() + " " + student.getfName().substring(0, 1) + "." + student.getmName().substring(0, 1) + ".";
		} else{
			return student.getlName() + " " + student.getfName() + "." + student.getmName() + ".";
		}
	}
	
	private String getReviewingHours(Review review){
		Calendar calendar = Calendar.getInstance();
		
		int startHour = 0;
		if (review.getTimeStart() != null){
			calendar.setTime(review.getTimeStart());
			startHour = calendar.get(Calendar.HOUR_OF_DAY);
		}
		
		int endHour = 0;
		if (review.getTimeEnd() != null){
			calendar.setTime(review.getTimeEnd());
			endHour = calendar.get(Calendar.HOUR_OF_DAY);
		}

		return (endHour - startHour) + "";
	}
	
	private String getReviewingTimeString(Review review){
		Calendar calendar = Calendar.getInstance();
		StringBuilder builder = new StringBuilder();
		
		if (review.getTimeStart() != null){
			calendar.setTime(review.getTimeStart());
			builder.append(getNormalizedTime(calendar.get(Calendar.HOUR_OF_DAY)));
			builder.append(":");
			builder.append(getNormalizedTime(calendar.get(Calendar.MINUTE)));
		}
		builder.append("-");
		
		if (review.getTimeEnd() != null){
			calendar.setTime(review.getTimeEnd());
			builder.append(getNormalizedTime(calendar.get(Calendar.HOUR_OF_DAY)));
			builder.append(":");
			builder.append(getNormalizedTime(calendar.get(Calendar.MINUTE)));
		}
		
		return builder.toString();
	}
	
	private String getNormalizedTime(int time){
		String timeString = time + "";
		if (timeString.length() == 1){
			return "0" + timeString;
		} else{
			return timeString;
		}
	}
	
	private void addEmptyStudentsTableFragment(List<String> table){
		for (int i = 0; i < 5; i++){
			table.add("");
		}
	}
	
	private Map<String, String> getTagsMap(Reviewer reviewer){
		Map<String, String> tagsMap = new HashMap<String, String>();
		
		tagsMap.put("pip", reviewer.getlName() + " " + reviewer.getfName() + " " + reviewer.getmName());
		tagsMap.put("academic_title", reviewer.getAcademicTitle());
		tagsMap.put("degree", reviewer.getDegree());
		tagsMap.put("place_of_permanent_job", reviewer.getPlaceOfPermanentJob());
		tagsMap.put("home_address", reviewer.getHomeAddress());
		tagsMap.put("passport_series", reviewer.getPassportSeries());
		tagsMap.put("passport_number", reviewer.getPassportNumber());
		tagsMap.put("passport_issued", reviewer.getPassportIssued());
		
		if (reviewer.getBirthday() != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(reviewer.getBirthday());
			tagsMap.put("birthday", calendar.get(Calendar.YEAR) + "");
		} else{
			tagsMap.put("birthday", "-");
		}
		
		if (reviewer.getNumberOfChildrens() != null){
			tagsMap.put("number_of_childrens", reviewer.getNumberOfChildrens().toString());
		} else{
			tagsMap.put("number_of_childrens", " ");
		}

		return tagsMap;
	}
	
}
