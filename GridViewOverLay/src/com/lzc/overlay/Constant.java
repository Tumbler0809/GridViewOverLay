package com.lzc.overlay;

import android.os.Environment;

public class Constant {	
 
	public static String httpRequest="http://115.29.103.28:8080/E_Read/";
	
	private static String  sdpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
	
	
	public static String getMeetfile(String file){
		return sdpath +"/meeting/" + file ;
	}
	
	public static String getRemoteFileUrl(String file){
		return httpRequest + file ;
	}
	public static String  mSavePath = sdpath;
	
	
}
