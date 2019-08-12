package com.zakj.formula.bean;

import java.math.BigDecimal;
import java.util.List;

public class FormulaDesc {

	private Integer id;//配方id
	private String f_name; //配方表名
	private String p_code;//产品代码
	private String f_number;//配方编码
	private String p_name; //产品名称
	private String batch_number; //批号
	private BigDecimal plain_amount; //计划量
	private String plain_unit; //计划量单位
	private BigDecimal actual_output;//实际产量
	private	BigDecimal water_ph; //水PH值
	private BigDecimal ele_conductivity; //导电性
	private	String equipment_state; //设备状态
	private	String product_date;//生产日期
	private	String technology_proc;//工艺记录
	private String exception_record;//异常记录
	private String engineer; //工程师
	private	String material_weigher;//称料员
	private	String material_checker; //核料员
	private	String material_distributor; //配料员
	private	String supervisor; //主管
	private String physicochemical_target;//理化指标
	private String attention_item;//注意事项
	private List<FMaterial> fmlist; //原料列表
	private String emulStartTime; //乳化开始时间
	private String emulEndTime; //乳化结束时间
	
	private BigDecimal price;//配方成本
	private List<String> procList;   //将工艺记录切割成列表
	private List<String> attentionList; //将注意事项切割成列表
	
	public String getEmulStartTime() {
		return emulStartTime;
	}
	public void setEmulStartTime(String emulStartTime) {
		this.emulStartTime = emulStartTime;
	}
	public String getEmulEndTime() {
		return emulEndTime;
	}
	public void setEmulEndTime(String emulEndTime) {
		this.emulEndTime = emulEndTime;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getF_name() {
		return f_name;
	}
	public void setF_name(String f_name) {
		this.f_name = f_name;
	}
	public String getP_code() {
		return p_code;
	}
	public void setP_code(String p_code) {
		this.p_code = p_code;
	}
	public String getF_number() {
		return f_number;
	}
	public void setF_number(String f_number) {
		this.f_number = f_number;
	}
	public String getP_name() {
		return p_name;
	}
	public void setP_name(String p_name) {
		this.p_name = p_name;
	}
	public String getBatch_number() {
		return batch_number;
	}
	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}
	public String getPlain_unit() {
		return plain_unit;
	}
	public void setPlain_unit(String plain_unit) {
		this.plain_unit = plain_unit;
	}
	public String getEquipment_state() {
		return equipment_state;
	}
	public void setEquipment_state(String equipment_state) {
		this.equipment_state = equipment_state;
	}
	public String getProduct_date() {
		return product_date;
	}
	public void setProduct_date(String product_date) {
		this.product_date = product_date;
	}
	public String getTechnology_proc() {
		return technology_proc;
	}
	public void setTechnology_proc(String technology_proc) {
		this.technology_proc = technology_proc;
	}
	public String getException_record() {
		return exception_record;
	}
	public void setException_record(String exception_record) {
		this.exception_record = exception_record;
	}
	public String getEngineer() {
		return engineer;
	}
	public void setEngineer(String engineer) {
		this.engineer = engineer;
	}
	public String getMaterial_weigher() {
		return material_weigher;
	}
	public void setMaterial_weigher(String material_weigher) {
		this.material_weigher = material_weigher;
	}
	public String getMaterial_checker() {
		return material_checker;
	}
	public void setMaterial_checker(String material_checker) {
		this.material_checker = material_checker;
	}
	public String getMaterial_distributor() {
		return material_distributor;
	}
	public void setMaterial_distributor(String material_distributor) {
		this.material_distributor = material_distributor;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public String getPhysicochemical_target() {
		return physicochemical_target;
	}
	public void setPhysicochemical_target(String physicochemical_target) {
		this.physicochemical_target = physicochemical_target;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAttention_item() {
		return attention_item;
	}
	public void setAttention_item(String attention_item) {
		this.attention_item = attention_item;
	}
	public List<FMaterial> getFmlist() {
		return fmlist;
	}
	public void setFmlist(List<FMaterial> fmlist) {
		this.fmlist = fmlist;
	}
	public List<String> getProcList() {
		return procList;
	}
	public void setProcList(List<String> procList) {
		this.procList = procList;
	}
	public List<String> getAttentionList() {
		return attentionList;
	}
	public void setAttentionList(List<String> attentionList) {
		this.attentionList = attentionList;
	}
	
	public BigDecimal getPlain_amount() {
		return plain_amount;
	}
	public void setPlain_amount(BigDecimal plain_amount) {
		this.plain_amount = plain_amount;
	}
	public BigDecimal getActual_output() {
		return actual_output;
	}
	public void setActual_output(BigDecimal actual_output) {
		this.actual_output = actual_output;
	}
	public BigDecimal getWater_ph() {
		return water_ph;
	}
	public void setWater_ph(BigDecimal water_ph) {
		this.water_ph = water_ph;
	}
	public BigDecimal getEle_conductivity() {
		return ele_conductivity;
	}
	public void setEle_conductivity(BigDecimal ele_conductivity) {
		this.ele_conductivity = ele_conductivity;
	}
	@Override
	public String toString() {
		return "FormulaDesc [f_name=" + f_name + ", p_code=" + p_code
				+ ", f_number=" + f_number + ", p_name=" + p_name
				+ ", batch_number=" + batch_number + ", plain_amount="
				+ plain_amount + ", plain_unit=" + plain_unit
				+ ", actual_output=" + actual_output + ", water_ph=" + water_ph
				+ ", ele_conductivity=" + ele_conductivity
				+ ", equipment_state=" + equipment_state + ", product_date="
				+ product_date + ", technology_proc=" + technology_proc
				+ ", exception_record=" + exception_record + ", engineer="
				+ engineer + ", material_weigher=" + material_weigher
				+ ", material_checker=" + material_checker
				+ ", material_distributor=" + material_distributor
				+ ", supervisor=" + supervisor + ", physicochemical_target="
				+ physicochemical_target + ", attention_item=" + attention_item
				+ ", fid=" + id + "]";
	}
	
}
