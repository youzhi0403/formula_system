package com.zakj.formula.bean;

import java.util.List;

public class FormulaBean {
	private Integer id;
	private String number;
	private String name;
	private String technology;
	private Float temperature;
	private Integer formulaAmount;
	private Float price;
	private List<MaterialFormulaBean> fmaterial;

	public List<MaterialFormulaBean> getFmaterial() {
		return fmaterial;
	}

	public void setFmaterial(List<MaterialFormulaBean> fmaterial) {
		this.fmaterial = fmaterial;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}


	public Integer getFormulaAmount() {
		return formulaAmount;
	}

	public void setFormulaAmount(Integer formulaAmount) {
		this.formulaAmount = formulaAmount;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "FormulaBean [id=" + id + ", number=" + number + ", name="
				+ name + ", technology=" + technology + ", temperature="
				+ temperature + ", formulaAmount=" + formulaAmount + ", price="
				+ price + ", fmaterial=" + fmaterial + "]";
	}
	
}
