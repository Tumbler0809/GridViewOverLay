package com.lzc.overlay;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzc.gridviewoverlay.R;

@SuppressLint("InflateParams")
public class Calendaradapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private List<Map<String, String>> mdata;
	private LayoutInflater inflate;
	WindowManager mwindowManager;
	Context context;
	Handler handler;
	private int height;
	private SparseArray<TextView> allWindow = new SparseArray<TextView>();
	int type;

	public Calendaradapter(Context context, List<Map<String, String>> data1, List<Map<String, String>> data2, int h) {
		this.data = data1;
		this.mdata = data2;
		inflate = LayoutInflater.from(context);
		this.context = context;
		this.height = h;
		if (allWindow.size() > 0) {
			close();
		}

	}

	// 在重新刷新页面之前要把上一次的窗口去掉
	public void close() {
		try {
			if (mwindowManager != null) {
				for (int i = 0; i < allWindow.size(); i++) {
					View view = allWindow.get(allWindow.keyAt(i));
					if (view != null)
						mwindowManager.removeView(view);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflate.inflate(R.layout.perpetualcalenderitem, null);
			holder.nl = (TextView) convertView.findViewById(R.id.nl);
			holder.yl = (TextView) convertView.findViewById(R.id.yl);
			holder.sx = (TextView) convertView.findViewById(R.id.sx);
			holder.h = (LinearLayout) convertView.findViewById(R.id.itemheight);
			holder.statetype = (TextView) convertView.findViewById(R.id.statetype);
			LayoutParams p = holder.h.getLayoutParams();
			p.height = height;
			holder.h.setLayoutParams(p);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nl.setText(data.get(position).get("nl").toString());
		holder.yl.setText(data.get(position).get("yl").toString());

		if (position == 5 || position == 6 || position == 12 || position == 13 || position == 19 || position == 20 || position == 26 || position == 27 || position == 33 || position == 34) {
			holder.nl.setTextColor(Color.parseColor("#ff0000"));
		}
		if ("0".equals(data.get(position).get("isnow"))) {
			holder.nl.setTextColor(Color.parseColor("#828282"));
		}
		if (position == 6 || position == 13 || position == 20 || position == 27 || position == 34) {
			holder.sx.setVisibility(View.INVISIBLE);
		}
		if (!"".equals(data.get(position).get("Holiday"))) {// 节假日
			holder.yl.setText(data.get(position).get("Holiday").toString());
			holder.yl.setTextColor(Color.parseColor("#ff0000"));
		}
		if (!"".equals(data.get(position).get("Solarterm"))) {// 节气
			holder.yl.setText(data.get(position).get("Solarterm").toString());
			holder.yl.setTextColor(Color.parseColor("#ff0000"));
		}
		final int index = position;
		String dataS = data.get(position).get("data");
		for (Map<String, String> items : mdata) {
			final String content = items.get("content");
			if (items.get("sdate").equals(dataS)) {// 有时间相等说明有状态
				if ("1".equals(items.get("type"))) {// 主任会议
					convertView.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener(convertView) {

						@Override
						public void onGlobalLayout() {
							// TODO Auto-generated method stub
							super.onGlobalLayout();
							int[] locations = new int[2];
							getView().getLocationOnScreen(locations);
							drag(index, locations[0] + getView().getWidth() / 2, locations[1] - getView().getHeight() / 3, content, R.drawable.rd_h01_1);
						}
					});
					holder.h.setBackgroundColor(Color.parseColor("#ff0000"));
					holder.nl.setTextColor(Color.parseColor("#ffffff"));
					holder.yl.setTextColor(Color.parseColor("#ffffff"));
				} else if ("2".equals(items.get("type"))) {// 常委会议
					convertView.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener(convertView) {

						@Override
						public void onGlobalLayout() {
							// TODO Auto-generated method stub
							super.onGlobalLayout();
							int[] locations = new int[2];
							getView().getLocationOnScreen(locations);
							drag(index, locations[0] + getView().getWidth() / 2, locations[1] - getView().getHeight() / 3, content, R.drawable.rd_h02_1);
						}
					});
					holder.h.setBackgroundColor(Color.parseColor("#ff0000"));
					holder.nl.setTextColor(Color.parseColor("#ffffff"));
					holder.yl.setTextColor(Color.parseColor("#ffffff"));
				} else if ("6".equals(items.get("type"))) {// 全国两会
					convertView.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener(convertView) {

						@Override
						public void onGlobalLayout() {
							// TODO Auto-generated method stub
							super.onGlobalLayout();
							int[] locations = new int[2];
							getView().getLocationOnScreen(locations);
							drag(index, locations[0] + getView().getWidth() / 4, locations[1] - getView().getHeight() / 2, content, R.drawable.rd_h03_1);
						}
					});
					holder.h.setBackgroundColor(Color.parseColor("#ff0000"));
					holder.nl.setTextColor(Color.parseColor("#ffffff"));
					holder.yl.setTextColor(Color.parseColor("#ffffff"));
				}
				if ("11".equals(items.get("type"))) {// 休假
					holder.h.setBackgroundColor(Color.parseColor("#eedada"));
					holder.statetype.setVisibility(View.VISIBLE);
					holder.nl.setTextColor(Color.parseColor("#ff0000"));
				}
				if ("12".equals(items.get("type"))) {// 班
					holder.h.setBackgroundColor(Color.parseColor("#eedada"));
					holder.statetype.setVisibility(View.VISIBLE);
					holder.statetype.setText("班");
					holder.statetype.setBackgroundColor(Color.parseColor("#979696"));
					holder.nl.setTextColor(Color.parseColor("#ff0000"));
					holder.yl.setTextColor(Color.parseColor("#ff0000"));
				}
			}
		}
		return convertView;
	}

	private void drag(int index, int x, int y, String content, int resuorce) {
		WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
		windowParams.gravity = Gravity.TOP | Gravity.LEFT;// 这个必须加
		// 得到preview左上角相对于屏幕的坐标
		windowParams.x = x;
		windowParams.y = y;
		// 设置宽和高
		windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

		windowParams.format = PixelFormat.TRANSLUCENT;
		windowParams.windowAnimations = 0;
		TextView tv = allWindow.get(index);
		if (tv == null)
			tv = (TextView) inflate.inflate(R.layout.popicon, null);
		else
			mwindowManager.removeView(tv);
		tv.setBackgroundResource(resuorce);
		tv.setText(content);
		if (mwindowManager == null)
			mwindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);// "window"
		Log.i("Drag", "index:" + index);
		mwindowManager.addView(tv, windowParams);
		allWindow.put(index, tv);
	}

}

class ViewHolder {

	public TextView nl, yl, sx, statetype;
	public LinearLayout h;
}

abstract class MyOnGlobalLayoutListener implements OnGlobalLayoutListener {

	private View view;

	public MyOnGlobalLayoutListener(View view) {
		this.view = view;
	}

	public View getView() {
		return view;
	}

	@Override
	public void onGlobalLayout() {
		if (view != null)
			view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

}
