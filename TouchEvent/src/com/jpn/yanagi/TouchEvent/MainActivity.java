package com.jpn.yanagi.TouchEvent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
    DrawNoteView view;
    private static final int MENU_RETRY = 0;
    private static final int MENU_SAVE = 1;
    /** アプリの初期化 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 描画クラスを設定
        view = new DrawNoteView(getApplication(), this);
        setContentView(view);
    }
    /** メニューの生成イベント */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, MENU_RETRY, 0, "Retry");
//    	menu.add(0, MENU_SAVE, 0, "Save");
    	return true;
    }
    /** メニューがクリックされた時のイベント */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch ( item.getItemId() ) {
    	case MENU_RETRY:
    		//view.clearDrawList(); break;
    		view.reArrangement();
    		break;
    	case MENU_SAVE:
    		view.saveToFile();
    		break;
    	}
    	return true;
    }
}
    