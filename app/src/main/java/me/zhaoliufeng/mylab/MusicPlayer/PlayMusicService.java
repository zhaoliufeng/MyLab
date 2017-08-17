package me.zhaoliufeng.mylab.MusicPlayer;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.zhaoliufeng.mylab.MusicPlayer.bean.Song;

public class PlayMusicService extends Service {

    private PlayBinder binder;
    private MediaPlayer mediaPlayer;
    //当前播放音乐的下标 默认0
    private int mCurrIndex = 0;
    private List<Song> musicDatas;
    private IMusicPlayListener musicPlayListener = new IMusicPlayListener();
    private MODE mMode = MODE.LIST_LOOP;
    private List<Integer> randomIndexList = new ArrayList<>();
    //播放模式
    enum MODE{
        LIST_LOOP,//列表循环
        SINGLE_LOOP,//单曲循环
        RANDOM_LOOP;//随机循环
    }

    public PlayMusicService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new PlayBinder();
        mediaPlayer = new MediaPlayer();
        initListener();
    }

    private void initListener() {
        mediaPlayer.setLooping(false);
        //播放完继续播放下一首
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                switch (mMode){
                    case LIST_LOOP:
                        mediaPlayer.setLooping(false);
                        nextMusic();
                        break;
                    case SINGLE_LOOP:
                        mediaPlayer.setLooping(true);
                        break;
                    case RANDOM_LOOP:
                        mediaPlayer.setLooping(false);
                        randomNextMusic();
                        break;
                }

            }
        });
    }

    private void randomNextMusic() {
        //判断list是否已经大于列表数量
        if (randomIndexList.size() >= musicDatas.size()) {
            randomIndexList.clear();
            return;
        }
        //生成随机数 判断生成的随机数是否在list中 是则重新生成
        int randomIndex;
        do {
            randomIndex = (int) (Math.random() * musicDatas.size());
        }
        while (randomIndexList.contains(randomIndex));
        mCurrIndex = randomIndex;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getMusicData().get(mCurrIndex).getPath());
            prepareMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayListener.MusicChange(mCurrIndex, mediaPlayer.getDuration());

        randomIndexList.add(randomIndex);
    }

    public void initMediaData() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicDatas.get(mCurrIndex).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            startTimeCount(mediaPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startTimeCount(final MediaPlayer mediaPlayer) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()){
                    musicPlayListener.MusicPlaying(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                }
            }
        }, 0, 1000);
    }

    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public List<Song> getMusicData() {
        List<Song> list = new ArrayList<>();
        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
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

                    if (song.getMusicName().contains(".mp3")) {
                        song.setMusicName(song.getMusicName().replace(".mp3", "").trim());
                    }
                    list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }
        musicDatas = list;
        return list;
    }

    //选择音乐
    public void selectMusic(int index) {
        mCurrIndex = index;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicDatas.get(mCurrIndex).getPath());
            prepareMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //上一首
    public int lastMusic() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getMusicData().get(mCurrIndex = mCurrIndex == 0 ? musicDatas.size() - 1 : --mCurrIndex).getPath());
            prepareMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayListener.MusicChange(mCurrIndex, mediaPlayer.getDuration());
        return mCurrIndex;
    }

    //下一首
    public int nextMusic() {
        if (mMode == MODE.RANDOM_LOOP){
            randomNextMusic();
        }else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getMusicData().get(mCurrIndex = mCurrIndex == musicDatas.size() - 1 ? 0 : ++mCurrIndex).getPath());
                prepareMediaPlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            musicPlayListener.MusicChange(mCurrIndex, mediaPlayer.getDuration());
        }
        return mCurrIndex;
    }

    private void prepareMediaPlayer() throws IOException {
        mediaPlayer.prepare();
        mediaPlayer.start();
        startTimeCount(mediaPlayer);
    }

    //暂停
    public void pause() {
        mediaPlayer.pause();
        musicPlayListener.MusicPaused();
    }

    //继续 开始播放
    public void start() {
        mediaPlayer.start();
        musicPlayListener.MusicStart();
    }

    //设置播放模式
    public void setPlayMode(MODE mode) {
        mMode = mode;
        if (mMode == MODE.RANDOM_LOOP){
            randomIndexList.clear();
            randomIndexList.add(mCurrIndex);
        }
    }

    public class PlayBinder extends Binder {
        public PlayMusicService getService() {
            return PlayMusicService.this;
        }
    }

    //音乐播放监听
    public void setMusicPlayListener(IMusicPlayListener musicPlayListener) {
        if (musicPlayListener == null) {
            this.musicPlayListener = new IMusicPlayListener();
        } else {
            this.musicPlayListener = musicPlayListener;
        }
    }

    public static class IMusicPlayListener {

        public void NewPlay(Song songData) {
        }

        public void MusicStoped() {
        }

        public void MusicPaused() {
        }

        public void MusicResumed() {
        }

        public void MusicStart() {

        }

        public void MusicPlaying(int currPosition, int dur) {
        }

        public void MusicChange(int currIndex, int dur) {

        }

        public void PlayModedChanged(int oldMode, int newMode) {
        }


        public void MusicListChanged() {
        }
    }
}
