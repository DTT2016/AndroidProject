package com.laohuai.testview.views;

import android.view.LayoutInflater;
import android.view.View;

import com.laohuai.testview.R;

/**
 * һ�����ڲ��Ե�view�����ߣ��������ڷ���һ��view����Ϊ���view����չʾ����û���κ��������߼���������ֻ�Ǵ�����һ��LayoutInflater
 * ������ʵ��ʹ�ù����У��������ľ�����������Դ�����Ҫ�Ĳ���
 * 
 * @author laohuai
 * 
 */

public class ViewHolder0 {
	private LayoutInflater inflater;

	private View view;

	public ViewHolder0(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	public View getView() {
		view = inflater.inflate(R.layout.view0, null);

		return view;

	}

}
