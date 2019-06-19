package com.lightsnail.music_module;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

public class MusicModule    {

    private final Context mContext;
    private MediaPlayer mediaPlayer;
    public   MusicModule(Context context){
        this.mContext = context;
        mediaPlayer = new MediaPlayer();
    }
    public  void MusicChange(final String musicurl) {
            Log.e("LightSnail",musicurl);

        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(mContext,Uri.parse(musicurl) );
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void start() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
    public void pause(){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    protected void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


}
