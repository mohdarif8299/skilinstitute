package com.learning.skilclasses.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class video_player extends Fragment {
    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayerStandard.releaseAllVideos();
    }

    private static video_player instance;

    public static video_player getInstance() {
        if (instance == null)
            instance = new video_player();

        return instance;
    }

    @BindView(R.id.video_title)
    TextView title;
    @BindView(R.id.video_subject)
    TextView videoSuject;
    Video videoList;
    Context context;
    JCVideoPlayerStandard jcVideoPlayerStandard;
    String[] country = { "India", "USA", "China", "Japan", "Other"};
    Spinner spinner;
    List<urlLists> al=new ArrayList<urlLists>();
    //CustomVideoPlayer customVideoPlayer;



    public video_player() {
    }
    public video_player(Video videoList,Context context) {
        this.context=context;
        this.videoList=videoList;
    }

    /*@Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(context, "landscape", Toast.LENGTH_SHORT).show();
            Log.d("change121",newConfig.orientation+"s");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(context, "portrait", Toast.LENGTH_SHORT).show();
        }
    }*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this, view);
        jcVideoPlayerStandard=(view).findViewById(R.id.videoplayer);
        spinner=(view).findViewById(R.id.spinner);

        for (Map.Entry<String,String> m:videoList.getUrlList().entrySet() ){
            String key=m.getKey();
            String value=m.getValue();
            al.add(new urlLists(key,value));
        }
        ArrayAdapter<urlLists> aa = new ArrayAdapter<urlLists>(context,android.R.layout.simple_spinner_item,al);
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

        }catch (Exception e){
            e.printStackTrace();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                urlLists s=(urlLists) adapterView.getItemAtPosition(i);
                jcVideoPlayerStandard.setUp(s.getValue(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });








        return view;
    }
}
