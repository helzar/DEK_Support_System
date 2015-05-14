package com.nulp.dss.model.enums;

public enum QuarterEnum {
	summer("���"), winter("����");
	
	private String name;
	
	private QuarterEnum(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
