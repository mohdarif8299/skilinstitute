package com.learning.skilclasses.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.learning.skilclasses.R;
import com.learning.skilclasses.models.Video;
import com.learning.skilclasses.models.urlLists;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class SingleVideoActivity extends AppCompatActivity {

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayerStandard.releaseAllVideos();
    }

    Video video;
    @BindView(R.id.video_title)
    TextView title;
    @BindView(R.id.video_subject)
    TextView videoSuject;
    Video videoList;
    Context context;
    JCVideoPlayerStandard jcVideoPlayerStandard;
    String[] country = {"India", "USA", "China", "Japan", "Other"};
    Spinner spinner;
    List<urlLists> al = new ArrayList<urlLists>();
    //CustomVideoPlayer customVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Watch Videos");
        videoList = (Video) getIntent().getSerializableExtra("video");
        Log.d("VIDEO", videoList.getUrlList() + "");
        jcVideoPlayerStandard = findViewById(R.id.videoplayer);
        spinner = findViewById(R.id.spinner);

        for (Map.Entry<String, String> m : videoList.getUrlList().entrySet()) {
            String key = m.getKey();
            String value = m.getValue();
            al.add(new urlLists(key, value));
        }
        ArrayAdapter<urlLists> aa = new ArrayAdapter<urlLists>(this, android.R.layout.simple_spinner_item, al);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        //customVideoPlayer=(view).findViewById(R.id.videoplayer);
        try {
            title.setText(videoList.getVdesp());
            String sub = videoList.getVsubject();
            videoSuject.setText(sub.substring(0, 1).toUpperCase() + sub.substring(1));
            //Toast.makeText(context,al.get(0).getValue(),Toast.LENGTH_LONG).show();
            jcVideoPlayerStandard.setUp(al.get(0).getValue(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
            Picasso.get().load(videoList.getThumbnailimg()).into(jcVideoPlayerStandard.thumbImageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                urlLists s = (urlLists) adapterView.getItemAtPosition(i);
                jcVideoPlayerStandard.setUp(s.getValue(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
