package com.dtt.chemlab.activity;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.dtt.chemlab.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WebActivity extends Activity {
	private EditText aText;
	private EditText bText;
	private TextView resultView;
	private Button queryButton;
	private String a, b, result;
	
	private Handler handle = new Handler() {

		public void handleMessage(Message msg) {
			String result = msg.obj.toString();
			if ("Y".equalsIgnoreCase(result)) {
				result = "����";
			} else {
				result = "����";
			}
			resultView.setText(result);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		aText = (EditText) findViewById(R.id.args_a);
		bText = (EditText) findViewById(R.id.args_b);
		resultView = (TextView) findViewById(R.id.result_text);
		queryButton = (Button) findViewById(R.id.query_btn);

		aText.setHint("����QQ�Ų�ѯ����״̬");
		queryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �ֻ����루�Σ�
				a = aText.getText().toString().trim();
				b = bText.getText().toString().trim();
				// ���ж��û�������ֻ����루�Σ��Ƿ�Ϸ�
				/*if ("".equals(a) || "".equals(b) ) {
					resultView.setText("��������");
					return;
				}*/
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						//Log.d("MainActivity", "in thread");
						getRemoteInfo(a,b);
						Message msg = handle.obtainMessage();
						msg.obj = result;
						handle.sendMessage(msg);
					}
				}).start();

			}
		});
	}

	public void getRemoteInfo(String a,String b) {
		// �����ռ�
		String nameSpace = "http://WebXml.com.cn/";
		// ���õķ�������
		String methodName = "qqCheckOnline";
		// EndPoint
		String endPoint = "http://www.webxml.com.cn/webservices/qqOnlineWebService.asmx";
		// SOAP Action
		String soapAction = "http://WebXml.com.cn/qqCheckOnline";

		// ָ��WebService�������ռ�͵��õķ�����
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// ���������WebService�ӿ���Ҫ�������������mobileCode��userId
		//rpc.addProperty("Word", a);
		rpc.addProperty("qqCode", a);
		//rpc.addProperty("b", b);

		// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);

		envelope.bodyOut = rpc;
		// �����Ƿ���õ���dotNet������WebService
		envelope.dotNet = true;
		// �ȼ���envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// ����WebService
			transport.call(soapAction, envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("MainActivity", envelope.bodyIn.toString());
		// ��ȡ���ص�����
		SoapObject object = (SoapObject) envelope.bodyIn;
		// ��ȡ���صĽ��
		Log.d("MainActivity", object.getProperty(0).toString());
		result = object.getProperty(0).toString();
	}
}