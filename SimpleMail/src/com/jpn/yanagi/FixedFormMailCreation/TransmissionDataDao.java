package com.jpn.yanagi.FixedFormMailCreation;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class TransmissionDataDao extends SQLiteOpenHelper{
	private final static String DB_NAME = "TransmissionData.db";
	private final static String DB_TABLE = "transmission_data";
	private final static int DB_VERSION = 2;
	private final static String DATA_LIMIT = "1000";
	private final String TAG = "TransmissionDataDao";
	
	public interface DataColumns extends BaseColumns {
		public static final String SEQ = "seq";
		public static final String NAME = "name";
		public static final String ADDRESS = "address";
		public static final String SUBJECT = "subject";
		public static final String BODY = "body";
	}
	public static int SEQ_POS = 0;
	public static int ADDRESS_POS = 1;
	public static int NAME_POS = 2;
	public static int SUBJECT_POS = 3;
	public static int BODY_POS = 4;

	
	public TransmissionDataDao(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {		
		db.execSQL(
				"create table if not exists " + DB_TABLE + "(" 
				+ DataColumns.SEQ + " integer primary key autoincrement, " 
				+ DataColumns.ADDRESS + " text," 
				+ DataColumns.NAME + " text,"
				+ DataColumns.SUBJECT + " text,"
				+ DataColumns.BODY + " text" 
				+ ");"
		);
	
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(
				"drop table if exists" + DB_TABLE
		);
		onCreate(db);
	}
	
	public long insert(SQLiteDatabase db, String name, String address, String subject, String body) {
		ContentValues values = new ContentValues();
		values.put(DataColumns.ADDRESS, address);
		values.put(DataColumns.NAME, name);
		values.put(DataColumns.SUBJECT, subject);
		values.put(DataColumns.BODY, body);
		return db.insert(DB_TABLE, "", values);		
	}
	
	public Cursor read(SQLiteDatabase db)  throws Exception{
		Cursor c = db.query(
				DB_TABLE, 
				new String[] {DataColumns.SEQ, DataColumns.ADDRESS, DataColumns.NAME, DataColumns.SUBJECT, DataColumns.BODY}, 
				null,
				null,
				null,
				null,
				DataColumns.SEQ,
				DATA_LIMIT
				);
		return c;	
	}
	
	public int update(SQLiteDatabase db, int seq, String name, String address, String subject, String body)  throws Exception{
		String seqStr = String.valueOf(seq);
		ContentValues values = new ContentValues();
		values.put(DataColumns.ADDRESS, address);
		values.put(DataColumns.NAME, name);
		values.put(DataColumns.SUBJECT, subject);
		values.put(DataColumns.BODY, body);
		
		int result  = db.update(
				DB_TABLE, 
				values,
				"seq = ?",
				new String[]{seqStr}
				);
		return result;
	}	
	
	public int delete(SQLiteDatabase db, int seq)  throws Exception{
		String seqStr = String.valueOf(seq);
		int result  = db.delete(
				DB_TABLE, 
				"seq = ?",
				new String[]{seqStr}
				);
		return result;
	}
}
