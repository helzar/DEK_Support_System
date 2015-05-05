package com.nulp.dss.docxmanaging.container.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class PaymentFormContainerGenerator {
	private static final Logger LOG = Logger.getLogger(PaymentFormContainerGenerator.class);

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
			LOG.warn("Graduation or reviewer do not exists! Reviewer id - " + reviewer + ", graduation id - " + graduation);
			return null;
		}
		Map<String, String> tagsMap = getTagsMap(reviewer);
		List<Diploma> diplomas = new DiplomaDao().getDiplomasByReviewerAndGraduation(reviewer, graduation);
		
		PaymentFormContainer dataContainer;
		for (int i = 0; i < diplomas.size(); i+=10){
			dataContainer = new PaymentFormContainer(tagsMap, getStudentsTableList(diplomas, i));
			containerList.add(dataContainer);
		}

		return containerList;
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
		table.add(new GroupDao().getByStudentId(student.getId()).getName());
		table.add(getReviewingHours(review));
		table.add(getReviewingTimeString(review));
	}
	
	private String getReviewingDate(Review review){
		if (review.getDate() != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(review.getDate());
			return getNormalizedTime(calendar.get(Calendar.DAY_OF_MONTH)) + "." + getNormalizedTime(calendar.get(Calendar.MONTH) + 1);
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
		

		tagsMap.put("from_date", "???");   // only for the curent doc, or for all???
		tagsMap.put("to_date", "???");
		tagsMap.put("discipline", "???");   // only for the curent doc, or for all???
		tagsMap.put("with students", "???");   // only for the curent doc, or for all???

		return tagsMap;
	}
	
}
