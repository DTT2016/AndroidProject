package com.dtt.chemlab.activity;

import com.dtt.chemlab.R;
import com.dtt.chemlab.fragment.ContactFragment;
import com.dtt.chemlab.fragment.SystemFragment;
import com.dtt.chemlab.fragment.NewsFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends SlidingActivity implements
		OnCheckedChangeListener {

	//TitleBar items
	private TextView titleText;
	private Button titleMenu;
	
	// Left Sliding Menu Items
	//private ImageView userPhoto;
	private TextView userIDText;
	private TextView userNameText;
	private TextView userRoleText;
	private Button loginButton;
	private Button contactUsButton;
	
	private RadioGroup mainGroup;
	private FragmentManager fragmentManager;
	private NewsFragment newsFragment;
	private ContactFragment contactFragment;
	private SystemFragment functionFragment;
	private boolean showMenu = false;

	@Override
	protected void setContents() {
		// super.setContents();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		content = findViewById(R.id.content_son);
		menu = findViewById(R.id.menu);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initView() {
		super.initView();

		titleText = (TextView) findViewById(R.id.title_text);
		titleMenu = (Button) findViewById(R.id.title_menu);
		titleText.setText("��ҳ");

		mainGroup = (RadioGroup) findViewById(R.id.main_tab);
		mainGroup.setOnCheckedChangeListener(this);
		fragmentManager = getFragmentManager();
		newsFragment = (NewsFragment) fragmentManager
				.findFragmentById(R.id.news_fragment);
		functionFragment = (SystemFragment) fragmentManager
				.findFragmentById(R.id.function_fragment);
		contactFragment = (ContactFragment) fragmentManager
				.findFragmentById(R.id.contact_fragment);
		showFragment(newsFragment);

		View view = LayoutInflater.from(this).inflate(R.layout.layout_popmenu, null);
		final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		
		titleMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.showAsDropDown(v,-50, 0);
				// ʹ��ۼ�
				popupWindow.setFocusable(true);
				//ˢ��״̬������ˢ�·�����Ч��
				popupWindow.update();
			}
		});
		
		findViewById(R.id.title_image).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!showMenu) {
					scrollToMenu();
					showMenu = true;
				} else {
					scrollToContent();
					showMenu = false;
				}
				
			}
		});
		
		findViews();
	}

	private void findViews() {
		//userPhoto = (ImageView) findViewById(R.id.user_photo_left);
		userIDText = (TextView) findViewById(R.id.user_id_left);
		userNameText = (TextView) findViewById(R.id.user_name_left);
		userRoleText = (TextView) findViewById(R.id.user_role_left);
		loginButton = (Button) findViewById(R.id.login_left);
		contactUsButton = (Button) findViewById(R.id.contact_us_left);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		
		contactUsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userIDText.setText(" ID :007");
				userNameText.setText("Name:007");
				userRoleText.setText("Role:Administrator");
			}
		});
	}

	private void showFragment(Fragment fragment) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// transaction.replace(R.id.content_layout, fragment);
		transaction.hide(newsFragment).hide(functionFragment);

		transaction.show(fragment).commit();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int id) {
		switch (id) {
		case R.id.radio_button1:
			showFragment(newsFragment);
			titleText.setText("��ҳ");
			break;
		case R.id.radio_button2:
			showFragment(contactFragment);
			titleText.setText("��ϵ��");
			break;
		case R.id.radio_button3:
			showFragment(functionFragment);
			titleText.setText("ϵͳ����");
			break;
		case R.id.radio_button4:
			showFragment(functionFragment);
			titleText.setText("��������");
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// return super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// return super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Settings!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_quit:
			finish();
			break;
		default:
			break;
		}

		return true;
	}
}
