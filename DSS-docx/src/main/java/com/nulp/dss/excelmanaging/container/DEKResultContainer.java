package com.nulp.dss.excelmanaging.container;

public class DEKResultContainer {
	private String position;
	private Double value;

	public DEKResultContainer(String position, Double value) {
		super();
		this.position = position;
		this.value = value;
	}

	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}

}
