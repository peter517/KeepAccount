package com.pengjun.ka.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.pengjun.ka.tools.MyDebug;
import com.pengjun.keepaccounts.R;

@SuppressLint("DrawAllocation")
public class FocusedChangeImageView extends ImageView {

	public FocusedChangeImageView(Context context) {
		super(context);
		init();
	}

	public FocusedChangeImageView(Context context,
			AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		init();
	}

	public void init() {
		// this.setFocusableInTouchMode(true);
		// this.setFocusable(true);
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				MyDebug.printFromPJ("onFocusChange");
				if (hasFocus) {
					MyDebug.printFromPJ("hasFocus");
					FocusedChangeImageView.this
							.setBackgroundResource(R.drawable.text_focused);
				} else {
					FocusedChangeImageView.this
							.setBackgroundColor(Color.TRANSPARENT);
				}

			}

		});
	}
}
