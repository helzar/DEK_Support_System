package com.nulp.dss.model.enums;

public enum RatingEnum {
	two("2"), three("3"), four("4"), five("5");
	
	private String name;
	
	private RatingEnum(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
