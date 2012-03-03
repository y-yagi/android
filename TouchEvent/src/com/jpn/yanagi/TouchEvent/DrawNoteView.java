package com.jpn.yanagi.TouchEvent;

import android.util.Log;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.MotionEvent;
import android.widget.Toast;

public class DrawNoteView extends View {
	Bitmap bmp = null;
	Canvas bmpCanvas;
	Point oldpos = new Point(-1, -1);
	Paint strokePaintStart;
	Paint fillPaintStart; 
	Context context;
	Activity mainActiviy;
	
	Paint strokePaintEnd;
	Paint fillPaintEnd;
	MediaPlayer mp;
	
	boolean connect = false;
	public final int CIRCLE_SIZE = 60;
	public int CIRCLE_START_X = 200;
	public int CIRCLE_START_Y = 200; 
	
	public int CIRCLE_END_X = 500;
	public int CIRCLE_END_Y = 600;

	public DrawNoteView(Context c, Activity mainActivity) {
		super(c);
		setFocusable(true);
	
		this.mainActiviy = mainActivity;
		this.strokePaintStart = new Paint();
		this.strokePaintStart.setColor(Color.BLUE);
		this.strokePaintStart.setStyle(Paint.Style.STROKE);
		this.strokePaintStart.setStrokeWidth(5); 
		this.fillPaintStart = new Paint();
		this.fillPaintStart.setColor(Color.BLUE);
		this.fillPaintStart.setStyle(Paint.Style.FILL);
		this.fillPaintStart.setStrokeWidth(5);  
		
		this.strokePaintEnd = new Paint();
		this.strokePaintEnd.setColor(Color.RED);
		this.strokePaintEnd.setStyle(Paint.Style.STROKE);
		this.strokePaintEnd.setStrokeWidth(5); 
		this.fillPaintEnd = new Paint();
		this.fillPaintEnd.setColor(Color.RED);
		this.fillPaintEnd.setStyle(Paint.Style.FILL);
		this.fillPaintEnd.setStrokeWidth(5); 
		
		this.context = c;
		this.mp = MediaPlayer.create(getContext(), R.raw.ta_ge_quiz_yes01);
	}

	public void clearDrawList() {
		bmpCanvas.drawColor(Color.WHITE);
		bmpCanvas.drawCircle(this.CIRCLE_START_X, this.CIRCLE_START_Y, this.CIRCLE_SIZE, this.strokePaintStart);
		bmpCanvas.drawCircle(this.CIRCLE_END_X, this.CIRCLE_END_Y, this.CIRCLE_SIZE, this.strokePaintEnd);
		invalidate();
	}

	public void saveToFile() {
		// 保存先の決定
		String status = Environment.getExternalStorageState();
		File fout;
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			fout = Environment.getDataDirectory();
		} else {
			fout = new File("/sdcard/DrawNoteK/");
			fout.mkdirs();
		}
		Date d = new Date();
		String fname = fout.getAbsolutePath() + "/";
		fname += String.format("%4d%02d%02d-%02d%02d%02d.png", (1900 + d
				.getYear()), d.getMonth(), d.getDate(), d.getHours(), d
				.getMinutes(), d.getSeconds());
		// 画像をファイルに書き込む
		try {
			FileOutputStream out = new FileOutputStream(fname);
			bmp.compress(CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
		}
	}

