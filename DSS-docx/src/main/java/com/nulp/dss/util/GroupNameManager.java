package com.nulp.dss.util;

import com.nulp.dss.util.enums.DiplomaTypeEnum;

public class GroupNameManager {

	/**
	 * Remove spaces (\t\n\x0B\f\r)
	 * @param name
	 * @return
	 */
	public static String cleanGroupName(String name){
		String newName;
		newName = name.replaceAll("\\s+","");
		return newName;
	}
	
	public static DiplomaTypeEnum getGroupDiplomasType(String groupName){
		if (groupName.startsWith("KN") || groupName.startsWith(" Õ")){
			return DiplomaTypeEnum.BACHELOR;
		}
		else if (groupName.startsWith("≤”—Ï") || groupName.startsWith("IYCm")){
			return DiplomaTypeEnum.MASTER;
		}
		else if (groupName.startsWith("≤”—Ò") || groupName.startsWith("IYCc")){
			return DiplomaTypeEnum.SPECIALIST;
		}
		else if (groupName.startsWith("≤”—") || groupName.startsWith("IYC")){
			return DiplomaTypeEnum.NCK;
		}
		return DiplomaTypeEnum.OTHER;
	}
	
	public static boolean isAbsentia(String groupName){
		if (groupName.matches("≤”—?-??Á") || groupName.matches("IYC?-??z")){
			return true;
		}
		return false;
	}
	
}
