package com.lightsnail.weatherclock;

import com.lightsnail.utils.app.AppUtils;

import android.R.animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class TranslucentActivity extends Activity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		 Intent intent = getIntent();
		 String hold_packagename = intent.getStringExtra("hold_packagename");
		   String direction = intent.getStringExtra("direction");
		   AppUtils.startAPPFromPackageName(this, hold_packagename);
		   
		   if("next".equals(direction)){
//			   overridePendingTransition(R.anim.right_enter_in, R.anim.left_exit_out);
//			   overridePendingTransition(R.anim.down_enter_in, R.anim.up_exit_out);
//			   overridePendingTransition(R.anim.right_drop_enter_in, R.anim.left_drop_exit_out);
			   overridePendingTransition(R.anim.left_rotate_enter_in, R.anim.right_rotate_exit_out);
		   }else {
//			   overridePendingTransition(R.anim.left_enter_in, R.anim.right_exit_out);
//			   overridePendingTransition(R.anim.up_enter_in, R.anim.down_exit_out);
//			   overridePendingTransition(R.anim.left_drop_enter_in, R.anim.right_drop_exit_out);
			   overridePendingTransition(R.anim.right_rotate_enter_in, R.anim.left_rotate_exit_out);
		   }   
		   
		   finish();
	}
}
