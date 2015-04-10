package com.tumblr.myapplication.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kevingrant on 3/26/15.
 */
public class ScaleTransition extends Visibility {

	private static boolean DBG = false;
	public static final String TAG = ScaleTransition.class.getSimpleName();

	public ScaleTransition() {
	}

	private Animator createAnimation(final View view, float startScale, float endScale) {
		if (startScale == endScale) {
			return null;
		}

		view.setScaleX(startScale);
		view.setScaleY(startScale);

		ValueAnimator animator = ValueAnimator.ofFloat(startScale, endScale);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				final float scale = (Float) animation.getAnimatedValue();
				view.setScaleX(scale);
				view.setScaleY(scale);
			}
		});

		ScaleAnimatorListener listener = new ScaleAnimatorListener(view);
		animator.addListener(listener);
		animator.addPauseListener(listener);

		return animator;
	}

	@Override
	public Animator onAppear(ViewGroup sceneRoot, View view,
	                         TransitionValues startValues, TransitionValues endValues) {
		if (DBG) {
			View startView = (startValues != null) ? startValues.view : null;
			Log.d(TAG, "Fade.onAppear: startView, startVis, endView, endVis = " +
					startView + ", " + view);
		}
		return createAnimation(view, 0, 1);
	}

	@Override
	public Animator onDisappear(ViewGroup sceneRoot, final View view,
	                            TransitionValues startValues, TransitionValues endValues) {
		return createAnimation(view, 1, 0);
	}

	private static class ScaleAnimatorListener extends AnimatorListenerAdapter {
		private final View mView;
		private boolean mCanceled = false;
		private float mPausedAlpha = -1;
		private boolean mLayerTypeChanged = false;

		public ScaleAnimatorListener(View view) {
			mView = view;
		}

		@Override
		public void onAnimationStart(Animator animator) {
			if (mView.hasOverlappingRendering() && mView.getLayerType() == View.LAYER_TYPE_NONE) {
				mLayerTypeChanged = true;
				mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
			}
		}

		@Override
		public void onAnimationCancel(Animator animator) {
			mCanceled = true;
			if (mPausedAlpha >= 0) {
				mView.setScaleX(mPausedAlpha);
				mView.setScaleY(mPausedAlpha);
			}
		}

		@Override
		public void onAnimationEnd(Animator animator) {
			if (!mCanceled) {
				mView.setScaleX(1);
				mView.setScaleY(1);
			}
			if (mLayerTypeChanged) {
				mView.setLayerType(View.LAYER_TYPE_NONE, null);
			}
		}

		@Override
		public void onAnimationPause(Animator animator) {
			mPausedAlpha = mView.getScaleX();
			mView.setScaleX(1);
			mView.setScaleY(1);
		}

		@Override
		public void onAnimationResume(Animator animator) {
			mView.setScaleX(mPausedAlpha);
			mView.setScaleY(mPausedAlpha);
		}
	}
}
