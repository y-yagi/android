package com.jpn.yanagi.FixedFormMailCreation;

import com.jpn.yanagi.FixedFormMailCreation.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final String tag = "MAIN_ACTIVITY";
	private final static int 
		MENU_SETTING = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
        setContentView(R.layout.main);	
        getDataList();
    }
    
    public void getDataList() {    	
    	TransmissionDataDao grDao;
    	SQLiteDatabase db;
		grDao = new TransmissionDataDao(this);
		db = grDao.getWritableDatabase();
       	ArrayAdapter<TransmissionData> adapter = new ArrayAdapter<TransmissionData>(this, R.layout.history_button_list);
    	Cursor cursor = null;
    	try { 
    		cursor = grDao.read(db);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return;
    	}
    	cursor.moveToFirst();
    	int cursorCount = cursor.getCount();
    	
    	String address = "", name = "", subject = "", body = "";
    	int seq = -1;
    	for(int i = 0; i < cursorCount; i++) {
    		seq = cursor.getInt(TransmissionDataDao.SEQ_POS);
    		name = cursor.getString(TransmissionDataDao.NAME_POS);
    		address = cursor.getString(TransmissionDataDao.ADDRESS_POS);
    		subject = cursor.getString(TransmissionDataDao.SUBJECT_POS);
    		body = cursor.getString(TransmissionDataDao.BODY_POS);
    	    TransmissionData logData = new TransmissionData(
    	    	seq, name, address, subject, body
   	    	);
   	    	adapter.add(logData);
    	    cursor.moveToNext();
    	}    
    	cursor.close();
        ListView listView = (ListView) findViewById(R.id.buttonList);
        listView.setAdapter(adapter); 
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                ListView listView = (ListView) parent;
                // クリックされたアイテムを取得します
        		TransmissionData item = (TransmissionData) listView.getItemAtPosition(position);
            	Intent intent = new Intent();
            	intent.putExtra(Intent.EXTRA_SUBJECT, item.subject);
            	intent.putExtra(Intent.EXTRA_TEXT, item.body);
            	intent.setAction(Intent.ACTION_SENDTO);
            	intent.setData(Uri.parse("mailto:" + item.address));
            	startActivity(intent);		
			}       		
        });
        db.close(); 
        
        if (cursorCount == 0) {
        	TextView mainText= (TextView) findViewById(R.id.mainText);
        	mainText.setText("データが登録されていません。設定からデータの登録を行って下さい。");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
       	MenuItem itemSetting = menu.add(1, MENU_SETTING, 0, "設定");
       	itemSetting.setIcon(android.R.drawable.ic_menu_preferences);
    	return true;    	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case MENU_SETTING:
    		Intent settingIntent = new Intent(this, com.jpn.yanagi.FixedFormMailCreation.MailSetting.class);
    		startActivity(settingIntent);
    		return true;
    	default :
    		// no progress
    	}
    	return true;
    } 
   
    // TODO:送信処理
    public void sendMail(TransmissionData data) {
		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SUBJECT, data.subject);
		intent.putExtra(Intent.EXTRA_TEXT, data.body);
		
		intent.setAction(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("mailto:" + data.address));
		this.startActivity(intent);		
    }	    
}
