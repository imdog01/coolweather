package com.coolweather.app.bean;

import java.io.Serializable;

public class CountyBean implements Serializable {

	private static final long serialVersionUID = -5811749657885630609L;

	private int id;
	private String code;
	private String name;
	private CityBean cityBean;

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

	public CityBean getCityBean() {
		return cityBean;
	}

	public void setCityBean(CityBean cityBean) {
		this.cityBean = cityBean;
	}

}
