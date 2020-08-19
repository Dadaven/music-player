package com.example.util;

public class MyTimeUtil {
	public static String formatSecond(int seconds){  
	int min=seconds/1000/60;
	int sec=seconds/1000%60;
	return min+":"+sec;
   } 

}
