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

public class PlayPageFragment extends Fragment implements View.OnClickListener{

    ImageView back,P_album,P_last,P_play,P_next;
    TextView P_singer,P_song;
    SongBean currentbean,newcurrentbean;
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
    //    playalbum =  ((MainActivity)activity).getalbum(currentbean);
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

        P_last = (ImageView)getView().findViewById(R.id.button_last);
        P_last.setOnClickListener(this);

        P_play = (ImageView)getView().findViewById(R.id.button_play);
        P_play.setOnClickListener(this);

        P_next = (ImageView)getView().findViewById(R.id.button_next);
        P_next.setOnClickListener(this);

//        progressbar.setMax(Integer.parseInt(currentbean.getDuration()));
        //设置播放界面的各种信息
        setMessage(currentbean);


        handler.post(runable);

        //返回键的点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消线程
                handler.removeCallbacks(runable);
                //显示main页面的布局
//                mainLayout.setVisibility(View.VISIBLE);
                getActivity().onBackPressed();

            }
        });


    }

    public void setMessage(SongBean currentbean){
        P_song.setText(currentbean.getSong());
        P_singer.setText(currentbean.getSinger());
        playalbum =  ((MainActivity)getActivity()).getalbum(currentbean);
        P_album.setImageDrawable(playalbum);
        progressbar.setMax(media.getDuration());

        if (media.isPlaying()) {
            P_play.setImageResource(R.drawable.activity_play_stop);
        } else {
            P_play.setImageResource(R.drawable.activity_play_start);
        }
    }

    @Override
    public void onClick(View v) { 

        ((MainActivity)getActivity()).onClick(v);

        newcurrentbean = ((MainActivity)getActivity()).currentsong;
        setMessage(newcurrentbean);
    }



}

