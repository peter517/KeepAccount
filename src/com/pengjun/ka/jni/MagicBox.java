package com.pengjun.ka.jni;

public class MagicBox {
	static {
		System.loadLibrary("ka_magicbox_jni");
	}

	public native static int add(int a, int b);
}
