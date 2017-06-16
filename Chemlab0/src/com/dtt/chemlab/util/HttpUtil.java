package com.dtt.chemlab.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.widget.Toast;

public class HttpUtil {

	public static void sendHttpRequest(final String address,final HttpCallbackListener listener) {
		
		if (!isNetworkAvailable()) {
			Toast.makeText(MyApplication.getContext(), "Network is unavailable!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connection = null;
				
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setDoInput(true);
					connection.setDoOutput(true);
					
					//POST
					/*connection.setRequestMethod("POST");
					DataOutputStream out = new DataOutputStream(connection.getOutputStream());
					out.writeBytes("username=admin&password=123456");*/
					
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					
					while ((line=reader.readLine())!=null) {
						response.append(line);
					}
					
					if (listener!=null) {
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener!=null) {
						listener.onError(e);
					}
				} finally {
					if (connection!=null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	private static boolean isNetworkAvailable() {
		
		return true;
	}
	
	public static String requestWebservice(String methodName,
			          String [] parameters,String [] parValues){
		String response="";

        String namespace="http://211.66.132.31/";
		String endPoint = "http://211.66.136.32/webservice/WebService1.asmx";
		
		String soapAction = namespace+methodName;

		// ָ��WebService�������ռ�͵��õķ�����
		SoapObject rpc = new SoapObject(namespace, methodName);

		// ���������WebService�ӿ���Ҫ����Ĳ���
		if (parameters!=null) {
			for (int i = 0; i < parameters.length; i++) {
				/*String param = parameters[i].toString();
				String value = parValues[i].toString();*/
				rpc.addProperty(parameters[i], parValues[i]);
			}
		}
		//rpc.addProperty("Word", a);
		//rpc.addProperty("qqCode", a);
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

		//Log.d("MainActivity", envelope.bodyIn.toString());
		// ��ȡ���ص�����
		SoapObject object = (SoapObject) envelope.bodyIn;
		// ��ȡ���صĽ��
		//Log.d("MainActivity", object.getProperty(0).toString());
		response = object.getProperty(0).toString();
		return response;
	}
}
