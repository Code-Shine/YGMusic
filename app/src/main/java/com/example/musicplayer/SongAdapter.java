package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by XYG
 * 类SongAdapter：YGMusic的音乐列表的详情界面
 */

public class SongAdapter extends ArrayAdapter<SongBean>{

    private int resourceId;

    //构造方法来传入数据
    //重写了构造函数,变成了带参数的
    public  SongAdapter(Context context, int textViewResourceId, List<SongBean> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;//子项的ID
    }

    //重写了getView,获得每一子项的布局
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongBean song = getItem(position);//获取当前项的实例

        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        //获取song_item布局中每一个实例
        TextView songname = (TextView) view.findViewById(R.id.item_song_name);
        TextView singer = (TextView) view.findViewById(R.id.item_singer);
        TextView album = (TextView) view.findViewById(R.id.item_album);
        TextView duration = (TextView) view.findViewById(R.id.item_duration);

        //子项布局中的实例获取SongBean中的具体值
        songname.setText(song.getSong());
        singer.setText(song.getSinger());
        album.setText(song.getAlbum());
        duration.setText(song.getDuration());

        //返回获得了具体值的view
        return view;
    }

}
