package com.example.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by 国 on 2020/6/17.
 */

public class PlayPageFragment extends Fragment {

    ImageView back,P_album,P_play,P_last,P_next;
    TextView P_singer,P_song;
    SongBean currentbean;
    RoundedBitmapDrawable playalbum;
    ProgressBar progressbar;
    MediaPlayer media;
    RelativeLayout mainLayout;

    Handler handler = new Handler();
    Runnable runable = new Runnable() {
        @Override
        public void run() {
            progressbar.setProgress(media.getCurrentPosition());
            handler.postDelayed(runable,100);
        }
    };


    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        currentbean =((MainActivity)activity).currentsong;
        playalbum =  ((MainActivity)activity).getalbum(currentbean);
        media =  ((MainActivity)activity).mediaPlayer;
        mainLayout = ((MainActivity)activity).mainlayout;



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
      View view = inflater.inflate(R.layout.play_details_page,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        back = (ImageView) getView().findViewById(R.id.back_from_page);
        P_singer = (TextView)getView().findViewById(R.id.Top_singer);
        P_song = (TextView) getView().findViewById(R.id.Top_song_name);
        P_album = (ImageView)getView().findViewById(R.id.play_album);

        progressbar = (ProgressBar)getView().findViewById(R.id.progress_bar);

//        progressbar.setMax(Integer.parseInt(currentbean.getDuration()));
        progressbar.setMax(media.getDuration());
        P_song.setText(currentbean.getSong());
        P_singer.setText(currentbean.getSinger());
        P_album.setImageDrawable(playalbum);

        handler.post(runable);

        //返回键的点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消线程
                handler.removeCallbacks(runable);
                //显示main页面的布局
                mainLayout.setVisibility(View.VISIBLE);
                getActivity().onBackPressed();

            }
        });
        









    }




}
