package com.lzc.overlay;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.lzc.gridviewoverlay.R;

public class MainActivity extends Activity {

	GridView grid;
	LayoutInflater inflater;
	BaseAdapter baseAdapter;
	boolean isDraw;
	int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		grid = (GridView) findViewById(R.id.grid);
		inflater = getLayoutInflater();
		grid.setAdapter(baseAdapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				if (convertView == null)
					convertView = inflater.inflate(R.layout.item, parent, false);
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 20;
			}
		});
		grid.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {

			@Override
			public void onGlobalFocusChanged(View oldFocus, View newFocus) {
				// TODO Auto-generated method stub
				Log.d("Global", "onGlobalFocusChanged");
			}
		});
		grid.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				Log.d("Global", "onGlobalLayout");
				if (!isDraw) {
					View mItem = grid.getChildAt(5);
					int[] locations = new int[2];
					mItem.getLocationOnScreen(locations);
					drag(locations[0] + mItem.getWidth() / 2, locations[1] - mItem.getHeight() / 4);

					mItem = grid.getChildAt(10);
					locations = new int[2];
					mItem.getLocationOnScreen(locations);
					drag(locations[0] + mItem.getWidth() / 2, locations[1] - mItem.getHeight() / 4);
					if (index > 0) {
						mItem = grid.getChildAt(index);
						locations = new int[2];
						mItem.getLocationOnScreen(locations);
						drag(locations[0] + mItem.getWidth() / 2, locations[1] - mItem.getHeight() / 4);
					}
					isDraw = !isDraw;
				}
			}
		});
		new CountDownTimer(6000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				isDraw = false;
				index = 9;
				baseAdapter.notifyDataSetChanged();
			}
		}.start();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
//		if (!isDraw) {
//			View mItem = grid.getChildAt(5);
//			int[] locations = new int[2];
//			mItem.getLocationOnScreen(locations);
//			drag(locations[0] + mItem.getWidth() / 2, locations[1] - mItem.getHeight() / 4);
//
//			mItem = grid.getChildAt(10);
//			locations = new int[2];
//			mItem.getLocationOnScreen(locations);
//			drag(locations[0] + mItem.getWidth() / 2, locations[1] - mItem.getHeight() / 4);
//			isDraw = !isDraw;
//		}
	}

	private void drag(int x, int y) {
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

		TextView tv = new TextView(this);
		tv.setBackgroundResource(R.drawable.rd_h03);
		tv.setText("开会");
		tv.setGravity(Gravity.CENTER);
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);// "window"
		windowManager.addView(tv, windowParams);
	}
}
