package me.zhaoliufeng.mylab.MusicPlayer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.zhaoliufeng.customviews.RingProgressBar;
import me.zhaoliufeng.mylab.MusicPlayer.adapter.MusicAdapter;
import me.zhaoliufeng.mylab.MusicPlayer.bean.Song;
import me.zhaoliufeng.mylab.R;

public class MusicPlayActivity extends AppCompatActivity {

    private static final int COUNT_TIME = 0;

    private RecyclerView musicList;
    private MediaPlayer mediaPlayer;

    private TextView tvTime;

    private RingProgressBar progressBar;
    private MusicAdapter musicAdapter;
    private Button mBtnPause;
    private List<Song> datas;
    private int mCurrIndex;
    private Timer timer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_paly);
        tvTime = (TextView) findViewById(R.id.tv_time);
        progressBar = (RingProgressBar) findViewById(R.id.progressBar);
        musicList = (RecyclerView) findViewById(R.id.music_list);
        mBtnPause = (Button) findViewById(R.id.btn_pause);
        mediaPlayer = new MediaPlayer();

        datas = getMusicData(getBaseContext());
        musicAdapter = new MusicAdapter(getBaseContext(), datas);
        musicList.setLayoutManager(new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        musicList.addItemDecoration (new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        musicList.setAdapter(musicAdapter);

        musicAdapter.setItemSelectListener(new MusicAdapter.ItemSelectListener() {
            @Override
            public void itemSelect(int index) {
                mCurrIndex = index;
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(datas.get(index).getPath());
                    prepareMediaPlayer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mediaPlayer.setLooping(false);
        //播放完继续播放下一首
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("MUSIC", "INCOME");
               nextMusic();
            }
        });
        mCurrIndex = 0;
        initMediaData();
    }

    private void initMediaData() {
        try {
            mediaPlayer.setDataSource(datas.get(mCurrIndex).getPath());
            mediaPlayer.prepare();
            progressBar.setMax(mediaPlayer.getDuration());
            startTimeCount(mediaPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public static List<Song> getMusicData(Context context) {
        List<Song> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setMusicName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                if (song.getSize() > 1000 * 800) {
                    // 按 歌曲名 - 歌手 格式分割标题，分离出歌曲名和歌手
                    if (song.getMusicName().contains("-")) {
                        String[] str = song.getMusicName().split("-");
                        song.setArtist(str[0].trim());
                        song.setMusicName(str[1].replace(".mp3", "").trim());
                    }

                    if (song.getMusicName().contains(".mp3")){
                        song.setMusicName(song.getMusicName().replace(".mp3", "").trim());
                    }
                    list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }

        return list;
    }

    public void lastClick(View view) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getMusicData(this).get(mCurrIndex = mCurrIndex == 0 ? datas.size() -1 :  --mCurrIndex).getPath());
            prepareMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicAdapter.itemChange(mCurrIndex);
    }

    public void pauseClick(View view) {
        Button btn = (Button) view;
        if (btn.getText().toString().equals("播放")){
            btn.setText("暂停");
            mediaPlayer.start();
        }else {
            btn.setText("播放");
            mediaPlayer.pause();
        }

    }

    public void nextClick(View view) {
        nextMusic();
    }

    private void nextMusic(){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getMusicData(this).get(mCurrIndex = mCurrIndex == datas.size() - 1 ? 0 :  ++mCurrIndex).getPath());
            prepareMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicAdapter.itemChange(mCurrIndex);
    }

    private void prepareMediaPlayer() throws IOException {
        mediaPlayer.prepare();
        mediaPlayer.start();
        progressBar.setMax(mediaPlayer.getDuration());
        startTimeCount(mediaPlayer);
        mBtnPause.setText("暂停");
    }

    public void startTimeCount(final MediaPlayer mediaPlayer){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                final Message msg = new Message();
                msg.what = COUNT_TIME;
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        msg.arg1 = mediaPlayer.getCurrentPosition();
                        msg.arg2 = mediaPlayer.getDuration();
                        handler.sendMessage(msg);
                    }
                });

            }
        }, 0, 1000);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case COUNT_TIME:
                    tvTime.setText(String.format("%02d:%02d", msg.arg1/1000/60, msg.arg1/1000%60) + "/" + String.format("%02d:%02d", msg.arg2/1000/60, msg.arg2/1000%60) );
                    progressBar.setProgress(msg.arg1);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
