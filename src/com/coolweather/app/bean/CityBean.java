package com.coolweather.app.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CityBean implements Serializable {

	private static final long serialVersionUID = -8142074120731300779L;

	private int id;
	private String code;
	private String name;
	private ProvinceBean provinceBean;
	private Set<CountyBean> countyBeanSet = new HashSet<CountyBean>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public ProvinceBean getProvinceBean() {
		return provinceBean;
	}

	public void setProvinceBean(ProvinceBean provinceBean) {
		this.provinceBean = provinceBean;
	}

	public Set<CountyBean> getCountyBeanSet() {
		return countyBeanSet;
	}

	public void setCountyBeanSet(Set<CountyBean> countyBeanSet) {
		this.countyBeanSet = countyBeanSet;
	}

}
