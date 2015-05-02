package com.nulp.dss.model.enums;

public enum AnswerEnum {
	full("повна"), notFull("не повна");
	
	private String name;
	
	private AnswerEnum(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
