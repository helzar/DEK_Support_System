package com.nulp.dss.model.enums;

//denna, zaochna
public enum FormEnum {
	d("�����"), z("������");
	
	
	private String name;
	
	private FormEnum(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
