package com.arbyazra.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.arbyazra.audiorecorder.adapter.ListAudioAdapter;
import com.arbyazra.audiorecorder.db.DatabaseContract;
import com.arbyazra.audiorecorder.db.DatabaseHelper;
import com.arbyazra.audiorecorder.model.AudioData;
import com.arbyazra.audiorecorder.R;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    private DatabaseHelper mDbHelper;
    private ArrayList<AudioData> arrayList;
    private AudioData mAudioData;
    private ListAudioAdapter adapter;
    private SwipeRefreshLayout srl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        arrayList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        srl = findViewById(R.id.srl_audio_list);

        srl.setOnRefreshListener(this);
        srl.post(new Runnable() {
            @Override
            public void run() {
                if(srl!=null){
                    srl.setRefreshing(true);
                }
                arrayList.clear();
                loadDataFromDB();
                srl.setRefreshing(false);
            }
        });




    }

    private void loadDataFromDB() {
        mDbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+
                DatabaseContract.AudioBase.TABLE_NAME, null);
        if (c.getCount()>-1) {
            while (c.moveToNext()){
                mAudioData = new AudioData();
                mAudioData.setName(c.getString(c.getColumnIndex(DatabaseContract.AudioBase.COLUMN_NAME_FILE)));
                mAudioData.setOutputname(c.getString(c.getColumnIndex(DatabaseContract.AudioBase.COLUMN_NAME_OUTPUTFILE)));
                mAudioData.setOncreated(c.getString(c.getColumnIndex(DatabaseContract.AudioBase.COLUMN_NAME_ONCREATED)));
                arrayList.add(mAudioData);
            }
        }
        c.close();
        for(int i=0;i<arrayList.size();i++){
            Log.d("Name",arrayList.get(i).getName());
        Log.d("OutputName",arrayList.get(i).getOutputname());
        Log.d("Oncreated",arrayList.get(i).getOncreated());
    }
        Log.d("Database Row",""+c.getCount());
        adapter = new ListAudioAdapter(arrayList,getApplicationContext());
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {
        arrayList.clear();
        loadDataFromDB();
        srl.setRefreshing(false);
    }


}
