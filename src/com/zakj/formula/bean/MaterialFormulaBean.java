package com.zakj.formula.bean;

public class MaterialFormulaBean {
	private Integer fid;	//配方id
	private Integer mid;	//原料id
	private String name;	//原料名称
	private Float weight;	//原料重量
	private Float unitPrice; //原料单价
	private String unit;	//原料单位
	
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public Integer getMid() {
		return mid;
	}
	public void setMid(Integer mid) {
		this.mid = mid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	public Float getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Float unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	@Override
	public String toString() {
		return "MaterialFormulaBean [mid=" + mid + ", weight=" + weight + "]";
	}
	
}
