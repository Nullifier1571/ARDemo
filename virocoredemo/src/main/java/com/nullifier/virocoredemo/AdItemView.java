package com.nullifier.virocoredemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nullifier.virocoredemo.bean.AdInfo;
import com.nullifier.virocoredemo.listener.OnItemAnimatorEndListener;

public class AdItemView extends RelativeLayout implements Animator.AnimatorListener {

    private ImageView iv_ad_icon;
    private TextView tv_ad_title;
    private TextView tv_ad_intro;
    private AnimatorSet mAnimationSet = null;
    private OnItemAnimatorEndListener mOnItemAnimatorEndListener;
    private AdInfo mAdInfo;
    private boolean mNeedCallback;
    private boolean isCanPlayAnimation = true;

    public AdItemView(Context context) {
        super(context);
        initView(context);
    }

    public AdItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AdItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View rootView = View.inflate(context, R.layout.aditem_view, this);
        iv_ad_icon = rootView.findViewById(R.id.iv_ad_icon);
        tv_ad_title = rootView.findViewById(R.id.tv_ad_title);
        tv_ad_intro = rootView.findViewById(R.id.tv_ad_intro);
        createAnimator(rootView);
    }

    public void setAdInfo(AdInfo adInfo) {
        this.mAdInfo = adInfo;
        iv_ad_icon.setImageResource(adInfo.picId);
        tv_ad_title.setText(adInfo.title);
        tv_ad_intro.setText(adInfo.desc);
    }

    public AdInfo getAdInfo() {
        return mAdInfo;
    }

    public void setOnItemAnimatorEndListener(OnItemAnimatorEndListener onItemAnimatorEndListener) {
        this.mOnItemAnimatorEndListener = onItemAnimatorEndListener;
    }

    private void createAnimator(View target) {
        // Node's setLocalRotation method accepts Quaternions as parameters.
        // First, set up orientations that will animate a circle.

        mAnimationSet = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1f, 0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 1f, 0f);

        mAnimationSet.setInterpolator(new DecelerateInterpolator());
        mAnimationSet.setDuration(2000);
        //两个动画同时开始
        mAnimationSet.play(scaleX).with(scaleY).with(alpha);

        mAnimationSet.addListener(this);
    }

    public void canPlayAnimation(boolean isCanPlayAnimation) {
        this.isCanPlayAnimation = isCanPlayAnimation;
    }

    public void playAnimation(boolean needCallback) {
        if (mAnimationSet == null) {
            return;
        }
        if (!isCanPlayAnimation) {
            return;
        }
        this.mNeedCallback = needCallback;
        mAnimationSet.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (mNeedCallback) {
            mOnItemAnimatorEndListener.onAnimalStartLister(this);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (mNeedCallback) {
            mOnItemAnimatorEndListener.onAnimalEndLister(this);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
