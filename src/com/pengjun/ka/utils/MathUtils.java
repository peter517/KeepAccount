package com.pengjun.ka.utils;

import java.text.DecimalFormat;

public class MathUtils {

	public static float formatFloat(Float f) {
		return Float.valueOf(new DecimalFormat("#.00").format(f));
	}

	public static double formatDouble(Double f) {
		return Double.valueOf(new DecimalFormat("#.00").format(f));
	}

}
