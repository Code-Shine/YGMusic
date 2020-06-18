package com.example.musicplayer;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By XYG
 * 类ActivityCollector：活动管理器
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    /**
     * 添加活动到栈中，以便进行管理
     * @param activity
     */
    public static void addActivity(Activity activity){
        activities.add(activity);

    }

    /**
     * 将活动从管理栈中移除
     * @param activity
     */
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    /**
     * 结束管理栈中的所有活动
     */
    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }


}
