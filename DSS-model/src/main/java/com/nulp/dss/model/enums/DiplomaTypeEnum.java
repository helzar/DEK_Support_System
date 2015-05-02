package com.nulp.dss.model.enums;

// magister, speshialist, bakalaver
public enum DiplomaTypeEnum {
	m("магістр"), 
	s("спеціаліст"),
	b("бакалавр");
	
	private String name;
	
	private DiplomaTypeEnum(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	
}
