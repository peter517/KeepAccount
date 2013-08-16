package com.pengjun.ka.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class FragmentDirector {

	public static void replaceFragment(FragmentActivity activity, int layoutId, Fragment fragment) {
		FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.replace(layoutId, fragment);
		ft.commitAllowingStateLoss();
	}

}
