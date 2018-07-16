package com.google.ar.sceneform.samples.solarsystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.samples.solarsystem.listener.OnItemAnimatorEndListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MyAnimalNode extends Node implements Node.OnTapListener, Animator.AnimatorListener {
    private OnItemAnimatorEndListener mOnItemAnimatorEndListener;
    private Context mContext;
    @Nullable
    private AnimatorSet mAnimationSet = null;
    private float degreesPerSecond = 90.0f;

    private float lastSpeedMultiplier = 1.0f;

    private ViewRenderable testViewRenderable;
    private RelativeLayout rl_board;

    public MyAnimalNode(Context context, View target, OnItemAnimatorEndListener onItemAnimatorEndListener) {
        this.mContext = context;
        this.mOnItemAnimatorEndListener = onItemAnimatorEndListener;
        rl_board = target.findViewById(R.id.rl_board);
        setOnTapListener(this);
        createAnimator(target);
        //rl_board.setOnClickListener(this);
    }


    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

        // Animation hasn't been set up.
        if (mAnimationSet == null) {
            return;
        }

        // Check if we need  to change the speed of rotation.

        // Nothing has changed. Continue rotating at the same speed.
    }

    /**
     * Sets rotation speed
     */
    public void setDegreesPerSecond(float degreesPerSecond) {
        this.degreesPerSecond = degreesPerSecond;
    }

    @Override
    public void onActivate() {

    }

    @Override
    public void onDeactivate() {
    }

    private long getAnimationDuration() {
        return (long) (1000 * 360);
    }

    private void startAnimation() {
        if (mAnimationSet == null) {
            return;
        }
        mAnimationSet.start();
    }

    private void stopAnimation() {
        if (mAnimationSet == null) {
            return;
        }
        mAnimationSet.cancel();
        mAnimationSet = null;
    }

    /**
     * Returns an ObjectAnimator that makes this node rotate.
     */
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

    @Override
    public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
        Toast.makeText(mContext, "点击了。。", Toast.LENGTH_SHORT).show();
        mOnItemAnimatorEndListener.onAdItemClickLister(this);
        startAnimation();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        rl_board.setVisibility(View.GONE);
        mOnItemAnimatorEndListener.onAnimalEndLister(this);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

}
