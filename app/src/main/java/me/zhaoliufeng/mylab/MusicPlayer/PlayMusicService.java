package me.zhaoliufeng.mylab.MusicPlayer;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
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

import me.zhaoliufeng.customviews.Spectrograph;
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
    private Visualizer visualizer;
    public Spectrograph spectrograph;
    //播放模式
    enum MODE {
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

        visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        visualizer.setDataCaptureListener(captureListener,
                Visualizer.getMaxCaptureRate(), false, true);
        visualizer.setEnabled(true);
    }

    private void initListener() {
        mediaPlayer.setLooping(false);
        //播放完继续播放下一首
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                switch (mMode) {
                    case LIST_LOOP:
                        nextMusic();
                        break;
                    case SINGLE_LOOP:
                        singleMusic();
                        break;
                    case RANDOM_LOOP:
                        randomNextMusic();
                        break;
                }

            }
        });
    }

    /**
     * 随机下一曲
     */
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
            mediaPlayer.setDataSource(musicDatas.get(mCurrIndex).getPath());
            prepareMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayListener.MusicChange(mCurrIndex, mediaPlayer.getDuration());

        randomIndexList.add(randomIndex);
    }

    private void singleMusic(){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicDatas.get(mCurrIndex).getPath());
            prepareMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initMediaData() {
        try {
            if (musicDatas.size() > 0) {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicDatas.get(mCurrIndex).getPath());
                prepareMediaPlayer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startTimeCount(final MediaPlayer mediaPlayer) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
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
                if (song.getSize() > 1000 * 100) {
                    // 按 歌曲名 - 歌手 格式分割标题，分离出歌曲名和歌手
                    // 如果歌曲名中包含 “-” 则是复合歌手名
                    if (song.getArtist().equals("<unknown>")) {
                        //如果获取不到歌手名 就去拆分歌曲名
                        if (song.getMusicName().contains("-")) {
                            //按 - 标记拆分成两串字符串
                            String[] str = song.getMusicName().split("-");
                            song.setArtist(str[0].trim());
                            song.setMusicName(str[1].trim());
                        }else if (song.getMusicName().contains("_")){
                            String[] str = song.getMusicName().split("_");
                            for (int i = 0; i < str.length; i++){
                                if (str[i].equals(song.getArtist())){
                                    if (i == 0){
                                        song.setMusicName(str[1]);
                                    }else if (i == 1){
                                        song.setMusicName(str[0]);
                                    }
                                }
                            }
                        }
                    } else {
                        if (song.getMusicName().contains("-")) {
                            String[] str = song.getMusicName().split("-");
                            if (str[0].trim().equals(song.getArtist())) {
                                song.setMusicName(str[1].trim());
                            } else {
                                song.setMusicName(str[0].trim());
                            }
                        }else if (song.getMusicName().contains("_")){
                            String[] str = song.getMusicName().split("_");
                            for (int i = 0; i < str.length; i++){
                                if (str[i].equals(song.getArtist())){
                                    if (i == 0){
                                        song.setMusicName(str[1]);
                                    }else if (i == 1){
                                        song.setMusicName(str[0]);
                                    }
                                }
                            }
                        }
                    }

                    //除去末尾的文件格式后缀
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
        if (mCurrIndex == index) return;
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
        if (mMode == MODE.RANDOM_LOOP) {
            randomNextMusic();
        } else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicDatas.get(mCurrIndex = mCurrIndex == 0 ? musicDatas.size() - 1 : --mCurrIndex).getPath());
                prepareMediaPlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            musicPlayListener.MusicChange(mCurrIndex, mediaPlayer.getDuration());
        }
        return mCurrIndex;
    }

    //下一首
    public int nextMusic() {
        if (mMode == MODE.RANDOM_LOOP) {
            randomNextMusic();
        } else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicDatas.get(mCurrIndex = mCurrIndex == musicDatas.size() - 1 ? 0 : ++mCurrIndex).getPath());
                prepareMediaPlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            musicPlayListener.MusicChange(mCurrIndex, mediaPlayer.getDuration());
        }
        return mCurrIndex;
    }

    private void prepareMediaPlayer() throws IOException {
        if (musicDatas.size() > 0){
            mediaPlayer.prepare();
            mediaPlayer.start();
            musicPlayListener.NewPlay(musicDatas.get(mCurrIndex).getMusicName(), musicDatas.get(mCurrIndex).getArtist());
            startTimeCount(mediaPlayer);
        }
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
        if (mMode == MODE.RANDOM_LOOP) {
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

        public void NewPlay(String musicName, String singer) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    public static final int MIN_SEND_GAP = 80;
    Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener() {
        Util.Gap gapController = new Util.Gap(MIN_SEND_GAP);

        private float fftPoints[] = null;
        @Override
        public void onWaveFormDataCapture(Visualizer arg0, byte[] data, int arg2) {

        }

        @Override
        public void onFftDataCapture(Visualizer arg0, byte[] data, int arg2) {
            if (spectrograph != null){
                spectrograph.setData(data);
            }
        }
    };
}
