package com.jpn.yanagi.FixedFormMailCreation;
import com.jpn.yanagi.FixedFormMailCreation.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.view.View;

public class MailSetting extends TabActivity {
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.setting);

	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    intent = new Intent().setClass(this, DataRegistActivity.class);
	    spec = tabHost.newTabSpec("regist").setIndicator("登録")
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, DataRegistActivity.class);
	    spec = tabHost.newTabSpec("list").setIndicator("一覧")
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, AddressSettingActivity.class);
	    spec = tabHost.newTabSpec("address").setIndicator("宛先")
	                  .setContent(intent);
	    tabHost.addTab(spec);

	}
}