package com.echarm.qasystem.rating.util;

public class StringFormat {

	public StringFormat() {
		// TODO Auto-generated constructor stub
	}
	
	public static String snake2CamelCapital(String str) {
		String resultStr = "";
		String[] strArr = str.split("_");
		for(int index = 0; index < strArr.length; index++)
			resultStr = resultStr + strArr[index].substring(0, 1).toUpperCase() + strArr[index].substring(1);
		return resultStr;
	}
	
	public static String snake2Camel(String str) {
		String resultStr = snake2CamelCapital(str);
		resultStr = resultStr.substring(0,1).toLowerCase() + resultStr.substring(1);
		return resultStr;
	}
}
