package com.nulp.dss.util;

import java.util.List;

public class StringManager {
	
	public static String formateSequenceString(List<String> textList, String startText, String endText){
		String text = formateSequenceString(textList);
		if (!text.isEmpty()){
			return startText + text + endText;
		} else{
			return "";
		}
	}
	
	public static String formateSequenceString(List<String> textList, String startText){
		String text = formateSequenceString(textList);
		if (!text.isEmpty()){
			return startText + text;
		} else{
			return "";
		}
	}
	
	/**
	 * Marge string with separator ", " and last two elements separator " та "  
	 * @param textList
	 * @return
	 */
	public static String formateSequenceString(List<String> textList){
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

}
