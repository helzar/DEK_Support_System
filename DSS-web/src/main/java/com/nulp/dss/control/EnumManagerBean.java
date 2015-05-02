package com.nulp.dss.control;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nulp.dss.model.enums.AnswerEnum;
import com.nulp.dss.model.enums.DiplomaTypeEnum;
import com.nulp.dss.model.enums.FormEnum;
import com.nulp.dss.model.enums.QuarterEnum;
import com.nulp.dss.model.enums.RatingEnum;

@ManagedBean
@SessionScoped
public class EnumManagerBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, DiplomaTypeEnum> diplomaTypeEnumMap;
	private Map<String, AnswerEnum> answerEnumMap;
	private Map<String, FormEnum> formEnumMap;
	private Map<String, QuarterEnum> quarterEnumMap;
	private Map<String, RatingEnum> ratingEnumMap;
	
	@PostConstruct
	public void init() {
		String defaultValue = "не вказано";
		initDiplomaTypeEnumMap(defaultValue);
		initAnswerEnumMap(defaultValue);
		initFormEnumMap(defaultValue);
		initQuarterEnumMap(defaultValue);
		initRatingEnumMap(defaultValue);
	}
	
	private void initDiplomaTypeEnumMap(String nullObjectText){
		diplomaTypeEnumMap = new TreeMap<String, DiplomaTypeEnum>(new EnumStringComparator(nullObjectText, false));
		diplomaTypeEnumMap.put(nullObjectText, null);
		for (DiplomaTypeEnum element: DiplomaTypeEnum.values()){
			diplomaTypeEnumMap.put(element.toString(), element);
		}
	}
	
	private void initAnswerEnumMap(String nullObjectText){
		answerEnumMap = new TreeMap<String, AnswerEnum>(new EnumStringComparator(nullObjectText, false));
		answerEnumMap.put(nullObjectText, null);
		for (AnswerEnum element: AnswerEnum.values()){
			answerEnumMap.put(element.toString(), element);
		}
	}
	
	private void initFormEnumMap(String nullObjectText){
		formEnumMap = new TreeMap<String, FormEnum>(new EnumStringComparator(nullObjectText, false));
		formEnumMap.put(nullObjectText, null);
		for (FormEnum element: FormEnum.values()){
			formEnumMap.put(element.toString(), element);
		}
	}
	
	private void initQuarterEnumMap(String nullObjectText){
		quarterEnumMap = new TreeMap<String, QuarterEnum>(new EnumStringComparator(nullObjectText, false));
		quarterEnumMap.put(nullObjectText, null);
		for (QuarterEnum element: QuarterEnum.values()){
			quarterEnumMap.put(element.toString(), element);
		}
	}
	
	private void initRatingEnumMap(String nullObjectText){
		ratingEnumMap = new TreeMap<String, RatingEnum>(new EnumStringComparator(nullObjectText, false));
		ratingEnumMap.put(nullObjectText, null);
		for (RatingEnum element: RatingEnum.values()){
			ratingEnumMap.put(element.toString(), element);
		}
	}

	public Map<String, DiplomaTypeEnum> getDiplomaTypeEnumMap() {
		return diplomaTypeEnumMap;
	}

	public Map<String, AnswerEnum> getAnswerEnumMap() {
		return answerEnumMap;
	}

	public Map<String, FormEnum> getFormEnumMap() {
		return formEnumMap;
	}

	public Map<String, QuarterEnum> getQuarterEnumMap() {
		return quarterEnumMap;
	}

	public Map<String, RatingEnum> getRatingEnumMap() {
		return ratingEnumMap;
	}
	
	public static class EnumStringComparator implements Comparator<String>, Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String nullString;
		private boolean inEnd = false;
		
		public EnumStringComparator(String nullString, boolean inEnd){
			this.nullString = nullString;
			this.inEnd = inEnd;
		}
		
		@Override
		public int compare(String arg0, String arg1) {
			if (arg0.equals(nullString)) return inEnd?1:-1;
			if (arg1.equals(nullString)) return inEnd?-1:1;
			
			return arg0.compareTo(arg1);
		}
	}

}



