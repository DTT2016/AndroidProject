package com.dtt.dtmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.dtt.dtmap.ShakeListener.OnShakeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * ��ʾMapView�Ļ����÷�
 */
public class MainActivity extends Activity {
	// ��λ���
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	public boolean isFristLocation = true;

	MapView mMapView = null;
	TextView tv = null;
	BaiduMap mBaiduMap;

	// pedometer
	ShakeListener mShakeListener = null;
	Vibrator mVibrator;

	private float mCurrentLatitude;
	private float mCurrentLongitude;
//	private float mLastLatitude;
//	private float mLastLongitude;
	private boolean isDraw = false;
	private boolean isStep = false;
	List<LatLng> points = new ArrayList<LatLng>();

	private int mStep = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		// ��ȡ��ͼ�ؼ�����
		mMapView = (MapView) findViewById(R.id.bmapView);
		tv = (TextView) findViewById(R.id.tv);

		mBaiduMap = mMapView.getMap();
		// �Ŵ�16��
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
		// ��ͨ��ͼ
		// mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

		/*
		 * LatLng center = new LatLng(22.352921, 113.596621); //�����ͼ״̬ MapStatus
		 * mMapStatus = new MapStatus.Builder() .target(center) .zoom(16)
		 * .build(); //����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
		 * 
		 * MapStatusUpdate mMapStatusUpdate =
		 * MapStatusUpdateFactory.newMapStatus(mMapStatus); //�ı��ͼ״̬
		 * mBaiduMap.setMapStatus(mMapStatusUpdate);
		 */
		// mBaiduMap.addOverlay(new
		// MarkerOptions().position(center).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_geo)));

		/*
		 * LatLng center = new LatLng(22.352921, 113.596621); mMapView = new
		 * MapView(this, new BaiduMapOptions().mapStatus(new
		 * MapStatus.Builder().target(center).build()));
		 * mBaiduMap.addOverlay(new
		 * MarkerOptions().position(center).icon(BitmapDescriptorFactory
		 * .fromResource(R.drawable.icon_geo))); setContentView(mMapView);
		 */
		isFristLocation = true;
		initLocation();
		// start location
		// mLocationClient.start();

		// ---------------pedometer------------------
		// drawerSet ();//���� drawer���� �л� ��ť�ķ���
		// �����������
		mVibrator = (Vibrator) getApplication().getSystemService(
				VIBRATOR_SERVICE);
		// ʵ�������ٶȴ����������
		mShakeListener = new ShakeListener(MainActivity.this);

		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				if(isStep){
					mShakeListener.stop();
					mStep++;
					tv.setText("Step: "+mStep);
					mShakeListener.start();
				}
				
