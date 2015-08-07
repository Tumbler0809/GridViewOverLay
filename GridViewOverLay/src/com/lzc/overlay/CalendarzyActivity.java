package com.lzc.overlay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lzc.gridviewoverlay.R;

@SuppressLint("InflateParams")
public class CalendarzyActivity extends Activity implements OnClickListener {
	private GridView grid;
	private Calendaradapter adapter;
	private MonthDisplayHelper mHelper;
	public static final int PREVIOUS_MOUNT = -1;
	public static final int CURRENT_MOUNT = 0;
	public static final int NEXT_MOUNT = 1;
	public static int GridviewHeight = 0, GridViewWidth;
	private Context mContext;
	ImageView back;
	LinearLayout btn_back0, left_linearLayout, right_linearLayout;;
	TextView title_activity, currentyear, currentmouth, type1, type2, type3;
	RadioGroup rgp_month_nar;
	public static int GlobaLindex = 0;
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();// 装日历
	private List<Map<String, String>> mdata = new ArrayList<Map<String, String>>();// 装假期
	private Map<String, String> getjiaqiRequestParrms;
	private myHttpRequestVolley getjiaqi_task;
	private RequestQueue quest;
	boolean isDraw;
	int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perpetualcalendar);
		Init();
	}

	private void Init() {
		// TODO Auto-generated method stub
		mHelper = new MonthDisplayHelper(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().getFirstDayOfWeek());
		getjiaqiRequestParrms = new HashMap<String, String>();
		type1 = (TextView) findViewById(R.id.type1);
		type2 = (TextView) findViewById(R.id.type2);
		type3 = (TextView) findViewById(R.id.type3);
		grid = (GridView) findViewById(R.id.grid);
		btn_back0 = (LinearLayout) findViewById(R.id.btn_back0);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		btn_back0.setOnClickListener(this);
		mContext = this;
		quest = Volley.newRequestQueue(mContext);
		GlobaLindex = Calendar.getInstance().get(Calendar.MONTH);

		title_activity = (TextView) findViewById(R.id.tv_activityTitle);
		title_activity.setText("年历表");
		currentyear = (TextView) findViewById(R.id.currentyear);
		currentmouth = (TextView) findViewById(R.id.currentmouth);
		rgp_month_nar = (RadioGroup) findViewById(R.id.rgp_month_nar);
		rgp_month_nar.setOnCheckedChangeListener(onCheckedChangeListener);
		left_linearLayout = (LinearLayout) findViewById(R.id.left_linearLayout);
		right_linearLayout = (LinearLayout) findViewById(R.id.right_linearLayout);
		left_linearLayout.setOnClickListener(this);
		right_linearLayout.setOnClickListener(this);

		changeTextColor(GlobaLindex);
		if (GlobaLindex == 0) {
			left_linearLayout.setVisibility(View.INVISIBLE);
		} else if (GlobaLindex == 11) {
			right_linearLayout.setVisibility(View.INVISIBLE);
		} else {
			left_linearLayout.setVisibility(View.VISIBLE);
			right_linearLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (!isDraw) {
			GridviewHeight = grid.getHeight();
			getjiaqiRequestParrms.put("sdate", Calendar.getInstance().get(Calendar.YEAR) + "");
			getjiaqiRequestParrms.put("sdate1", GlobaLindex + "");// 上月
			getjiaqiRequestParrms.put("sdate2", GlobaLindex + 1 + "");// 当前月
			getjiaqiRequestParrms.put("sdate3", GlobaLindex + 2 + "");// 下月
			GetHttpJiaqi();
			isDraw = !isDraw;
		}
	}

	private void GetHttpJiaqi() {
		if (adapter != null)
			adapter.close();
		if (getjiaqi_task == null) {
			getjiaqi_task = new myHttpRequestVolley(Request.Method.POST, Constant.httpRequest + "service/interfaceService!getCalendarmonth.action", getjiaqiRequestParrms, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
				//	Log.e("web端", response.toString());
					try {
						if (response.optBoolean("state")) {
							if (mdata != null) {
								mdata.clear();
							}
							JSONArray array = response.optJSONArray("data");
							for (int i = 0; i < array.length(); i++) {
								JSONObject object = array.optJSONObject(i);
								Map<String, String> map = new HashMap<String, String>();
								map.put("type", object.optString("typeid"));
								map.put("content", object.optString("content"));
								map.put("sdate", object.optString("sdate"));
								if (object.optString("typeid").equals("1")) {
									type1.setVisibility(View.VISIBLE);
								}
								if (object.optString("typeid").equals("2")) {
									type2.setVisibility(View.VISIBLE);
								}
								if (object.optString("typeid").equals("6")) {
									type3.setVisibility(View.VISIBLE);
								}
								mdata.add(map);
							}
							initCells();
						} else {
							Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.printStackTrace();
					Toast.makeText(mContext, "未知错误，请检查网络", Toast.LENGTH_SHORT).show();

				}
			});
			quest.add(getjiaqi_task);// 发送登陆网络请求
		} else {
			getjiaqi_task.setParams(getjiaqiRequestParrms);// 如果不设置的话login_task初始化先于map初始化就会报空指针
			quest.add(getjiaqi_task);// 发送登陆网络请求
		}
	}

	public void changeTextColor(int index) {
		rgp_month_nar.check(rgp_month_nar.getChildAt(index).getId());
	}

	private void initCells() {

		class zyCalendar {
			public int day;
			public int mouth;
			public int year;
			public int isnow;// 1为当前月,0为上月或下月
			public int whichMonth; // -1 为上月 1为下月 0为此月

			public zyCalendar(int d, int b, int x, int mm, int yy) {
				day = d;
				whichMonth = b;
				isnow = x;
				mouth = mm;
				year = yy;
			}

			public zyCalendar(int d, int isnow, int mm, int yy) { // 上个月 默认为
				this(d, PREVIOUS_MOUNT, isnow, mm, yy);
			}
		}
		;
		zyCalendar tmp[][] = new zyCalendar[5][7];
		for (int i = 0; i < tmp.length; i++) {
			int n[] = mHelper.getDigitsForRow(i);
			for (int d = 0; d < n.length; d++) {
				if (mHelper.isWithinCurrentMonth(i, d)) {
					tmp[i][d] = new zyCalendar(n[d], CURRENT_MOUNT, 1, mHelper.getMonth() + 1, mHelper.getYear()); // 当前
				} else if (i == 0) {

					if (mHelper.getMonth() == 0) {
						tmp[i][d] = new zyCalendar(n[d], 0, 12, mHelper.getYear() - 1); // 前一个月
					} else {
						tmp[i][d] = new zyCalendar(n[d], 0, mHelper.getMonth(), mHelper.getYear());
					}

				} else {
					if (mHelper.getMonth() == 11) {
						tmp[i][d] = new zyCalendar(n[d], NEXT_MOUNT, 0, 1, mHelper.getYear() + 1); // 下月
					} else {
						tmp[i][d] = new zyCalendar(n[d], NEXT_MOUNT, 0, mHelper.getMonth() + 2, mHelper.getYear());
					}
				}

			}

		}
		if (data != null) {
			data.clear();
		}
		int m, n = 0;
		for (m = 0; m < 5; m++) {
			if (m != 0) {
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("nl", tmp[m][0].day + "");
				map1.put("yl", Lunar.getDayNongli(tmp[m][0].year, tmp[m][0].mouth, tmp[m][0].day));
				map1.put("isnow", tmp[m][0].isnow + "");
				map1.put("Holiday", new GregorianUtil().getGremessage(tmp[m][0].mouth, tmp[m][0].day));
				Calendar x = Calendar.getInstance();
				x.set(tmp[m][0].year, tmp[m][0].mouth - 1, tmp[m][0].day);
				SolarTermsUtil t = new SolarTermsUtil(x);
				map1.put("Solarterm", t.getSolartermsMsg());

				map1.put("data", DateUtils.dateToString(x.getTime(), DateUtils.formatter2));
				data.add(map1);
				// Log.e("中文年里", tmp[m][0].year+"年"+tmp[m][0].mouth+"月"+
				// tmp[m][0].day+"日");

			}
			for (n = 1; n < 7; n++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("nl", tmp[m][n].day + "");
				map.put("yl", Lunar.getDayNongli(tmp[m][n].year, tmp[m][n].mouth, tmp[m][n].day));
				map.put("isnow", tmp[m][n].isnow + "");// 是否是当前月
				map.put("Holiday", new GregorianUtil().getGremessage(tmp[m][n].mouth, tmp[m][n].day));
				Calendar x = Calendar.getInstance();
				x.set(tmp[m][n].year, tmp[m][n].mouth - 1, tmp[m][n].day);
				SolarTermsUtil t = new SolarTermsUtil(x);
				map.put("Solarterm", t.getSolartermsMsg());
				map.put("data", DateUtils.dateToString(x.getTime(), DateUtils.formatter2));

				data.add(map);
				// Log.e("中文年里", tmp[m][n].year+"年"+tmp[m][n].mouth+"月"+
				// tmp[m][n].day+"日");

			}
		}
		int day = Integer.parseInt(data.get(data.size() - 1).get("nl"));
		int lastday = day;
		int lastmouth = tmp[m - 1][n - 1].mouth;
		int lastyear = tmp[m - 1][n - 1].year;
		if (day == 31) {
			lastday = 1;
			if (lastmouth == 12) {
				lastmouth = 1;
				lastyear += 1;
			} else {
				lastmouth += 1;
			}

		} else if (day == 30) {
			if (tmp[m - 1][n - 1].mouth == 1 || tmp[m - 1][n - 1].mouth == 3 || tmp[m - 1][n - 1].mouth == 5 || tmp[m - 1][n - 1].mouth == 7 || tmp[m - 1][n - 1].mouth == 8 || tmp[m - 1][n - 1].mouth == 10 || tmp[m - 1][n - 1].mouth == 12) {
				lastday = 31;
			} else {
				lastday = 1;
				lastmouth += 1;
			}
		} else if (day == 28 && tmp[m - 1][n - 1].mouth == 2) {
			int year = mHelper.getYear();
			if ((year % 4 == 0 && year % 1 != 0) || (year % 400 == 0)) {
				lastday = 29;
			} else {
				lastday = 1;
				lastmouth = lastmouth + 1;
			}
		} else if (day == 29 && tmp[m - 1][n - 1].mouth == 2) {
			lastday = 1;
			lastmouth = lastmouth + 1;
		} else {
			lastday = 1 + lastday;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("nl", lastday + "");
		map.put("yl", Lunar.getDayNongli(lastyear, lastmouth, lastday));
		map.put("isnow", "0");// 是否是当前月
		map.put("Holiday", new GregorianUtil().getGremessage(lastmouth, lastday));
		Calendar x = Calendar.getInstance();
		x.set(lastyear, lastmouth, lastday);
		SolarTermsUtil t = new SolarTermsUtil(x);
		map.put("Solarterm", t.getSolartermsMsg());
		map.put("data", DateUtils.dateToString(x.getTime(), DateUtils.formatter2));

		data.add(map);
		adapter = new Calendaradapter(mContext, data, mdata, GridviewHeight / 5);
		grid.setAdapter(adapter);
	}

	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			left_linearLayout.setVisibility(View.VISIBLE);
			right_linearLayout.setVisibility(View.VISIBLE);
			switch (checkedId) {
			case R.id.rdb_one:
				left_linearLayout.setVisibility(View.GONE);
				break;
			case R.id.rdb_twelve:
				right_linearLayout.setVisibility(View.GONE);

				mHelper = new MonthDisplayHelper(Calendar.getInstance().get(Calendar.YEAR), 11, Calendar.getInstance().getFirstDayOfWeek());
				currentyear.setText(mHelper.getYear() + "年");
				currentmouth.setText(mHelper.getMonth() + 1 + "月");
				GlobaLindex = mHelper.getMonth();
				getjiaqiRequestParrms.clear();
				getjiaqiRequestParrms.put("sdate", Calendar.getInstance().get(Calendar.YEAR) + "");
				getjiaqiRequestParrms.put("sdate1", GlobaLindex + "");// 上月
				getjiaqiRequestParrms.put("sdate2", GlobaLindex + 1 + "");// 当前月
				if (GlobaLindex + 2 > 12) {
					getjiaqiRequestParrms.put("sdate3", 1 + "");// 下月
				} else {
					getjiaqiRequestParrms.put("sdate3", GlobaLindex + 2 + "");// 下月
				}
				GetHttpJiaqi();
				break;
			}
		}
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_back0:
			finish();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.right_linearLayout:
			mHelper.nextMonth();
			currentyear.setText(mHelper.getYear() + "年");
			currentmouth.setText(mHelper.getMonth() + 1 + "月");
			changeTextColor(mHelper.getMonth());
			GlobaLindex = mHelper.getMonth();
			getjiaqiRequestParrms.clear();
			getjiaqiRequestParrms.put("sdate", Calendar.getInstance().get(Calendar.YEAR) + "");
			getjiaqiRequestParrms.put("sdate1", GlobaLindex + "");// 上月
			getjiaqiRequestParrms.put("sdate2", GlobaLindex + 1 + "");// 当前月
			if (GlobaLindex + 2 > 12) {
				getjiaqiRequestParrms.put("sdate3", 1 + "");// 下月
			} else {
				getjiaqiRequestParrms.put("sdate3", GlobaLindex + 2 + "");// 下月
			}
			;// 下月
			GetHttpJiaqi();
			if (GlobaLindex == 11) {
				right_linearLayout.setVisibility(View.GONE);
			} else {
				left_linearLayout.setVisibility(View.VISIBLE);
				right_linearLayout.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.left_linearLayout:
			mHelper.previousMonth();
			currentyear.setText(mHelper.getYear() + "年");
			currentmouth.setText(mHelper.getMonth() + 1 + "月");
			GlobaLindex = mHelper.getMonth();
			getjiaqiRequestParrms.clear();
			getjiaqiRequestParrms.put("sdate", Calendar.getInstance().get(Calendar.YEAR) + "");
			getjiaqiRequestParrms.put("sdate1", GlobaLindex + "");// 上月
			getjiaqiRequestParrms.put("sdate2", GlobaLindex + 1 + "");// 当前月
			if (GlobaLindex + 2 > 12) {
				getjiaqiRequestParrms.put("sdate3", 1 + "");// 下月
			} else {
				getjiaqiRequestParrms.put("sdate3", GlobaLindex + 2 + "");// 下月
			}
			;// 下月
			GetHttpJiaqi();
			if (GlobaLindex == 0) {
				left_linearLayout.setVisibility(View.GONE);
			} else {
				left_linearLayout.setVisibility(View.VISIBLE);
				right_linearLayout.setVisibility(View.VISIBLE);
			}
			changeTextColor(mHelper.getMonth());
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (adapter != null)
			adapter.close();
		super.onPause();
	}
}
