package com.zakj.formula.bean;

import java.math.BigDecimal;

public class FMaterial {
	
	private Integer fmId;//配方中的原料id
	private String group; //组别
	private String name; //商品名
	private String cn_name;//中文名
	private BigDecimal plan_amount; //计划量
	private BigDecimal actual_amount; //实称量
	private String m_batch_num;//料批号
	private BigDecimal checked_weight;//核称
	private Integer mid;//原料id
	private Integer fid;//该原料所属的配方id
	
	//以下是为数据存取方便，额外添加的字段
	private String unit;//原料的价格单位
	private BigDecimal price;//原料单价
	private BigDecimal cost;//原料成本
	private String inci_cn;//原料的中文inci
	private String inci_en;//原料的英文inci
	private String code; //原料代码
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInci_cn() {
		return inci_cn;
	}
	public void setInci_cn(String inci_cn) {
		this.inci_cn = inci_cn;
	}
	public String getInci_en() {
		return inci_en;
	}
	public void setInci_en(String inci_en) {
		this.inci_en = inci_en;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getFmId() {
		return fmId;
	}
	public void setFmId(Integer fmId) {
		this.fmId = fmId;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getCn_name() {
		return cn_name;
	}
	public void setCn_name(String cn_name) {
		this.cn_name = cn_name;
	}
	public String getM_batch_num() {
		return m_batch_num;
	}
	public void setM_batch_num(String m_batch_num) {
		this.m_batch_num = m_batch_num;
	}
	public BigDecimal getPlan_amount() {
		return plan_amount;
	}
	public void setPlan_amount(BigDecimal plan_amount) {
		this.plan_amount = plan_amount;
	}
	public BigDecimal getActual_amount() {
		return actual_amount;
	}
	public void setActual_amount(BigDecimal actual_amount) {
		this.actual_amount = actual_amount;
	}
	public BigDecimal getChecked_weight() {
		return checked_weight;
	}
	public void setChecked_weight(BigDecimal checked_weight) {
		this.checked_weight = checked_weight;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public Integer getMid() {
		return mid;
	}
	public void setMid(Integer mid) {
		this.mid = mid;
	}
	@Override
	public String toString() {
		return "FMaterial [fmId=" + fmId + ", group=" + group + ", name="
				+ name + ", plan_amount=" + plan_amount + ", price=" + price
				+ ", code=" + code + "]";
	}
	
}
