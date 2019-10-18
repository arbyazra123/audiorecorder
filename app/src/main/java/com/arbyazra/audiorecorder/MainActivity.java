package com.arbyazra.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arbyazra.audiorecorder.db.DatabaseContract;
import com.arbyazra.audiorecorder.db.DatabaseHelper;
import com.arbyazra.audiorecorder.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private EditText mAudioName;
    private Button btn;


    //DB SETS
    private DatabaseHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.showList);

        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        stop.setEnabled(false);
        play.setEnabled(false);
        mAudioName = findViewById(R.id.et_name);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myAudioRecorder!=null) {
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                }
                mAudioName.setText("");
                Intent i = new Intent(MainActivity.this,ListActivity.class);
                startActivity(i);
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},0);
                } else {
                    outputFile = getExternalCacheDir().getAbsolutePath() + "/"+mAudioName.getText()+".3gp";
                    myAudioRecorder = new MediaRecorder();
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(outputFile);
                    myAudioRecorder.setAudioSamplingRate(16000);
                    mAudioName.setEnabled(false);
                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IllegalStateException ise) {
                        ise.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    record.setEnabled(false);
                    stop.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    myAudioRecorder.stop();
                } catch (RuntimeException e){
                    e.printStackTrace();
                }

                myAudioRecorder.release();
                record.setEnabled(true);
                stop.setEnabled(false);
                play.setEnabled(true);
                mAudioName.setEnabled(true);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                myAudioRecorder = null;
                mDbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.AudioBase.COLUMN_NAME_FILE, mAudioName.getText().toString());
                values.put(DatabaseContract.AudioBase.COLUMN_NAME_OUTPUTFILE, outputFile);
                values.put(DatabaseContract.AudioBase.COLUMN_NAME_ONCREATED,dateFormat.format(date));

                long newRowId = db.insert(DatabaseContract.AudioBase.TABLE_NAME, null, values);
                if(newRowId>0){
                    Toast.makeText(getApplicationContext(),"Successfully inserted",Toast.LENGTH_SHORT).show();
                    Log.d("Name",mAudioName.getText()+" was inserted");
                    Log.d("Path",outputFile+" was inserted");
                    Log.d("Path",dateFormat.format(date)+" was inserted");
                } else {
                    Toast.makeText(getApplicationContext(),"failed to insert",Toast.LENGTH_SHORT).show();
                    Log.d("DATABASE",mAudioName.getText()+" failed to inserted");

                }

                Toast.makeText(MainActivity.this,outputFile,Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();

                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });


    }
}
