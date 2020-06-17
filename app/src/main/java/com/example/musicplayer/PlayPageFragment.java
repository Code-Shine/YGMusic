package com.example.musicplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by 国 on 2020/6/17.
 */

public class PlayPageFragment extends Fragment {

    ImageView back,P_album;
    TextView P_singer,P_song;
    SongBean currentbean;
    RoundedBitmapDrawable playalbum;
    ProgressBar progressbar;
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        P_singer = (TextView)getView().findViewById(R.id.Top_singer);
        P_song = (TextView) getView().findViewById(R.id.Top_song_name);
        P_album = (ImageView)getView().findViewById(R.id.play_album);
        progressbar = (ProgressBar)getView().findViewById(R.id.progress_bar);

        P_song.setText(currentbean.getSong());
        P_singer.setText(currentbean.getSinger());
        P_album.setImageDrawable(playalbum);




    }

    @Override
    public void onAttach(Context activity){
        super.onAttach(activity);
        currentbean =((MainActivity)activity).currentsong;
        playalbum =  ((MainActivity)activity).getalbum(currentbean);

    }


}
