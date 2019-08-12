package com.zakj.formula.bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ProductBean {
	private Integer id;
	private String name;  //产品名称
	private String number;  //配方编号
	private String formula;  //配方名（过时）
	private String formula_desc; //产品描述 -> 配方描述
	private BigDecimal price;  //配方成本
	private Integer fid;
	private String major_composition; //主要成本
	private BigDecimal sample_quantity; //留样量（单位为千克）
	private List<PSeries> p_series;//产品系列
	
	private Integer sid;//接收系列id
	private Integer cid;//接收类别id
	private List<Map<String, Object>> ztree;
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getSample_quantity() {
		return sample_quantity;
	}
	public void setSample_quantity(BigDecimal sample_quantity) {
		this.sample_quantity = sample_quantity;
	}
	public String getFormula_desc() {
		return formula_desc;
	}
	public void setFormula_desc(String formula_desc) {
		this.formula_desc = formula_desc;
	}
	public String getMajor_composition() {
		return major_composition;
	}
	public void setMajor_composition(String major_composition) {
		this.major_composition = major_composition;
	}
	public List<Map<String, Object>> getZtree() {
		return ztree;
	}
	public void setZtree(List<Map<String, Object>> ztree) {
		this.ztree = ztree;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
//	public String getFormula() {
//		return formula;
//	}
//	public void setFormula(String formula) {
//		this.formula = formula;
//	}
	public List<PSeries> getP_series() {
		return p_series;
	}
	public void setP_series(List<PSeries> p_series) {
		this.p_series = p_series;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	@Override
	public String toString() {
		return "ProductBean [id=" + id + ", name=" + name + ", number="
				+ number + ", formula=" + formula + ", product_desc="
				+ formula_desc + ", p_series=" + p_series + "]";
	}
	
}
