package com.zakj.formula.bean;

import java.util.List;
import java.util.Set;

public class PSeries {
	
	private Integer id;
	private String s_name; //系列名
	private List<PCatogery> p_catogeris;
	
	//方便存放数据的字段
	private String c_name;//类别名
	
	public PSeries() {
		super();
	}
	public PSeries(Integer id, String name, List<PCatogery> p_catogeris) {
		super();
		this.id = id;
		this.s_name = name;
		this.p_catogeris = p_catogeris;
	}
	
	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<PCatogery> getP_catogeris() {
		return p_catogeris;
	}
	public void setP_catogeris(List<PCatogery> p_catogeris) {
		this.p_catogeris = p_catogeris;
	}
	@Override
	public String toString() {
		return "PSeries [id=" + id + ", name=" + s_name + ", p_catogeris="
				+ p_catogeris + "]";
	}
	
}
