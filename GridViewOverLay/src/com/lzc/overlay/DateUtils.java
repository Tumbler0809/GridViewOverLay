package com.lzc.overlay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static String formatter2 = "yyyy-MM-dd";
	public static String formatter = "yyyy-MM-dd HH:mm:ss";
	public static String formatter3 = "HH:mm:ss";
	public static String formatter4 = "HHmmss";
	public static String formatter5 = "yyyyMMdd";
	public static String formatter6 = "yyyyMMddHHmmss";
	public static Date stringToDate(String date ,String formatter){
		SimpleDateFormat df = new SimpleDateFormat(formatter);//设置日期格式
		Date m = null; 
		try {
		   m=df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return m;
	}
	public static String dateToString(Date date ,String formatter){
		SimpleDateFormat df = new SimpleDateFormat(formatter);//设置日期格式
		try {
			return df.format(date);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
//	public  String getNowStringDate(){
//		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat(formatter);       
//		String    date    =    sDateFormat.format(new    java.util.Date());
//		return date ;
//	}

	public  String getNowStringDate(){
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat(formatter);       
		String    date    =    sDateFormat.format(Calendar.getInstance().getTime());
		return date ;
	}
	public  String getNowStringDate3(){
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat(formatter4);       
		String    date    =    sDateFormat.format(Calendar.getInstance().getTime());
		return date ;
	}
	public  String getNowStringDatetime(){
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat(formatter2);       
		String    date    =    sDateFormat.format(new    java.util.Date());
		return date ;
	}
	public static String dayForWeek(String pTime)  {
    	SimpleDateFormat format = new SimpleDateFormat(formatter2);
    	Calendar c = Calendar.getInstance();
    	try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	int dayForWeek = 0;
    	if(c.get(Calendar.DAY_OF_WEEK) == 1){
    	dayForWeek = 7;
    	}else{
    	dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
    	}
    	String strweek = "";
    	switch(dayForWeek){
    	case 1:strweek = "星期一";
    	break ;
    	case 2:strweek = "星期二";
    	break ;
    	case 3:strweek = "星期三";
    	break ;
    	case 4:strweek = "星期四";
    	break ;
    	case 5:strweek = "星期五";
    	break ;
    	case 6:strweek = "星期六";
    	break ;
    	case 7:strweek = "星期天";
    	break ;
    	}
    	return strweek;
    	}
	public static int dayForWeek1(String pTime)  {
    	SimpleDateFormat format = new SimpleDateFormat(formatter2);
    	Calendar c = Calendar.getInstance();
    	try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	int dayForWeek = 0;
    	if(c.get(Calendar.DAY_OF_WEEK) == 1){
    	dayForWeek = 7;
    	}else{
    	dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
    	}
    	int  strweek = 0;
    	switch(dayForWeek){
    	case 1:strweek =1;
    	break ;
    	case 2:strweek = 2;
    	break ;
    	case 3:strweek = 3;
    	break ;
    	case 4:strweek = 4;
    	break ;
    	case 5:strweek = 5;
    	break ;
    	case 6:strweek = 6;
    	break ;
    	case 7:strweek = 7;
    	break ;
    	}
    	return strweek;
    	}
	
	public static boolean isLocation(Date date){

		String time1 = "23:50:00";
		String time2 = "08:00:00";
		String datetime = DateUtils.dateToString(date, formatter);
		String strweek = DateUtils.dayForWeek(datetime);
		Date temp1 = DateUtils.stringToDate(datetime, formatter);
		String temp2 = DateUtils.dateToString(temp1, formatter3);
		Date hhmmssdate = DateUtils.stringToDate(temp2, formatter3);
		Date etime = DateUtils.stringToDate(time1, formatter3);
		Date stime = DateUtils.stringToDate(time2, formatter3);
		if(false)//(strweek.equals("星期六") || strweek.equals("星期天"))
		{
			return false;
		}else if(!(hhmmssdate.before(etime) && hhmmssdate.after(stime))){
			return false;
		}
		return true ;
    }
}