				/*
				mShakeListener.stop();
				mStep++;
				tv.setText("Step: "+mStep);
				//startVibrato(); // ��ʼ ��
				
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, mStep + "ҡ��ҡ��ҡ��ҡ",
								Toast.LENGTH_SHORT).show();

						mVibrator.cancel();
						mShakeListener.start();
					}
				}, 1000);
				mShakeListener.start();
				*/
			}
		});
		// ----------------------------------------------------//
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);

		menu.add(0, 0, 0, "��¼�켣");
		menu.add(0, 1, 0, "��ʼ�ǲ�");
		menu.add(0, 2, 0, "����켣");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			if(isDraw){
				isDraw = false;
				Toast.makeText(MainActivity.this, "���ƹ켣����", Toast.LENGTH_SHORT).show();
				//drawPolyline();
				item.setTitle("��¼�켣");
			}
			else{
				isDraw = true;
				Toast.makeText(MainActivity.this, "���ƹ켣��ʼ", Toast.LENGTH_SHORT).show();
				//drawPolyline();
				item.setTitle("ֹͣ����");
			}
			break;
		case 1:
			if(isStep){
				isStep = false;
				tv.setText("Step: "+mStep);
				item.setTitle("��ʼ�ǲ�");
				Toast.makeText(MainActivity.this, "�ǲ�ֹͣ", Toast.LENGTH_SHORT).show();
			}
			else{
				isStep = true;
				mStep = 0;
				tv.setText("Step: "+mStep);
				item.setTitle("ֹͣ�ǲ�");
				Toast.makeText(MainActivity.this, "�ǲ���ʼ", Toast.LENGTH_SHORT).show();
			}
			break;
		case 2:
			saveState();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void saveState(){
		try {
			//�ж�SDcard�Ƿ���ڲ��ҿɶ�д
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				saveToSdCard("TestRoad.txt");
			}else{
				Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			Toast.makeText(MainActivity.this, "write error", Toast.LENGTH_SHORT).show();
			//e.printStackTrace();
		}
		Toast.makeText(MainActivity.this, "�켣�Ѿ�����", Toast.LENGTH_SHORT).show();
	}
	
	//�����ļ���SD��
	public void saveToSdCard(String filename) throws IOException 
	{
		//�õ��ֻ�Ĭ�ϴ洢Ŀ¼����ʵ����
		//File externDir = Environment.getExternalStorageDirectory();
		File file=new File(Environment.getExternalStorageDirectory(),filename);
		FileOutputStream fos=new FileOutputStream(file);
		
		int plen = 0;
		while(plen <points.size()){
			String content = points.get(plen).toString();
		    fos.write(("<"+plen+">: "+content+"\n").getBytes());
		    plen++;
		}
		
		fos.close();
	}

	private void initLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������
		LocationClientOption option = new LocationClientOption();
		// option.setLocationMode(LocationMode.Hight_Accuracy);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 5000; // ÿ5���ȡһ��λ����Ϣ
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		// option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		// option.setLocationNotify(true);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
		// option.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		// option.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		// option.setIgnoreKillProcess(false);//��ѡ��Ĭ��false����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ��ɱ��
		// option.SetIgnoreCacheException(false);//��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		// option.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
		mLocationClient.setLocOption(option);
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;

			mCurrentLatitude = (float) location.getLatitude();
			mCurrentLongitude = (float) location.getLongitude();

			// ���춨λ����
			MyLocationData locData = new MyLocationData.Builder()
					.latitude(mCurrentLatitude).longitude(mCurrentLongitude)
					.build();

			// ���ö�λ����
			mBaiduMap.setMyLocationData(locData);

			// �����Զ���ͼ��
			BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
					.fromResource(R.drawable.icon_geo);
			MyLocationConfiguration config = new MyLocationConfiguration(
					LocationMode.NORMAL, true, mCurrentMarker);
			mBaiduMap.setMyLocationConfigeration(config);

			// ��һ�ζ�λʱ������ͼλ���ƶ�����ǰλ��
			if (isFristLocation) {
				isFristLocation = false;
				LatLng ll = new LatLng(mCurrentLatitude, mCurrentLongitude);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);

//				mLastLatitude = mCurrentLatitude;
//				mLastLongitude = mCurrentLongitude;
				
				points.add(ll);
			}
			
			if(isDraw){
				addPoint();
			}
			
//			if(isDraw&&!points.isEmpty()){
//				drawPolyline();
//			}
		}

		public void addPoint() {
			LatLng pt = new LatLng(mCurrentLatitude, mCurrentLongitude);
			points.add(pt);
			
			if(!points.isEmpty()){
				drawPolyline();
			}
			
			/*
			float deltaLat = mCurrentLatitude - mLastLatitude;
			float deltaLon = mCurrentLongitude - mLastLongitude;
			int distance = (int) Math.sqrt(deltaLat * deltaLat + deltaLon
					* deltaLon);
            
			if (true||distance > 10) {
				LatLng pt = new LatLng(mCurrentLatitude, mCurrentLongitude);
				points.add(pt);

				mLastLatitude = mCurrentLatitude;
				mLastLongitude = mCurrentLongitude;
			}
			*/
		}
		
		public void drawPolyline(){			
			// �������ͼ��
			//mMapView.getMap().clear();

			OverlayOptions ooPolyline = new PolylineOptions().width(6)
					.color(0xAAFF0000).points(points);
			mBaiduMap.addOverlay(ooPolyline);
		}
	}

	@Override
	protected void onStart() {
		// ����ͼ�㶨λ
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
		// �������򴫸���
		// myOrientationListener.start();
		super.onStart();
	}

	@Override
	protected void onStop() {
		// �ر�ͼ�㶨λ
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();

		// �رշ��򴫸���
		// myOrientationListener.stop();
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// activity ��ͣʱͬʱ��ͣ��ͼ�ؼ�
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity �ָ�ʱͬʱ�ָ���ͼ�ؼ�
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity ����ʱͬʱ���ٵ�ͼ�ؼ�
		mMapView.onDestroy();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}

	// ������
	public void startVibrato() {
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1);
		// ��һ�����������ǽ������飬
		// �ڶ����������ظ�������-1Ϊ���ظ�����-1���pattern��ָ���±꿪ʼ�ظ�
	}

}
