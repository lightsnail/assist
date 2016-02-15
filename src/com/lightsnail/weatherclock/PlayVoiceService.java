package com.lightsnail.weatherclock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baidu.tts.answer.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.lightsnail.utils.AppLog;
import com.zidoo.custom.init.ZidooJarPermissions;
import com.zidoo.custom.net.ZidooNetStatusTool;
import com.zidoo.custom.net.ZidooNetStatusTool.NetWorkListener;
import com.zidoo.custom.share.ZidooSharedPrefsUtil;
import com.zidoo.custom.update.ZidooUpdate;

public class PlayVoiceService extends Service implements SpeechSynthesizerListener {
	private SpeechSynthesizer	mSpeechSynthesizer;
	private boolean				isInitSuccess						= false;
	private boolean				isInitFinish						= false;
	private String				mSampleDirPath;
	private Context				mContext;
	private static final String	SAMPLE_DIR_NAME						= "lovebaiduTTS";
	private static final String	SPEECH_FEMALE_MODEL_NAME			= "bd_etts_speech_female.dat";
	private static final String	SPEECH_MALE_MODEL_NAME				= "bd_etts_speech_male.dat";
	private static final String	TEXT_MODEL_NAME						= "bd_etts_text.dat";
	private static final String	LICENSE_FILE_NAME					= "temp_license";
	private static final String	ENGLISH_SPEECH_FEMALE_MODEL_NAME	= "bd_etts_speech_female_en.dat";
	private static final String	ENGLISH_SPEECH_MALE_MODEL_NAME		= "bd_etts_speech_male_en.dat";
	private static final String	ENGLISH_TEXT_MODEL_NAME				= "bd_etts_text_en.dat";

