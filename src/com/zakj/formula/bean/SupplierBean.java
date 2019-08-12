package com.zakj.formula.bean;

public class SupplierBean {
	private Integer id;
	private String address;
	private String telephone;
	private String contact;
	private String companyName;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "SupplierBean [id=" + id + ", address=" + address
				+ ", telephone=" + telephone + ", contact=" + contact
				+ ", companyName=" + companyName + "]";
	}
	
}
