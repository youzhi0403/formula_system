package com.zakj.formula.bean;

import java.math.BigDecimal;

public class MaterialBean {
	private Integer id;   //主键
	private String name;  //商品
	private BigDecimal price;  //单价
	private String unit; //单位
	private String packingWay;  //包装方式
	private String origin; //产地
	private String MApparentState; //原料外观状态
	private String supplier; //供应商
	private String code; //编码
	private String inci_cn;  //中文INCI
	private String inci_en;  //英文INCI
	private String application; //用途
	private String chineseName;//中文名
	private Integer sid; //对应的供应商id
	
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
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public MaterialBean() {
		super();
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getPackingWay() {
		return packingWay;
	}
	public void setPackingWay(String packingWay) {
		this.packingWay = packingWay;
	}
	public String getMApparentState() {
		return MApparentState;
	}
	public void setMApparentState(String mApparentState) {
		MApparentState = mApparentState;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	@Override
	public String toString() {
		return "MaterialBean [id=" + id + ", name=" + name + ", origin="
				+ origin + ", texture=" + MApparentState + ", supplier=" + supplier
				+ ", sid=" + sid + "]";
	}
	
	
}
