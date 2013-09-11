package com.pengjun.ka.chart;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.pengjun.achart.BaseChart;
import com.pengjun.ka.db.model.AccountRecord;

public abstract class KaBaseChart extends BaseChart {

	public abstract void compute(List<AccountRecord> arList);

	public abstract View getView(Context context);

	public abstract void setZoomEnabled(boolean isZoomEnabled);

}
