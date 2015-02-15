package com.coolweather.app.business;

import java.util.LinkedList;
import java.util.List;

import com.coolweather.app.bean.CityBean;
import com.coolweather.app.bean.CountyBean;
import com.coolweather.app.bean.ProvinceBean;
import com.coolweather.app.dao.WeatherDao;

public class WeatherBusiness {

	private WeatherDao weatherDao;

	public WeatherBusiness() {
		weatherDao = new WeatherDao();
	}

	public List<ProvinceBean> queryProvinceBeans(String response) {
		List<ProvinceBean> list = new LinkedList<ProvinceBean>();
		String[] rs = response.split(",");
		for (String s : rs) {
			String[] ss = s.split("\\|");
			ProvinceBean provinceBean = new ProvinceBean();
			provinceBean.setCode(ss[0]);
			provinceBean.setName(ss[1]);
			list.add(provinceBean);
		}
		return list;
	}

	public List<CityBean> queryCityBeans(String response) {
		List<CityBean> list = new LinkedList<CityBean>();
		String[] rs = response.split(",");
		for (String s : rs) {
			String[] ss = s.split("\\|");
			CityBean cityBean = new CityBean();
			cityBean.setCode(ss[0]);
			cityBean.setName(ss[1]);
			list.add(cityBean);
		}
		return list;
	}

	public List<CountyBean> queryCountyBeans(String response) {
		List<CountyBean> list = new LinkedList<CountyBean>();
		String[] rs = response.split(",");
		for (String s : rs) {
			String[] ss = s.split("\\|");
			CountyBean countyBean = new CountyBean();
			countyBean.setCode(ss[0]);
			countyBean.setName(ss[1]);
			list.add(countyBean);
		}
		return list;
	}

}
