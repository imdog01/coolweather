package com.coolweather.app.activity;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.coolweather.app.R;
import com.coolweather.app.business.WeatherBusiness;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {

	public static final String REQUEST_CITYINFO_PREFIX = "http://www.weather.com.cn/data/cityinfo/";
	public static final String REQUEST_CITYINFO_SUFFIX = ".html";
	public static final int WHAT_INFO = 24;

	private WeatherBusiness weatherBusiness = new WeatherBusiness();
	private TextView cityTextView;
	private LinearLayout infoLinearLayout;
	private TextView ptimeTextView;
	private TextView weatherTextView;
	private TextView temp1TextView;
	private TextView temp2TextView;

	private Button swichButton;
	private Button refreshButton;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_INFO:
				String json = (String) msg.obj;
				Log.e("WeatherActivity", json);
				try {
					JSONObject jsonObject = new JSONObject(json);
					JSONObject weatherInfo = jsonObject
							.getJSONObject("weatherinfo");
					cityTextView.setText(weatherInfo.getString("city"));
					ptimeTextView.setText(weatherInfo.getString("ptime"));
					weatherTextView.setText(weatherInfo.getString("weather"));
					temp1TextView.setText(weatherInfo.getString("temp1"));
					temp2TextView.setText(weatherInfo.getString("temp2"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cityTextView.setVisibility(View.VISIBLE);
				infoLinearLayout.setVisibility(View.VISIBLE);
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
					String response;
					HttpGet httpGet = new HttpGet(request);
					HttpClient httpClient = new DefaultHttpClient();
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity httpEntity = httpResponse.getEntity();
						response = EntityUtils.toString(httpEntity, "utf-8");

//						String[] rs = response.split("\\|");
//						String infoRequest = REQUEST_CITYINFO_PREFIX + rs[1]
//								+ REQUEST_CITYINFO_SUFFIX;
//						httpGet = new HttpGet(infoRequest);
//						httpResponse = httpClient.execute(httpGet);
//						if (httpResponse.getStatusLine().getStatusCode() == 200) {
//							httpEntity = httpResponse.getEntity();
//							response = EntityUtils
//									.toString(httpEntity, "utf-8");
//						}

						Message message = new Message();
						message.what = WHAT_INFO;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);

		cityTextView = (TextView) findViewById(R.id.weather_textview_city);
		infoLinearLayout = (LinearLayout) findViewById(R.id.weather_linearlayout_info);
		ptimeTextView = (TextView) findViewById(R.id.weather_textview_ptime);
		weatherTextView = (TextView) findViewById(R.id.weather_textview_weather);
		temp1TextView = (TextView) findViewById(R.id.weather_textview_temp1);
		temp2TextView = (TextView) findViewById(R.id.weather_textview_temp2);

		cityTextView.setVisibility(View.INVISIBLE);
		infoLinearLayout.setVisibility(View.INVISIBLE);
		String code = getIntent().getStringExtra("code");
		String request = MainActivity.REQUEST_LIST3_PREFIX + code
				+ MainActivity.REQUEST_LIST3_SUFFIX;
		httpgetWithHttpclient(request);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather, menu);
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
