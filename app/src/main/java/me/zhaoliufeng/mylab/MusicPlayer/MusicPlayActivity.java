package me.zhaoliufeng.mylab.MusicPlayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import me.zhaoliufeng.customviews.RingProgressBar;
import me.zhaoliufeng.mylab.MusicPlayer.adapter.MusicAdapter;
import me.zhaoliufeng.mylab.MusicPlayer.bean.Song;
import me.zhaoliufeng.mylab.R;

public class MusicPlayActivity extends AppCompatActivity {

    private static final int COUNT_TIME = 0;

    private RecyclerView musicList;
    private TextView tvTime;

    private RingProgressBar progressBar;
    private MusicAdapter musicAdapter;
    private Button mBtnPause;
    //音乐列表数组
    private List<Song> musicDatas;
    private PlayMusicService musicService;
    private PlayConnection conn;
    private String[] modeStrs = {"循环", "单曲", "随机"};
    private int modeIndex = 0; //当前播放模式 循环

    PlayMusicService.IMusicPlayListener musicPlayListener = new PlayMusicService.IMusicPlayListener(){
        @Override
        public void MusicPlaying(int currPosition,int dur) {
            super.MusicPlaying(currPosition, dur);
            progressBar.setMax(dur);
            progressBar.setProgress(currPosition);
            Message msg = new Message();
            msg.arg1 = currPosition;
            msg.arg2 = dur;
            mHandler.sendMessage(msg);
            Log.i("MUSIC", currPosition + " : " + dur);
        }
    };

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvTime.setText(String.format("%02d:%02d", msg.arg1/1000/60, msg.arg1/1000%60) + "/" + String.format("%02d:%02d", msg.arg2/1000/60, msg.arg2/1000%60) );
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_paly);
        tvTime = (TextView) findViewById(R.id.tv_time);
        progressBar = (RingProgressBar) findViewById(R.id.progressBar);
        musicList = (RecyclerView) findViewById(R.id.music_list);
        mBtnPause = (Button) findViewById(R.id.btn_pause);

        if(musicService == null){
            Intent musicIntent = new Intent(this, PlayMusicService.class);
            this.startService(musicIntent);
            conn = new PlayConnection();
            this.bindService(musicIntent, conn, BIND_AUTO_CREATE);
        }
    }



    public void pauseClick(View view) {
        Button btn = (Button) view;
        if (btn.getText().toString().equals("播放")){
            btn.setText("暂停");
            musicService.start();
        }else {
            btn.setText("播放");
            musicService.pause();
        }
    }

    //上一首
    public void lastClick(View view){
        musicAdapter.itemChange(musicService.lastMusic());
        musicAdapter.notifyDataSetChanged();
        mBtnPause.setText("暂停");
    }

    //下一首
    public void nextClick(View view) {
        musicAdapter.itemChange(musicService.nextMusic());
        musicAdapter.notifyDataSetChanged();
        mBtnPause.setText("暂停");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(conn);
    }

    public void modeClick(View view) {
        Button btn = (Button) view;
        btn.setText(modeStrs[modeIndex = modeIndex < 2 ? ++modeIndex : 0]);
        switch (modeIndex){
            case 0:
                musicService.setPlayMode(PlayMusicService.MODE.LIST_LOOP);
                break;
            case 1:
                musicService.setPlayMode(PlayMusicService.MODE.SINGLE_LOOP);
                break;
            case 2:
                musicService.setPlayMode(PlayMusicService.MODE.RANDOM_LOOP);
                break;
        }
    }

    //服务连接回调
    private class PlayConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接之后开始查找音乐
            musicService = ((PlayMusicService.PlayBinder) service).getService();
            musicService.setMusicPlayListener(MusicPlayActivity.this.musicPlayListener);

            musicDatas = musicService.getMusicData();
            Log.i("MUSIC", "READY  TO  LOAD MUSIC");
            musicAdapter = new MusicAdapter(getBaseContext(), musicDatas);
            musicList.setLayoutManager(new LinearLayoutManager (MusicPlayActivity.this, LinearLayoutManager.VERTICAL, false));
            musicList.addItemDecoration (new DividerItemDecoration(MusicPlayActivity.this, DividerItemDecoration.VERTICAL));
            musicList.setAdapter(musicAdapter);

            musicAdapter.setItemSelectListener(new MusicAdapter.ItemSelectListener() {
                @Override
                public void itemSelect(int index) {
                    Log.i("MUSIC", index + "");
                    musicService.selectMusic(index);
                    musicAdapter.itemChange(index);
                    musicAdapter.notifyDataSetChanged();
                    //修改按钮显示为 暂停
                    mBtnPause.setText("暂停");
                }
            });
            musicService.initMediaData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
