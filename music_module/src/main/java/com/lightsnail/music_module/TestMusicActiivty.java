package com.lightsnail.music_module;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.lightsnail.music.song.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestMusicActiivty extends Activity {

    private int index = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LightSnailMusicManager.getInstance(this ).playMusic("明天,你好");

        setContentView(R.layout.test_music);
//        findViewById(R.id.music_change).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                index += 1;
//                index %=list.size();
//                //musicModule.MusicChange(list.get(index));
//                mhd.MusicChange(list.get(index));
//            }
//        });
//
//
//        //list.add("http:\\/\\/music.163.com\\/song\\/media\\/outer\\/url?id=368727.mp3");
//        //list.add("http://www.ytmp3.cn/down/57799.mp3");
//        list.add("http://m10.music.126.net/20190616162218/df8cf795922f0fa0faa97b3244480bb2/ymusic/15cd/d06e/ad12/a77f599a2dfe83ebd9911819afd4b42e.mp3");
//        list.add("http://win.web.ra01.sycdn.kuwo.cn/6e1d13993ff82e9017967b754fb5a5d6/5d05f744/resource/n2/2009/07/24/2707183870.mp3");
    }
}
