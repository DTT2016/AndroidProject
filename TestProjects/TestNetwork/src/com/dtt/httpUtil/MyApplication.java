package com.dtt.httpUtil;

import android.app.Application;
import android.content.Context;


//ע�⣺Ҫ��AndroidManifest.xml��ע��
/*<application
    android:name="com.dtt.httpUtil.MyApplication"
    ......
    */
public class MyApplication extends Application {

	private static Context context;
	
	@Override
	public void onCreate() {
		context = getApplicationContext();
	}
	
	public static Context getContext() {
		return context;
	}
}
