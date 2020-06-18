package com.example.musicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Create By XYG
 * 类BaseActivity：基础活动，作为所有活动的父类
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
