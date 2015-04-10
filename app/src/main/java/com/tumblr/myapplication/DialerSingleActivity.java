package com.tumblr.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

/**
 * Example activity that has all animation and transition code in a single Activity
 */
public class DialerSingleActivity extends Activity {

	private View mOpenPadFrameBtn, mPadFrame, mCallBtn;
	private boolean mPadOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialer_single);

		mOpenPadFrameBtn = findViewById(R.id.num_pad_btn);
		mOpenPadFrameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPadOpen = true;
				mOpenPadFrameBtn.animate().scaleX(0f).scaleY(0f).alpha(0f)
						.withEndAction(new Runnable() {
							@Override
							public void run() {
								mPadFrame.animate().translationY(0).withEndAction(new Runnable() {
									@Override
									public void run() {
										mCallBtn.animate().scaleX(1f).scaleY(1f).alpha(1f);
									}
								});
							}
						});

			}
		});

		mPadFrame = findViewById(R.id.number_pad);
		mPadFrame.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				mPadFrame.getViewTreeObserver().removeOnPreDrawListener(this);
				mPadFrame.setTranslationY(mPadFrame.getHeight());
				return true;
			}
		});

		mCallBtn = findViewById(R.id.call_button);
		mCallBtn.setAlpha(0f);
		mCallBtn.setScaleX(0f);
		mCallBtn.setScaleY(0f);

		mCallBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(DialerSingleActivity.this, "Calling bae", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (mPadOpen) {
			mPadOpen = false;
			mPadFrame.animate().translationY(mPadFrame.getHeight()).withEndAction(new Runnable() {
				@Override
				public void run() {
					mOpenPadFrameBtn.animate().scaleX(1f).scaleY(1f).alpha(1f);
				}
			});
			mCallBtn.animate().scaleX(0f).scaleY(0f).alpha(0f);
		} else {
			super.onBackPressed();
		}
	}
}
