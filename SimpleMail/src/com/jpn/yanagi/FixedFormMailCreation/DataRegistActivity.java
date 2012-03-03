package com.jpn.yanagi.FixedFormMailCreation;

import com.jpn.yanagi.FixedFormMailCreation.R;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DataRegistActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText addressText;
	private EditText subjectText;
	private EditText bodyText;
	private final static int  MENU_MAIN = 2;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_regist);
		addressText = (EditText)findViewById(R.id.address);
		subjectText = (EditText)findViewById(R.id.subject);
		bodyText = (EditText)findViewById(R.id.body);
    }
    
    public void clickRegist(View v) {
		String address = addressText.getText().toString();
		String body = bodyText.getText().toString();
		String subject = subjectText.getText().toString();
		String name = "";
    	TransmissionDataDao grDao;
    	SQLiteDatabase db;
		grDao = new TransmissionDataDao(this);
		db = grDao.getWritableDatabase();
		long result = grDao.insert(db, name, address, subject, body);
		if (result != -1)  {
			Toast.makeText(this, "データの登録が完了しました", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "データの登録に失敗しました", Toast.LENGTH_LONG).show();
		}
		
    }		
    
    public void clickInputAddress(View v) {
    	// TODO:そもそもなんだっけ？
    	Toast.makeText(this, "listで表示すればいいのか？", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
       	MenuItem itemSetting = menu.add(1, MENU_MAIN, 0, "送信");
       	itemSetting.setIcon(android.R.drawable.ic_menu_send);
    	return true;    	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case MENU_MAIN:
    		Intent settingIntent = new Intent(this, com.jpn.yanagi.FixedFormMailCreation.MainActivity.class);
    		startActivity(settingIntent);
    		return true;
    	default :
    		// no progress
    	}
    	return true;
    } 
    

}