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
 * Created by XYG
 * 类PlayPageFragment：YGMusic的音乐详情播放界面
 */

public class PlayPageFragment extends Fragment implements View.OnClickListener{

    ImageView back,P_album,P_last,P_play,P_next;
    TextView P_singer,P_song;
    SongBean currentbean,newcurrentbean;
    RoundedBitmapDrawable playalbum;
    ProgressBar progressbar;
    MediaPlayer media;
    RelativeLayout mainLayout;

    //创建一个线程对播放器的进度进行读取，实现进度条功能
    Handler handler = new Handler();
    Runnable runable = new Runnable() {
        @Override
        public void run() {
            progressbar.setProgress(media.getCurrentPosition());
            handler.postDelayed(runable,100);
        }
    };

    /**
     * 碎片的生命周期函数
     * @param activity
     */
    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        currentbean =((MainActivity)activity).currentsong;
    //    playalbum =  ((MainActivity)activity).getalbum(currentbean);
        media =  ((MainActivity)activity).mediaPlayer;
        mainLayout = ((MainActivity)activity).mainlayout;



    }

    /**
     * 为碎片创建视图的碎片生命周期函数
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
      View view = inflater.inflate(R.layout.play_details_page,container,false);
        return view;
    }

    /**
     * 碎片视图加载完成后的生命周期函数
     * @param savedInstanceState
     */
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

    /**
     * 设置播放详情界面的播放信息
     * @param currentbean
     */
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

    /**
     * 相应播放详情页面的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        ((MainActivity)getActivity()).onClick(v);
        newcurrentbean = ((MainActivity)getActivity()).currentsong;
        setMessage(newcurrentbean);
    }

}

