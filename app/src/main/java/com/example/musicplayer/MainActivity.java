package com.example.musicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Create By XYG
 * 类MainActivity：YGMusic的主要音乐列表界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private List<SongBean> songBeanList = new ArrayList<>();

    ImageView B_next, B_play, B_last,B_ablum;
    TextView B_singer, B_song_name;
    ListView listView;
    SongAdapter adapter;
    MediaPlayer mediaPlayer;
    SongBean currentsong;
    PlayPageFragment playpageFragment;

    RelativeLayout mainlayout;

    //CurrentStopReason是用来判断当前音乐停止的原因变量
    int CurrentStopReason=0; //第一次进入没有点击音乐
    int Changesong = 1;//切换音乐导致的
    int Pausesong = 2;//暂停按钮导致的
    int Deletesong = 3;//删除音乐导致的

    //用一个变量记录ListView中的点击的位置，用于切换音乐
    int CurrentMusicPosition=0;

    /**
     * onCreate方法
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mediaPlayer = new MediaPlayer();
        mainlayout = (RelativeLayout) findViewById(R.id.main_layout);
        //初始化底部按钮与视图的控件
        initView();

        //初始化歌曲的测试数据，将数据放入适配器中
        initSongs();

        //加载位于本地SD卡的数据文件
        loadMusic();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    /**********************    ToolBar     ***********************/
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载toolbar.xml
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete:
//                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("确定删除当前正在播放的音乐吗？");
                dialog.setMessage("请确认是否删除");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        //设置点击OK的事件，删除音乐
                    deleteCurrentMusic();
                    }});
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        //设置点击Cancel的事件
                    }});
                dialog.show();
            default:
        }
        return true;
    }

    /**
     * 删除音乐
     */
    private void deleteCurrentMusic() {
        //停止播放当前音乐,并说明停止的原因
        mediaPlayer.stop();
        CurrentStopReason = Deletesong;
        //在适配器中移除当前播放的对象，listview会自动更新
        adapter.remove(songBeanList.get(CurrentMusicPosition));
    }
    /**********************   END ToolBar     ***********************/



    /**********************    动态申请权限并读取数据     ***********************/
    /**
     *   功能 加载本地音乐的信息到SongBean的集合中
     */
    private void loadMusic() {
        //运行时的SD卡权限申请
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){//如果权限没有申请
            //则是申请权限
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE },1);
        }else {
            readContacts();
        }
    }

    /**
     * 功能 找到SD卡中的歌曲信息，将其封装为一个Bean添加到songBeanList中
     */
    private void readContacts() {

//        1.获取内容解析者对象ContextResolver
        ContentResolver resolver = getContentResolver();

//        2.获取本地音乐存储的地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = null;
        try{
//        3.开始查询地址
            cursor = resolver.query(uri, null, null, null, null);

//        4.遍历Cursor
            while (cursor.moveToNext()) {
                //获取歌曲路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));


                //根据路径判断是否为MP3,是MP3时才将其放入列表中
                if (path.endsWith(".mp3")) {


                    //获取歌曲信息
                    String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String ablum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

                    //获取歌曲时长，因为是long，所以需要转换
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    SimpleDateFormat simpdf = new SimpleDateFormat("mm:ss");
                    String time = simpdf.format(new Date(duration));

                    //获取album_id,专辑图片的ID
                    int album_id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
//                    Log.d("album", String.valueOf(album_id));
//                    Toast.makeText(MainActivity.this,String.valueOf(album_id),Toast.LENGTH_LONG).show();

                    //将一行当中的数据封装到对象当中
                    SongBean bean = new SongBean(song, singer, ablum, time,path,album_id);
                    songBeanList.add(bean);
              }
            }
            //通知数据进行更新
            adapter.notifyDataSetChanged();
        }catch (Exception e ){
            e.printStackTrace();

        }finally {
            if(cursor == null){
                cursor.close();
            }
        }
    }


    /**
     * 功能 动态申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED){
                    readContacts();
                }else{
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    /**********************    END 动态申请权限并读取数据     ***********************/




    /**********************    初始化     ***********************/
    /**
     * 功能 按钮初始化
     */
    private void initView() {
        B_next = (ImageView) findViewById(R.id.button_next);
        B_last = (ImageView) findViewById(R.id.button_last);
        B_play = (ImageView) findViewById(R.id.button_play);//play与pause共用一个按钮，到时替换图片
        B_ablum = (ImageView)findViewById(R.id.song_icon) ;


        B_next.setOnClickListener(this);
        B_last.setOnClickListener(this);
        B_play.setOnClickListener(this);
        B_ablum.setOnClickListener(this);

        //底部歌曲与歌手信息初始化
        B_song_name = (TextView) findViewById(R.id.bottom_song_name);
        B_singer = (TextView) findViewById(R.id.bottom_singer);
    }

    /**
     * 功能 1.将songBeanList中的对象数据放入适配器中。
     *      2.将适配器与ListView进行关联
     *      3.为ListView中的每一个item进行事件监听
     *      4.每当点击item播放相应的歌曲
     *
     */
    private void initSongs() {
            //测试布局数据
//            SongBean song1 = new SongBean("盔甲", "王贰浪", "盔甲", "4:22");
//            songBeanList.add(song1);
//            SongBean song2 = new SongBean("像鱼", "王贰浪", "盔甲", "4:22");
//            songBeanList.add(song2);
//            SongBean song3 = new SongBean("超人", "王贰浪", "盔甲", "4:22");
//            songBeanList.add(song3);
//            SongBean song4 = new SongBean("爱你", "王贰浪", "盔甲", "4:22");
//            songBeanList.add(song4);

//        创建适配器
         adapter = new SongAdapter(MainActivity.this,R.layout.song_item,songBeanList);
//        实例化ListView
         listView = (ListView) findViewById(R.id.list_view);
//        关联适配器与ListView
        listView.setAdapter(adapter);

//        为ListView中的每一项注册监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //将此时点击的音乐位置赋给 CurrentMusicPosition变量
                CurrentMusicPosition = position;

                currentsong = songBeanList.get(position);
                //播放此时点击的事件
                PlayBeanMusic(currentsong);

            }
        });

    }
    /**********************    END 初始化     ***********************/





    /**********************    音乐的播放等操作     ***********************/
       /**
     * 功能 1.播放由点击ListView的item所指定的歌曲
     *      2.根据获得的songBean对象设置控制栏的歌手与歌名
     *      3.加载专辑图片
     *
     * @param song
     */
    //播放指定Bean的音乐
    public void PlayBeanMusic(SongBean song) {
        //根据点击事件设置底部的歌手与歌曲名,还有专辑图片
        B_singer.setText(song.getSinger());
        B_song_name.setText(song.getSong());

        //获取专辑图片并设置图片
        RoundedBitmapDrawable ralbum = getalbum(currentsong);
        B_ablum.setImageDrawable(ralbum);

        //停止当前播放的音乐
        StopCurrentMusic();

        //重置多媒体播放器
        mediaPlayer.reset();//删掉原来的地址
        try {
            //设置新的播放路径
            mediaPlayer.setDataSource(song.getPath());
            CurrentStopReason = Changesong;//表明当前音乐是由于切换而暂停的
            PlayMusic();//播放音乐
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能 获取专辑图片并转化为圆形
     * @param song
     * @return 专辑的图片
     */
    public RoundedBitmapDrawable getalbum(SongBean song){
        //设置专辑图片
        // Mediametadataretriever类提供了一个统一的接口取回帧和取回从一个输入媒体文件中的元数据。
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(song.getPath());
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap= BitmapFactory.decodeByteArray(picture,0,picture.length);

        //创建RoundedBitmapDrawable对象，将bitmap转化为RoundedBitmapDrawable
        RoundedBitmapDrawable roundalbum = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundalbum.setCornerRadius(bitmap.getWidth()/2); //设置圆角半径（根据实际需求）
        roundalbum.setAntiAlias(true); //设置反走样

        return roundalbum;
    }


    /**
     * 功能 1.停止当前播放的音乐
     *      2.更改播放按钮图片
     */
    //停止当前播放的音乐
    private void StopCurrentMusic(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            //停止的按钮变为将要播放按钮
            B_play.setImageResource(R.mipmap.icon_play);
        }

    }

    /**
     * 功能 1.根据暂停的原因对播放音乐进行不同的处理
     *      2.更改播放图片按钮
     */
    //播放音乐
    private void  PlayMusic(){
        if(CurrentStopReason == 0){
            Toast.makeText(MainActivity.this,"请点击列表进行播放",Toast.LENGTH_SHORT).show();
        }else if( mediaPlayer!=null&&!mediaPlayer.isPlaying()){ //当有音乐正在mediaPlayer中，且没有音乐正在播放
            //判断停止的原因
            if( CurrentStopReason == Changesong) {

                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(CurrentStopReason == Pausesong){
                //继续播放
                mediaPlayer.start();
            }else if(CurrentStopReason == Deletesong){
                Toast.makeText(MainActivity.this,"当前音乐已删除，请切换其他音乐播放",Toast.LENGTH_SHORT).show();
                return;//避免删除的没有播放音乐，又改变按钮
            }
            //播放按钮变为暂停按钮
            B_play.setImageResource(R.mipmap.icon_pause);
        }
    }

    /**
     * 功能 对当前播放的音乐进行暂停
     */
    //暂停音乐
    private  void PauseCurrentMusic(){
       if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
           mediaPlayer.pause();
           //更换图标
           B_play.setImageResource(R.mipmap.icon_play);
           CurrentStopReason = Pausesong;//表明此时是因为音乐暂停引起的
        }
    }

    /**
     * 当活动销毁时停止播放音乐
     */
    //活动被销毁就不再播放音乐
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.finishAll();
        StopCurrentMusic();

    }
    /**********************    END 音乐的播放等操作     ***********************/


    /**
     * 功能 对下边的控制按钮进行监听，响应不同的点击事件
     * @param v
     */
    //按钮点击事件
    @Override
    public void onClick(View v) {
        switch(v.getId()){
//            下一首点击按钮
            case R.id.button_next:
                if(currentsong == null){
                    currentsong = songBeanList.get(CurrentMusicPosition);
                    PlayBeanMusic(currentsong);
                }else {
                    if (CurrentMusicPosition == songBeanList.size() - 1) {
                        Toast.makeText(MainActivity.this, "已经是最后一首啦！", Toast.LENGTH_SHORT).show();
                    } else {
                        CurrentMusicPosition++;
                        currentsong = songBeanList.get(CurrentMusicPosition);
                        PlayBeanMusic(currentsong);
                    }
                }
                break;
//            上一首点击按钮
            case R.id.button_last:
                if(currentsong == null){
                    Toast.makeText(MainActivity.this, "当前没有音乐在播放", Toast.LENGTH_SHORT).show();
                }else {
                    if (CurrentMusicPosition == 0) {
                        Toast.makeText(MainActivity.this, "已经是第一首啦！", Toast.LENGTH_SHORT).show();
                    } else {
                        CurrentMusicPosition--;
                        currentsong = songBeanList.get(CurrentMusicPosition);
                        PlayBeanMusic(currentsong);
                    }
                }
                break;
//            播放的点击按钮
            case R.id.button_play:
//                Toast.makeText(MainActivity.this,"播放",Toast.LENGTH_SHORT).show();
                if(mediaPlayer.isPlaying()){
                    //暂停音乐
                    PauseCurrentMusic();
                }else{
                    //播放音乐
                    PlayMusic();
                }
                break;

//            点击专辑按钮跳转到详细播放页面
            case R.id.song_icon:
                if (currentsong == null){
                    Toast.makeText(MainActivity.this,"当前没有音乐播放",Toast.LENGTH_SHORT).show();
                }else {
                    //隐藏活动的布局，让碎片不响应按键事件
                    mainlayout.setVisibility(View.INVISIBLE);
//                    Log.d("Main" ,String.valueOf(View.INVISIBLE));
//                    Log.d("Main" ,String.valueOf(View.VISIBLE));
//                    Log.d("Main" ,String.valueOf(mainlayout.getVisibility()));
                    //打开碎片的布局
                    playpageFragment = new PlayPageFragment();
                    goPlayFragment(playpageFragment);
                }
                break;


        }
    }



    /**
     * 功能 跳转播放页面的碎片
     * @param fragment
     */
    private void goPlayFragment(Fragment fragment) {
        FragmentManager fragmentmanager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentmanager.beginTransaction();
        transaction.replace(R.id.out_main_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    /**
     * 让碎片中的自定义返回按钮与系统返回按键一样可以返回
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //让之前消失的活动布局变得可见
        if(mainlayout.getVisibility() == View.INVISIBLE) {
            mainlayout.setVisibility(View.VISIBLE);
        }


    }
}
