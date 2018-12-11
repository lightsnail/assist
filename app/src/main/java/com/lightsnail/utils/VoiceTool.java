package com.lightsnail.utils;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.lightsnail.weatherclock.R;

public class VoiceTool {
	private HashMap<String, Integer>	soundMap	= new HashMap<String, Integer>();
	private SoundPool					soundPool;
	private static VoiceTool			mVoiceTool;

	private VoiceTool(final Context context) {

		soundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 100);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// // TODO Auto-generated method stub
				// soundMap.put("101", soundPool.load(context, R.raw.voice101,
				// 1));
				// soundMap.put("102", soundPool.load(context, R.raw.voice102,
				// 1));
				// soundMap.put("103", soundPool.load(context, R.raw.voice103,
				// 1));
				// soundMap.put("104", soundPool.load(context, R.raw.voice104,
				// 1));
				// soundMap.put("105", soundPool.load(context, R.raw.voice105,
				// 1));
				// soundMap.put("106", soundPool.load(context, R.raw.voice106,
				// 1));
				// soundMap.put("107", soundPool.load(context, R.raw.voice107,
				// 1));
				// soundMap.put("108", soundPool.load(context, R.raw.voice108,
				// 1));	
				soundMap.put("101", soundPool.load(context, R.raw.yes_master, 1));	
				soundMap.put("102", soundPool.load(context, R.raw.mubiao_suoding, 1));
				soundMap.put("103", soundPool.load(context, R.raw.di_yi_ji_zzzb, 1));
				soundMap.put("104", soundPool.load(context, R.raw.zhunming, 1));
	
//				soundMap.put("103", soundPool.load(context, R.raw.blny_you_zhong_hqgdgj, 1));
//				 soundMap.put("104", soundPool.load(context, R.raw.blny_bu_zhi_dao_ckgysmbqlxr, 1));
			}
		}).start();

	}

	public static VoiceTool getInstance(Context context) {
		if (mVoiceTool == null) {
			mVoiceTool = new VoiceTool(context);
		}
		return mVoiceTool;
	}

	public void playVoice(String tag) {
		if (tag == null) {
			return;
		}
		if (!soundMap.containsKey(tag))
			return;
		int soundId = soundMap.get(tag);
		soundPool.play(soundId, 1, 1, 1, 0, 1);
	}
}
