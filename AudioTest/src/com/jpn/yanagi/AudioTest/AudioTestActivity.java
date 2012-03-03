package com.jpn.yanagi.AudioTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.jpn.yanagi.AudioTest.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class AudioTestActivity extends Activity {
	
	public MediaRecorder mrec = null;
	private Button startRecording = null;
	private Button stopRecording = null;
	private Button play = null;
	private Button stop = null;
	private static final String TAG = "SoundRecordingDemo";
	File audiofile;
	MediaPlayer mp;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
         
        mrec = new MediaRecorder();
        
        startRecording = (Button)findViewById(R.id.startrecording);
        stopRecording = (Button)findViewById(R.id.stoprecording);
        play = (Button)findViewById(R.id.play);
        stop = (Button)findViewById(R.id.stop);
        
        startRecording.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v) {
           try
           {
        	   startRecording.setEnabled(false);
        	   stopRecording.setEnabled(true);
        	   stopRecording.requestFocus();
        	  
        	   startRecording();
           }catch (Exception ee)
           {
        	   Log.e(TAG,"Caught io exception " + ee.getMessage());
           }
           
          }
        });
        
        stopRecording.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v) {
        	  startRecording.setEnabled(true);
        	  stopRecording.setEnabled(false);
        	  startRecording.requestFocus();
        	  stopRecording();
              processaudiofile();
          }
          
        });
        
        play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				play.setEnabled(false);
				stop.setEnabled(true);
				play();
			}
		}); 
        
        stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stop.setEnabled(false);
				play.setEnabled(true);
				stop();
			}
		}); 
        
	   stopRecording.setEnabled(false);
	   startRecording.setEnabled(true);
       play.setEnabled(true);
       stop.setEnabled(false);
       
    }
    protected void processaudiofile() {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
            
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
        ContentResolver contentResolver = getContentResolver();
        
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        // this does not always seem to work cleanly....
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
      }

    
    protected void startRecording() throws IOException 
    {
//        mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
    	mrec.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        mrec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        mRecorder.setOutputFile("/sdcard/yousuck2.3gp");
//        mrec.setOutputFile("/dscard/test.3gp");
        if (audiofile == null) {
	          // File sampleDir = Environment.getExternalStorageDirectory();
	          String filePath = Environment.getExternalStorageDirectory() + "/"
					+ "test.3gp";
	          try { 
	        	  // audiofile = File.createTempFile("test", ".3gp", sampleDir);
	        	  audiofile= new File(filePath);
	        	  if (!audiofile.isFile()) {
	        		  audiofile.delete();
	        	  }
	          } 
	          catch (Exception e)
	          {
	              Log.e(TAG,"sdcard access error");
	              return;
	          }
        }

        mrec.setOutputFile(audiofile.getAbsolutePath());
       
       mrec.prepare();
       mrec.start();
    }

    protected void stopRecording() {
        mrec.stop();
        mrec.release();
   }
    
    protected void play() {
    	String filePath = Environment.getExternalStorageDirectory() + "/"
    		+ "test.3gp";
	    File tempFile = new File(filePath);
	    if (!tempFile.isFile()) {
	    	Toast.makeText(this, "not recorded", Toast.LENGTH_LONG);
	    	return; 
	    }
		this.mp = MediaPlayer.create(getBaseContext(), Uri.fromFile(tempFile));
		this.mp.start();
    }
    
    protected void stop() {
    	this.mp.stop();
    }
}
