package com.coolweather.app.activity;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.coolweather.app.R;
import com.coolweather.app.bean.CityBean;
import com.coolweather.app.bean.CountyBean;
import com.coolweather.app.bean.ProvinceBean;
import com.coolweather.app.business.WeatherBusiness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	public static final String REQUEST_COUNTRY = "http://www.weather.com.cn/data/list3/city.xml";
	public static final String REQUEST_LIST3_PREFIX = "http://www.weather.com.cn/data/list3/city";
	public static final String REQUEST_LIST3_SUFFIX = ".xml";
	public static final int SELECTION_COUNTRY = 11;
	public static final int SELECTION_PROVINCE = 12;
	public static final int SELECTION_CITY = 13;
	public static final int SELECTION_COUNTY = 14;
	public static final int WHAT_PROVINCES = 21;
	public static final int WHAT_CITYS = 22;
	public static final int WHAT_COUNTYS = 23;

	private int selection;

	private WeatherBusiness weatherBusiness = new WeatherBusiness();
	private TextView selectionTextView;
	private ListView selectionListView;
	private List<String> selectionList = new LinkedList<String>();
	private ArrayAdapter<String> arrayAdapter;
	private List<ProvinceBean> provinceBeanList;
	private List<CityBean> cityBeanList;
	private List<CountyBean> countyBeanList;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String response;
			switch (msg.what) {
			case WHAT_PROVINCES:
				response = (String) msg.obj;
				Log.e("MainActivity", "***************************" + response);
				provinceBeanList = weatherBusiness.queryProvinceBeans(response);
				selectionList.clear();
				for (ProvinceBean provinceBean : provinceBeanList) {
					selectionList.add(provinceBean.getName());
				}
				arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
						android.R.layout.simple_list_item_1, selectionList);
				selectionListView.setAdapter(arrayAdapter);
				selection = SELECTION_PROVINCE;
				break;
			case WHAT_CITYS:
				response = (String) msg.obj;
				cityBeanList = weatherBusiness.queryCityBeans(response);
				selectionList.clear();
				for (CityBean cityBean : cityBeanList) {
					selectionList.add(cityBean.getName());
				}
				arrayAdapter.notifyDataSetChanged();
				selectionListView.setSelection(0);
				// selectionListView.setSelection(selectionList.size());
				selection = SELECTION_CITY;
				break;
			case WHAT_COUNTYS:
				response = (String) msg.obj;
				countyBeanList = weatherBusiness.queryCountyBeans(response);
				selectionList.clear();
				for (CountyBean countyBean : countyBeanList) {
					selectionList.add(countyBean.getName());
				}
				arrayAdapter.notifyDataSetChanged();
				selectionListView.setSelection(selectionList.size());
				// selectionListView.setSelection(0);
				selection = SELECTION_COUNTY;
				break;
			default:
				break;
			}
		}
	};

	private void httpgetWithHttpclient(final String request) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpGet httpGet = new HttpGet(request);
					HttpClient httpClient = new DefaultHttpClient();
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity httpEntity = httpResponse.getEntity();
						String response = EntityUtils.toString(httpEntity,
								"utf-8");
						Message message = new Message();
						switch (selection) {
						case SELECTION_COUNTRY:
							message.what = WHAT_PROVINCES;
							break;
						case SELECTION_PROVINCE:
							message.what = WHAT_CITYS;
							break;
						case SELECTION_CITY:
							message.what = WHAT_COUNTYS;
							break;
						default:
							break;
						}
						message.obj = response.toString();
						handler.sendMessage(message);
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		selectionTextView = (TextView) findViewById(R.id.main_textview_selection);
		selectionListView = (ListView) findViewById(R.id.main_listview_selection);
		selection = SELECTION_COUNTRY;
		httpgetWithHttpclient(REQUEST_COUNTRY);

		selectionListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String request;
				switch (selection) {
				case SELECTION_COUNTRY:
					selectionTextView.setText(R.string.main_textview_selection);
					httpgetWithHttpclient(REQUEST_COUNTRY);
					break;
				case SELECTION_PROVINCE:
					ProvinceBean provinceBean = provinceBeanList.get(position);
					selectionTextView.setText(provinceBean.getName());
					request = REQUEST_LIST3_PREFIX + provinceBean.getCode()
							+ REQUEST_LIST3_SUFFIX;
					httpgetWithHttpclient(request);
					break;
				case SELECTION_CITY:
					CityBean cityBean = cityBeanList.get(position);
					selectionTextView.setText(cityBean.getName());
					request = REQUEST_LIST3_PREFIX + cityBean.getCode()
							+ REQUEST_LIST3_SUFFIX;
					httpgetWithHttpclient(request);
					break;
				case SELECTION_COUNTY:
					// to WeatherActivity
					CountyBean countyBean = countyBeanList.get(position);
					Intent intent = new Intent(MainActivity.this,
							WeatherActivity.class);
					intent.putExtra("code", countyBean.getCode());
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
