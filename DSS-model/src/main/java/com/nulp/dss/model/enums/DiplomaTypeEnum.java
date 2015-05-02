package com.nulp.dss.model.enums;

// magister, speshialist, bakalaver
public enum DiplomaTypeEnum {
	m("������"), 
	s("���������"),
	b("��������");
	
	private String name;
	
	private DiplomaTypeEnum(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	
}
