package com.pengjun.ka.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pengjun.ka.component.GalleryFlow;
import com.pengjun.ka.db.model.AccountRecord;
import com.pengjun.ka.db.service.ArService;
import com.pengjun.ka.fragment.ArSearchResultFragment;
import com.pengjun.ka.utils.Constants;
import com.pengjun.ka.utils.Constants.ChartType;
import com.pengjun.keepaccounts.R;

public class ArChartActivity extends Activity {

	private GalleryFlow gfChart;
	private ProgressBar pbLoad;
	private List<AccountRecord> arList = new ArrayList<AccountRecord>();
	private TextView tvTilte;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.ar_chart);
		gfChart = (GalleryFlow) findViewById(R.id.gfChart);
		pbLoad = (ProgressBar) findViewById(R.id.pbLoad);
		tvTilte = (TextView) findViewById(R.id.tvTilte);

		Integer[] images = { R.drawable.chart_columnar, R.drawable.chart_pie, R.drawable.chart_line_day,
				R.drawable.chart_line_mouth, R.drawable.chart_line_year };

		ImageAdapter adapter = new ImageAdapter(this, images);
		adapter.createReflectedImages();// 创建倒影效果
		gfChart.setFadingEdgeLength(0);
		gfChart.setSpacing(-25); // 图片之间的间距
		gfChart.setAdapter(adapter);

		gfChart.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(ArChartActivity.this, ArChartDisplayActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constants.INTENT_AR_LIST, (Serializable) arList);
				switch (position) {
				case 0:
					bundle.putSerializable(Constants.INTENT_AR_CHART_TYPE, ChartType.bar);
					break;
				case 1:
					bundle.putSerializable(Constants.INTENT_AR_CHART_TYPE, ChartType.pie);
					break;
				case 2:
					bundle.putSerializable(Constants.INTENT_AR_CHART_TYPE, ChartType.line_day);
					break;
				case 3:
					bundle.putSerializable(Constants.INTENT_AR_CHART_TYPE, ChartType.line_mouth);
					break;
				case 4:
					bundle.putSerializable(Constants.INTENT_AR_CHART_TYPE, ChartType.line_year);
					break;
				}
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
		gfChart.setSelection(1);

		showProgress();
		new LoadArTask().execute();

	}

	private void showProgress() {
		pbLoad.setVisibility(View.VISIBLE);
		gfChart.setVisibility(View.GONE);
		tvTilte.setVisibility(View.GONE);

	}

	private void hideProgress() {
		pbLoad.setVisibility(View.GONE);
		gfChart.setVisibility(View.VISIBLE);
		tvTilte.setVisibility(View.VISIBLE);
	}

	class LoadArTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			arList = ArService.queryAr(ArSearchResultFragment.getArSearchCondition(), 0, -1);
			return null;

		}

		@Override
		protected void onPostExecute(Void v) {
			hideProgress();
			super.onPostExecute(v);
		}
	}

	class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;
		private Integer[] mImageIds;
		private ImageView[] mImages;

		public ImageAdapter(Context c, Integer[] ImageIds) {
			mContext = c;
			mImageIds = ImageIds;
			mImages = new ImageView[mImageIds.length];
		}

		/**
		 * 创建倒影效果
		 * 
		 * @return
		 */
		public boolean createReflectedImages() {
			// 倒影图和原图之间的距离
			final int reflectionGap = 1;
			int index = 0;
			for (int imageId : mImageIds) {
				// 返回原图解码之后的bitmap对象
				Bitmap originalImage = BitmapFactory.decodeResource(mContext.getResources(), imageId);
				int width = originalImage.getWidth();
				int height = originalImage.getHeight();
				// 创建矩阵对象
				Matrix matrix = new Matrix();

				// 指定矩阵(x轴不变，y轴相反)
				matrix.preScale(1, -1);

				// 将矩阵应用到该原图之中，返回一个宽度不变，高度为原图1/2的倒影位图
				Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2,
						matrix, false);

				// 创建一个宽度不变，高度为原图+倒影图高度的位图
				Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2),
						Config.ARGB_8888);

				// 将上面创建的位图初始化到画布
				Canvas canvas = new Canvas(bitmapWithReflection);
				canvas.drawBitmap(originalImage, 0, 0, null);

				Paint deafaultPaint = new Paint();
				deafaultPaint.setAntiAlias(false);
				// canvas.drawRect(0, height, width, height +
				// reflectionGap,deafaultPaint);
				canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
				Paint paint = new Paint();
				paint.setAntiAlias(false);

				/**
				 * 参数一:为渐变起初点坐标x位置， 参数二:为y轴位置， 参数三和四:分辨对应渐变终点， 最后参数为平铺方式，
				 * 这里设置为镜像Gradient是基于Shader类，所以我们通过Paint的setShader方法来设置这个渐变
				 */
				LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
						bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
						TileMode.MIRROR);
				// 设置阴影
				paint.setShader(shader);
				paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
				// 用已经定义好的画笔构建一个矩形阴影渐变效果
				canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

				// 创建一个ImageView用来显示已经画好的bitmapWithReflection
				ImageView imageView = new ImageView(mContext);
				imageView.setImageBitmap(bitmapWithReflection);
				// 设置imageView大小 ，也就是最终显示的图片大小
				imageView.setLayoutParams(new GalleryFlow.LayoutParams(256, 256));
				// imageView.setScaleType(ScaleType.MATRIX);
				mImages[index++] = imageView;
			}
			return true;
		}

		@SuppressWarnings("unused")
		private Resources getResources() {
			return null;
		}

		public int getCount() {
			return mImageIds.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return mImages[position];
		}
	}
}
