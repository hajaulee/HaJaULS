package com.hajau.lockscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main_Activity extends Activity {
	static boolean active = false;
	public static int wantToStopLock = 0;
	final WindowManager.LayoutParams param = new WindowManager.LayoutParams();
	private String hanKey;
	public Button[] ButtonIndex = new Button[10];
	TextView textview;
	EditText passwd;
	private int max_try = 5;
    public static Main_Activity instance;
	public static  int interval = 240000;
	private Handler handler = new Handler();
	public  Runnable runnable = new Runnable() {
		public void run() {
			Toast.makeText(Main_Activity.this, "Bàn phím đã được kích hoạt lại!", Toast.LENGTH_SHORT).show();
			max_try = 5;
			resetActivity();
			for (int i = 1; i < 10; i++) {
				ButtonIndex[i].setEnabled(true);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        instance = this;
        setContentView(R.layout.main);
		this.startService(new Intent(this, LockScreenService.class));
		this.startService(new Intent(this, DetectOtherApp.class));
		wantToStopLock = 0;
		//ButtonIndex = new Button[10];
		textview = (TextView) findViewById(R.id.textView1);
		passwd = (EditText) findViewById(R.id.editText1);
		passwd.setClickable(false);
		passwd.setCursorVisible(false);
		passwd.setGravity(Gravity.CENTER_HORIZONTAL);

		ButtonIndex[1] = (Button) findViewById(R.id.button1);
		ButtonIndex[2] = (Button) findViewById(R.id.button2);
		ButtonIndex[3] = (Button) findViewById(R.id.button3);
		ButtonIndex[4] = (Button) findViewById(R.id.button4);
		ButtonIndex[5] = (Button) findViewById(R.id.button5);
		ButtonIndex[6] = (Button) findViewById(R.id.button6);
		ButtonIndex[7] = (Button) findViewById(R.id.button7);
		ButtonIndex[8] = (Button) findViewById(R.id.button8);
		ButtonIndex[9] = (Button) findViewById(R.id.button9);
		final HomeKeyLocker HKL = new HomeKeyLocker();
		HKL.lock(this);

		final WindowManager.LayoutParams param = new WindowManager.LayoutParams();
		param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
		final View view = findViewById(R.id.main_layout);
		final ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null)
			parent.removeView(view);
		param.format = PixelFormat.RGBA_8888;
		param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        param.token = getWindow().getDecorView().getRootView().getWindowToken();
		param.gravity = Gravity.TOP | Gravity.START;
		param.width = view.getLayoutParams().width;
		param.height = view.getLayoutParams().height;
		final WindowManager wmgr = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		wmgr.addView(view, param);
        setStatusBarTranslucent(true);
		View clock = findViewById(R.id.digitalClock1);
		if(clock != null) {
			clock.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					wantToStopLock++;
					if (wantToStopLock < 4)
						Toast.makeText(Main_Activity.this, "Nhấn " + (4 - wantToStopLock) + " lần nữa để tắt khóa!",
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(Main_Activity.this, "Nhập đũng mã để tắt khóa", Toast.LENGTH_LONG).show();
				}
			});
		}
		for (int i = 0; i < 9; i++) {
			ButtonIndex[i + 1].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String text = passwd.getText().toString() + ((Button) v).getText();
					passwd.setText(text);
					if (text.length() == hanKey.length()) {
						if (text.equals(hanKey)) {
							Toast.makeText(Main_Activity.this, "Khớp mã, khóa đã mở", Toast.LENGTH_SHORT).show();
							try {
								wmgr.removeView(view);
							} catch (Exception e) {
								Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
							}
							HKL.unlock();
							Main_Activity.this.stopService(new Intent(Main_Activity.this, DetectOtherApp.class));
//							Intent i = new Intent(Main_Activity.this, Setting.class);
//							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							Main_Activity.this.startActivity(i);

							Main_Activity.this.finish();
						} else {
							Toast.makeText(Main_Activity.this, "Sai mã, còn " + (--max_try) + " lần thử.",
									Toast.LENGTH_SHORT).show();
							if (max_try == 0) {
								Toast.makeText(Main_Activity.this, "Vô hiệu hóa phím, thử lại sau 4 phút.",
										Toast.LENGTH_LONG).show();
								for (int j = 1; j <= 9; j++) {
									ButtonIndex[j].setEnabled(false);
								}
								handler.postDelayed(runnable, interval);
							}
						}
						resetActivity();
					}
				}
			});
		}
		resetActivity();
	}

	@Override
	protected void onResume() {
		super.onResume();
		resetActivity();
		Log.i("MainActivity", "onReSume");
	}
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
	@Override
	public void onStart() {
		super.onStart();
		active = true;
		Log.i("MainActivity", "onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		active = false;
		Log.i("MainActivity", "onStop");
	}

	@SuppressLint("SimpleDateFormat")
	private void resetActivity() {
		Random r = new Random();
		int randomIndex = r.nextInt(468);
		String RStr = getStringResourceByName("haudeptrai.0x" + String.valueOf(randomIndex));
		String[] Sarray = RStr.split(" ");
		DateFormat dateFormat = new SimpleDateFormat("mm");
		Date date = new Date();
		int minute = Integer.valueOf(dateFormat.format(date));
		LanguageAPI hangul = new LanguageAPI(Sarray[1 + (minute % 2)]);
		hanKey = hangul.toHangul();
		String keyOrdered = hanKey;
		String keyRandom = "";
		for (int i = hanKey.length(); i < 9; i++) {
			keyOrdered += getStringResourceByName("hangul.0x" + r.nextInt(38));
		}
		for (int i = 0; i < 9; i++) {
			int ran = r.nextInt(keyOrdered.length());
			keyRandom += keyOrdered.charAt(ran);
			keyOrdered = keyOrdered.replaceFirst(keyOrdered.charAt(ran) + "", "");
		}

		for (int i = 0; i < 9; i++) {
			ButtonIndex[i + 1].setText(String.valueOf(keyRandom.charAt(i)));
			// ButtonIndex[i + 1].setText("" + (i+1));
		}
		textview.setText(RStr);
		passwd.setText("");
	}

	private String getStringResourceByName(String aString) {
		String packageName = getPackageName();
		int resId = getResources().getIdentifier(aString, "string", packageName);
		return getString(resId);

	}

}