	/** 画面サイズが変更された時 */
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		bmpCanvas = new Canvas(bmp);
		bmpCanvas.drawColor(Color.WHITE);
	}

	/** 描画イベント */
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(bmp, 0, 0, null); 
		bmpCanvas.drawCircle(this.CIRCLE_START_X, this.CIRCLE_START_Y, this.CIRCLE_SIZE, this.strokePaintStart);
		bmpCanvas.drawCircle(this.CIRCLE_END_X, this.CIRCLE_END_Y, this.CIRCLE_SIZE, this.strokePaintEnd);
	}

	/** タッチイベント */
	public boolean onTouchEvent(MotionEvent event) {
		// 描画位置の確認
		int x = (int) event.getX();
		int y = (int) event.getY();
		int touchAction = event.getAction();
		Point cur = new Point(x, y);
		
		if (oldpos.x < 0) {
			oldpos = cur;
		}
		// 描画属性を設定
		Paint paint = new Paint();
		paint.setColor(Color.GRAY);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(10);
		// 円の中だったら線を描画しない
		if (!this.isInsideCircle(this.CIRCLE_START_X, this.CIRCLE_START_Y, this.CIRCLE_SIZE, x, y)) {
			// 線を描画
			bmpCanvas.drawLine(oldpos.x, oldpos.y, cur.x, cur.y, paint);
		}
		// タッチ位置は円の中かチェック
		if (touchAction == MotionEvent.ACTION_DOWN) { 
			if (this.isInsideCircle(this.CIRCLE_START_X, this.CIRCLE_START_Y, this.CIRCLE_SIZE, x, y)) {
				bmpCanvas.drawCircle(this.CIRCLE_START_X, this.CIRCLE_START_Y, this.CIRCLE_SIZE, this.fillPaintStart);
				this.connect = true;
			}
		}
		oldpos = cur;
		// 指を持ち上げたら座標をリセット
		if (touchAction == MotionEvent.ACTION_UP) {
			oldpos = new Point(-1, -1);
			Log.e("view", "connect : " + connect);
			if (this.connect && this.isInsideCircle(this.CIRCLE_END_X, this.CIRCLE_END_Y, this.CIRCLE_SIZE, x, y)) {
				this.mp.start();
				bmpCanvas.drawCircle(this.CIRCLE_END_X, this.CIRCLE_END_Y, this.CIRCLE_SIZE, this.fillPaintEnd);
				Log.e("view", "clear!!");
                AlertDialog.Builder ad = new AlertDialog.Builder(this.mainActiviy);
                ad.setTitle("ダイアログ");
                ad.setMessage("クリアーしました");
                ad.setPositiveButton("もう一回",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int whichButton) {
                    	reArrangement();
                    }
                });
                ad.create();
                ad.show();
				this.connect = false;
			} else { 
				this.connect = false;
				this.clearDrawList();
			}
		}
		invalidate();
		return true;
	}
	
	public boolean isInsideCircle(int circleX, int circleY, int circleSize, int touchX, int touchY) { 
		int half = circleSize;
//		Log.e("isInsideCircl", "pos x: " + touchX + " y:" + touchY);
		if ((touchX >= circleX - half && touchX <= circleX + half) 
				&& (touchY >= circleY - half && touchY <= circleY + half)) {
			return true;
		}
		return false;
	}
	
	public void reArrangement() {
		Random rand = new Random();
		this.CIRCLE_START_X = rand.nextInt(500) + this.CIRCLE_SIZE;
		this.CIRCLE_START_Y = rand.nextInt(500) + this.CIRCLE_SIZE;
		this.CIRCLE_END_X = rand.nextInt(500) + this.CIRCLE_SIZE;
		this.CIRCLE_END_Y = rand.nextInt(500) + this.CIRCLE_SIZE;  
		
		if ((this.CIRCLE_END_X >= this.CIRCLE_START_X - this.CIRCLE_SIZE && this.CIRCLE_START_X <= this.CIRCLE_END_X + this.CIRCLE_SIZE)) {
			this.CIRCLE_END_X += this.CIRCLE_SIZE;
		}
		if ((this.CIRCLE_END_Y >= this.CIRCLE_START_Y - this.CIRCLE_SIZE && this.CIRCLE_START_Y <= this.CIRCLE_END_Y + this.CIRCLE_SIZE)) {
			this.CIRCLE_END_Y += this.CIRCLE_SIZE;
		}
		
		bmpCanvas.drawColor(Color.WHITE);
		bmpCanvas.drawCircle(this.CIRCLE_START_X, this.CIRCLE_START_Y, this.CIRCLE_SIZE, this.strokePaintStart);
		bmpCanvas.drawCircle(this.CIRCLE_END_X, this.CIRCLE_END_Y, this.CIRCLE_SIZE, this.strokePaintEnd);
		invalidate();
	}
	
}
