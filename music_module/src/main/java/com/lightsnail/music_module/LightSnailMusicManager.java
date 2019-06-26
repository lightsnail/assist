package com.lightsnail.music_module;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.lightsnail.music.song.Song;

import java.io.IOException;

public class LightSnailMusicManager {

    private static LightSnailMusicManager sLightSnailMusicManager;

    private MusicModule musicModule;
   // private HD mhd;
    private Handler mHandler = new Handler();
    public LightSnailMusicManager( Context context){
       // mhd = new HD();
        musicModule = new MusicModule(context);
    }
    public static LightSnailMusicManager getInstance(Context context ){
        if(sLightSnailMusicManager == null){
            sLightSnailMusicManager = new LightSnailMusicManager( context);
        }
        return sLightSnailMusicManager;
    }

    public void stopMusic() {
        musicModule.stop();
    }
    public void playMusic(String song) {
        Utils.downloadJson("http://www.xiaoxina.cn/api.php?s="+song+"&num=1",jsonDownloadListener);
    }
    Utils.JsonDownloadListener jsonDownloadListener= new Utils.JsonDownloadListener() {
        @Override
        public void onFailed(IOException e) {
            Log.e("LightSnail","JsonDownloadListener = error ");
        }
        @Override
        public void onSuccess(String json) {
            Log.e("LightSnail","json = "+json);
            Utils.parseJson(json,jsonParseListener);
        }
    };
    Utils.JsonParseListener jsonParseListener = new Utils.JsonParseListener() {
        @Override
        public void onSuccess(Song song) {

            Log.e("LightSnail","song.mName = "+song.mName);
            Log.e("LightSnail","song.mUrl = "+song.mUrl);
            Utils.getRedirectUrl(song.mUrl,songUrlRedirectListener);
        }

        @Override
        public void onFailed() {

        }
    };
    Utils.SongUrlRedirectListener songUrlRedirectListener = new Utils.SongUrlRedirectListener() {
        @Override
        public void onFailed(IOException e) {

        }

        @Override
        public void onSuccess(final String redirectUrl) {
            Log.e("LightSnail","new url = "+redirectUrl);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
//                    mhd.MusicPlay(redirectUrl);
                    musicModule.MusicPlay(redirectUrl);
                }
            });
        }
    };

}