	private ZidooNetStatusTool	mNetStatusTool						= null;
	public static boolean		ISPLAYVOICE							= true;
	public static int			PLAYSYSTLE							= 0;
	private Handler				mHandler							= null;
	private final static int	PLAYVOICE							= 0;
	private final static int	PLAYVOICETIME						= 250;
	private FrameWindowManager mFrameWindowManager;
	private X_WeatherManager mX_WeatherManager;
	protected String mWeatherString = "暂无数据";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		AppLog.d("bob  PlayVoiceService  init");
		mContext = this;
		mFrameWindowManager = new FrameWindowManager(mContext,new OnClickListener() {
			@Override
			public void onClick(View v) {
//				 Intent serviceIntent = new Intent(mContext, PlayVoiceService.class);
//				 serviceIntent.setAction("android.voice.play");
//				 serviceIntent.putExtra("voice", text) ;
//				 startService(serviceIntent);
				 speak(mWeatherString);
				 
			}
		});
		mX_WeatherManager = new X_WeatherManager(mContext,new OnWeatherStringListenner(){

			@Override
			public void onWeatherString(String text) {
				mWeatherString  = text;
			}

			@Override
			public void onWeatherWendu(String wendu) {
				mFrameWindowManager.setWendu(wendu);
			}});
		PlayVoiceService.ISPLAYVOICE = ZidooSharedPrefsUtil.getValue(this, "isPalyVoice", true);
		init();
		ZidooJarPermissions.initZidooJar(this);
		mNetStatusTool = new ZidooNetStatusTool(this, new NetWorkListener() {

			@Override
			public void wifiConnected(boolean arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void netWorkConnected(boolean arg0) {
				// System.out.println("bob  voice net connect arg0 = " + arg0 +
				// "  isInitSuccess = " + isInitSuccess);
				if (arg0 && !isInitSuccess) {
					init();
				}
			}

			@Override
			public void ethernetConnected(boolean arg0) {

			}
		});
		PLAYSYSTLE = ZidooSharedPrefsUtil.getValue(this, "playsystle", 0);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					int result = mSpeechSynthesizer.speak(((String) msg.obj));
					if (result < 0) {
						AppLog.d("bob   = " + "error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		updataApk();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		try {
			mNetStatusTool.release();
			mSpeechSynthesizer.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	private void updataApk() {
		try {
			new ZidooUpdate(this, ZidooUpdate.CHINA).getImg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		AppLog.d("bob  voice init = init");
		isInitFinish = false;
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					initialEnv();
					initialTts();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent != null) {
			speak(intent.getStringExtra("voice"));
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void speak(String speak) {
		try {
			AppLog.d("bob  isInitSuccess  = " + isInitSuccess + "  isPlay = " + PlayVoiceService.ISPLAYVOICE + "  PLAYSYSTLE = " + PLAYSYSTLE + "  speak = " + speak
					+ "  isInitFinish = " + isInitFinish);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			mHandler.removeMessages(PLAYVOICE); 
			if (isInitFinish && PlayVoiceService.ISPLAYVOICE && mSpeechSynthesizer != null && speak != null && !speak.trim().equals("")) {
				
				mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, PLAYSYSTLE == 0 ? SpeechSynthesizer.SPEAKER_FEMALE : SpeechSynthesizer.SPEAKER_MALE);
				// mSpeechSynthesizer.stop();
				mHandler.sendMessageDelayed(mHandler.obtainMessage(PLAYVOICE, speak.trim()), PLAYVOICETIME);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialEnv() {
		if (mSampleDirPath == null) {
			String sdcardPath = Environment.getExternalStorageDirectory().toString();
			mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
		}
		makeDir(mSampleDirPath);
		copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
		copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
		copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
		copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
		
		
		// copyFromAssetsToSdcard(false, "english/" +
		// ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" +
		// ENGLISH_SPEECH_FEMALE_MODEL_NAME);
		// copyFromAssetsToSdcard(false, "english/" +
		// ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" +
		// ENGLISH_SPEECH_MALE_MODEL_NAME);
		// copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME,
		// mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME);
	}

	private void initialTts() {
		try {
			AppLog.d("bob  initialTts  = ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
		this.mSpeechSynthesizer.setContext(mContext);
		this.mSpeechSynthesizer.setSpeechSynthesizerListener(this);
//		mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
//		mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "6");
//		mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "4");

		this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/" + TEXT_MODEL_NAME);
		this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
		// this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE,
		// mSampleDirPath + "/"
		// + LICENSE_FILE_NAME);
		// this.mSpeechSynthesizer.setAppId("your_app_id");
		// this.mSpeechSynthesizer.setApiKey("your_api_key", "your_secret_key");

		this.mSpeechSynthesizer.setAppId("7724228");
//		 this.mSpeechSynthesizer.setApiKey("eHMNjjK7hKrzol9LE8TWuYaN", "3108c8f790fa9940314d0a4a2448cc34");
		AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
		if (authInfo.isSuccess()) {
			AppLog.d("bob  voice init success **********");
			// toPrint("auth success");
			try {
				mSpeechSynthesizer.initTts(TtsMode.MIX);
			} catch (Exception e) {
				
			}
			AppLog.d("bob  voice init success ---------");
			isInitSuccess = true;
		} else {
			String errorMsg = authInfo.getTtsError().getDetailMessage();
			AppLog.d("bob  voice init failed  ");
			AppLog.d("bob  voice init errorMsg  = " + errorMsg);
			// toPrint("auth failed errorMsg=" + errorMsg);
			isInitSuccess = false;
		}
		isInitFinish = true;
	}

	private void toPrint(String str) {
		// Message msg = Message.obtain(); 
		// msg.obj = str;
		// this.mHandler.sendMessage(msg);
	}

	private void print(Message msg) {
		String message = (String) msg.obj;
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		// scrollLog(message);
	}

	private void makeDir(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 灏唖ample宸ョ▼闇�鐨勮祫婧愭枃浠舵嫹璐濆埌SD鍗′腑浣跨敤锛堟巿鏉冩枃浠朵负涓存椂鎺堟潈鏂囦欢锛岃娉ㄥ唽姝ｅ紡鎺堟潈锛�
	 * 
	 * @param isCover
	 *            鏄惁瑕嗙洊宸插瓨鍦ㄧ殑鐩爣鏂囦欢
	 * @param source
	 * @param dest
	 */
	private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
		File file = new File(dest);
		if (isCover || (!isCover && !file.exists())) {
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				is = mContext.getResources().getAssets().open(source);
				String path = dest;
				fos = new FileOutputStream(path);
				byte[] buffer = new byte[1024];
				int size = 0;
				while ((size = is.read(buffer, 0, 1024)) >= 0) {
					fos.write(buffer, 0, size);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onError(String arg0, SpeechError arg1) {
		// TODO Auto-generated method stub
		// System.out.println("bob  onError  arg0 = " + arg0);
	}

	@Override
	public void onSpeechFinish(String arg0) {
		// TODO Auto-generated method stub
		// System.out.println("bob  onSpeechFinish  arg0 = " + arg0);
	}

	@Override
	public void onSpeechProgressChanged(String arg0, int arg1) {
		// TODO Auto-generated method stub
		// System.out.println("bob  onSpeechProgressChanged  arg0 = " + arg0);
	}

	@Override
	public void onSpeechStart(String arg0) {
		// TODO Auto-generated method stub
		// System.out.println("bob  onSpeechStart  arg0 = " + arg0);
	}

	@Override
	public void onSynthesizeDataArrived(String arg0, byte[] arg1, int arg2) {
		// TODO Auto-generated method stub
		// System.out.println("bob  onSynthesizeDataArrived  arg0 = " + arg0);
	}

	@Override
	public void onSynthesizeFinish(String arg0) {
		// TODO Auto-generated method stub
		// System.out.println("bob  onSynthesizeFinish  arg0 = " + arg0);
	}

	@Override
	public void onSynthesizeStart(String arg0) {
		// TODO Auto-generated method stub
		// System.out.println("bob  onSynthesizeStart  arg0 = " + arg0);
	}
}
