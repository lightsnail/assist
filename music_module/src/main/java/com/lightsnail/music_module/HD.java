package com.lightsnail.music_module;

import android.content.Context;

import com.yhd.hdmediaplayer.MediaPlayerHelper;

//https://github.com/yinhaide/HDMediaPlayer
public class HD {
    public HD(){
    }
    public void MusicChange(final String url){

        //MediaPlayerHelper.getInstance().release();
        MediaPlayerHelper.getInstance().play( url);

    }

}
