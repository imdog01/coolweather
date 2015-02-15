package com.coolweather.app.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ProvinceBean implements Serializable {

	private static final long serialVersionUID = 3683820186095255794L;

	private int id;
	private String code;
	private String name;
	private Set<CityBean> cityBeanSet = new HashSet<CityBean>();

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

	public Set<CityBean> getCityBeanSet() {
		return cityBeanSet;
	}

	public void setCityBeanSet(Set<CityBean> cityBeanSet) {
		this.cityBeanSet = cityBeanSet;
	}

}
