package com.nullifier.virocoredemo;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.nullifier.virocoredemo.bean.AdInfo;
import com.nullifier.virocoredemo.bean.AdModuleInfo;
import com.nullifier.virocoredemo.bean.ObjectInfo;
import com.nullifier.virocoredemo.listener.OnItemAnimatorEndListener;
import com.nullifier.virocoredemo.listener.OnObjectLoadedListener;
import com.nullifier.virocoredemo.utils.AditemViewUitls;
import com.viro.core.ARImageTarget;
import com.viro.core.ARScene;
import com.viro.core.Animation;
import com.viro.core.AsyncObject3DListener;
import com.viro.core.Material;
import com.viro.core.Node;
import com.viro.core.Object3D;
import com.viro.core.OmniLight;
import com.viro.core.Spotlight;
import com.viro.core.Surface;
import com.viro.core.Texture;
import com.viro.core.Vector;
import com.viro.core.ViroView;
import com.viro.core.ViroViewARCore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity implements OnItemAnimatorEndListener {
    //private static final String resourcePath="file:///android_asset/pumpkinman_anim/pumpkinman_anim.vrx";
    private static final String resourcePath = "file:///android_asset/bangbang/bangbang_cheer.vrx";

    String cheer = "BB_HH_45_Fianl_opencollada_Main|BB_HH_45_Fianl_opencollada_FKShoulder_RAction"
            +"\nBB_HH_45_Fianl_opencollada_Main|BB_HH_45_Fianl_opencollada_FKShoulder_RAction"
            +"\nBB_HH_45_Fianl_opencollada_Main|BB_HH_45_Fianl_opencollada_FKShoulder_RAction"
            +"\nBB_HH_45_Fianl_opencollada_Main|BB_HH_45_Fianl_opencollada_FKShoulder_RAction"
            +"\nBB_HH_45_Fianl_opencollada_Main|BB_HH_45_Fianl_opencollada_FKShoulder_RAction"
            +"\nBB_HH_45_Fianl_opencollada_Main|BB_HH_45_Fianl_opencollada_FKShoulder_RAction"
            +"\nBB_HH_45_Fianl_opencollada_Main|BB_HH_45_Fianl_opencollada_FKShoulder_RAction"
            +"\nBB_HH_45_Fianl_opencollada_Main|BB_HH_45_Fianl_opencollada_FKShoulder_RAction"
            ;

    private static final String TAG = "BBBBBBBBBB";
    protected ViroView mViroView;
    private ARScene mScene;
    private ARImageTarget mImageTarget;
    private Node mBlackPantherNode;
    private AssetManager mAssetManager;
    private Object3D mBlackPantherModel;
    private ObjectNodeUtils mObjectNodeUtils;

    private boolean mObjLoaded = false;
    private boolean mImageTargetFound = false;

    private AditemViewUitls mAditemViewUitls;
    private List<AdModuleInfo> mAditems;

    // +---------------------------------------------------------------------------+
    //  Initialization
    // +---------------------------------------------------------------------------+

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mViroView = new ViroViewARCore(this, new ViroViewARCore.StartupListener() {
            @Override
            public void onSuccess() {
                // Override this function to start building your scene here
                onRenderCreate();
            }

            @Override
            public void onFailure(ViroViewARCore.StartupError error, String errorMessage) {
                // Fail as you wish!
            }
        });
        setContentView(mViroView);

        mAditemViewUitls = new AditemViewUitls(MainActivity.this, mViroView);
    }


    private void onRenderCreate() {
        // Create the base ARScene
        mScene = new ARScene();
        // mObjectNodeUtils = new ObjectNodeUtils(mViroView.getViroContext(), mScene);
        // Create an ARImageTarget out of the Black Panther poster
        Bitmap blackPantherPoster = getBitmapFromAssets("logo.jpg");
        mImageTarget = new ARImageTarget(blackPantherPoster, ARImageTarget.Orientation.Up, 0.188f);
        mScene.addARImageTarget(mImageTarget);

        // Create a Node containing the Black Panther model
        mBlackPantherNode = initBlackPantherNode();
        mBlackPantherNode.addChildNode(ObjectNodeUtils.initLightingNode(mScene));
        mBlackPantherNode.setVisible(false);
        mScene.getRootNode().addChildNode(mBlackPantherNode);
        // Add the Surface to the scene.
        mAditems = createAditems();
        mViroView.setScene(mScene);
    }

    private List<AdInfo> createAdInfos() {
        ArrayList<AdInfo> adInfos = new ArrayList<>();
        adInfos.add(new AdInfo(R.mipmap.wbu_default_avatar, "标题", "描述描述", new Vector(0, 0, -1.5), new Vector(Math.toRadians(0), 0, 0)));
        adInfos.add(new AdInfo(R.mipmap.wbu_default_avatar, "标题1", "描述描述1", new Vector(1.2, 0, -1.5), new Vector(0, Math.toRadians(-30), 0)));
        adInfos.add(new AdInfo(R.mipmap.wbu_default_avatar, "标题2", "描述描述2", new Vector(-1.2, 0, -1.5), new Vector(0, Math.toRadians(30), 0)));
        adInfos.add(new AdInfo(R.mipmap.wbu_default_avatar, "标题3", "描述描述3", new Vector(0.2, -0.5, -1.5), new Vector(Math.toRadians(0), 0, 0)));
        adInfos.add(new AdInfo(R.mipmap.wbu_default_avatar, "标题4", "描述描述4", new Vector(-0.2, 0.5, -1.5), new Vector(Math.toRadians(0), 0, 0)));


        return adInfos;
    }

    private List<AdModuleInfo> createAditems() {
        List<AdInfo> adInfos = createAdInfos();
        ArrayList<AdModuleInfo> adModuleInfos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AdInfo adInfo = adInfos.get(i);
            adInfo.index = i;
            AdModuleInfo adView = mAditemViewUitls.createAdView(adInfo, this);
            adModuleInfos.add(adView);
            mScene.getRootNode().addChildNode(adView.adItemNode);
        }
        return adModuleInfos;
    }

    private int currentAnimationIndex = 0;

    private void startPantherExperience() {

        mBlackPantherNode.setVisible(true);
        // Animate the black panther's jump animation

        Set<String> animationKeys = mBlackPantherModel.getAnimationKeys();
        Iterator<String> iterator = animationKeys.iterator();
        String[] animalNames = new String[animationKeys.size()];


        animationKeys.toArray(animalNames);
        while (iterator.hasNext()) {
            Log.e("BBBBBBB", "animal: " + iterator.next());
        }
        Log.e("BBBBBBB", "==========================================");
        currentAnimationIndex = 0;
        Log.e("BBBBBBB", "总共动画个数: " + animalNames.length);
        /*String[] split = cheer.split("\n");
        playAnimation(split);*/
        playAnimation(animalNames);
    }

    long currentTime = 0;

    private void playAnimation(final String[] animalNames) {
        if (animalNames.length == 0) {
            return;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        currentTime = 0;
        Animation animationIdle = mBlackPantherModel.getAnimation(animalNames[currentAnimationIndex]);
        animationIdle.setListener(new Animation.Listener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationFinish(Animation animation, boolean b) {
                if (currentAnimationIndex < animalNames.length - 1) {
                    Log.e("BBBBBBB", "刚才播放的动画名字是 : " + animalNames[currentAnimationIndex] + "动画持续时间是=>" + (System.currentTimeMillis() - currentTime));
                    currentAnimationIndex++;
                    playAnimation(animalNames);
                } else {
                    mBlackPantherNode.setVisible(false);
                }
            }
        });
        currentTime = System.currentTimeMillis();
        animationIdle.play();
    }

    // +---------------------------------------------------------------------------+
    //  3D Scene Construction
    // +---------------------------------------------------------------------------+

    private Node initBlackPantherNode() {
        Node blackPantherNode = new Node();

        ObjectInfo objectInfo = new ObjectInfo(new Vector(0, -1, -4), new Vector(Math.toRadians(-90), 0, 0), new Vector(0.5f, 0.5f, 0.5f), resourcePath);

        mBlackPantherModel = ObjectNodeUtils.initObject(mViroView.getViroContext(), objectInfo, new OnObjectLoadedListener() {
            @Override
            public void onObject3DLoadedSuccessful() {
                Log.e(TAG, "Black Panther Objecton Object3DLoadedSuccessful to load.");
                mObjLoaded = true;
            }

            @Override
            public void onObject3DLoadedError(String error) {
                Log.e(TAG, "Black Panther Object Failed to load.");
            }
        });
        blackPantherNode.addChildNode(mBlackPantherModel);

        return blackPantherNode;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViroView.onActivityStarted(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViroView.onActivityResumed(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViroView.onActivityPaused(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViroView.onActivityStopped(this);
    }

    // +---------------------------------------------------------------------------+
    //  Utility Functions
    // +---------------------------------------------------------------------------+

    private Bitmap getBitmapFromAssets(String assetName) {
        if (mAssetManager == null) {
            mAssetManager = getResources().getAssets();
        }

        InputStream imageStream;
        try {
            imageStream = mAssetManager.open(assetName);
        } catch (IOException exception) {
            Log.w("Viro", "Unable to find image [" + assetName + "] in assets! Error: "
                    + exception.getMessage());
            return null;
        }
        return BitmapFactory.decodeStream(imageStream);
    }

    @Override
    public void onAnimalEndLister(AdItemView adItemView) {
        mBlackPantherNode.setVisible(true);
        startPantherExperience();
    }

    @Override
    public void onAnimalStartLister(AdItemView adItemView) {
        for (int i = 0; i < mAditems.size(); i++) {
            if (i != adItemView.getAdInfo().index) {
                mAditems.get(i).adItemView.playAnimation(false);
            }
        }

    }
}
