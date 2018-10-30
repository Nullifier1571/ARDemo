package com.nullifier.virocoredemo;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nullifier.virocoredemo.listener.OnItemAnimatorEndListener;
import com.viro.core.ARAnchor;
import com.viro.core.ARImageTarget;
import com.viro.core.ARNode;
import com.viro.core.ARScene;
import com.viro.core.AndroidViewTexture;
import com.viro.core.Animation;
import com.viro.core.AsyncObject3DListener;
import com.viro.core.ClickListener;
import com.viro.core.ClickState;
import com.viro.core.Material;
import com.viro.core.Node;
import com.viro.core.Object3D;
import com.viro.core.OmniLight;
import com.viro.core.Quad;
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
    private static final String resourcePath = "file:///android_asset/bangbang/bangbang.vrx";

    private static final String TAG = MainActivity.class.getSimpleName();
    protected ViroView mViroView;
    private ARScene mScene;
    private ARImageTarget mImageTarget;
    private Node mBlackPantherNode;
    private AssetManager mAssetManager;
    private Object3D mBlackPantherModel;

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

        // Create an ARImageTarget out of the Black Panther poster
        Bitmap blackPantherPoster = getBitmapFromAssets("logo.jpg");
        mImageTarget = new ARImageTarget(blackPantherPoster, ARImageTarget.Orientation.Up, 0.188f);
        mScene.addARImageTarget(mImageTarget);

        // Create a Node containing the Black Panther model
        mBlackPantherNode = initBlackPantherNode();
        mBlackPantherNode.addChildNode(initLightingNode());
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
        currentAnimationIndex = 0;
        playAnimation(animalNames);
    }

    private void playAnimation(final String[] animalNames) {

        Log.e("BBBBBBB", "current play animal : " + animalNames[currentAnimationIndex]);
        Animation animationIdle = mBlackPantherModel.getAnimation(animalNames[currentAnimationIndex]);
        animationIdle.setListener(new Animation.Listener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationFinish(Animation animation, boolean b) {
                if (currentAnimationIndex < animalNames.length) {
                    currentAnimationIndex++;
                    playAnimation(animalNames);
                } else {
                    mBlackPantherNode.setVisible(false);
                }
            }
        });
        animationIdle.play();
    }

    // +---------------------------------------------------------------------------+
    //  3D Scene Construction
    // +---------------------------------------------------------------------------+

    private Node initBlackPantherNode() {
        Node blackPantherNode = new Node();
        mBlackPantherModel = new Object3D();
        mBlackPantherModel.setPosition(new Vector(0, -1, -4));
        //mBlackPantherModel.setRotation(new Vector(Math.toRadians(-90), 0, 0));
        mBlackPantherModel.setScale(new Vector(0.6f, 0.6f, 0.6f));
        //mBlackPantherModel.setScale(new Vector(10f, 10f, 10f));
        mBlackPantherModel.loadModel(mViroView.getViroContext(), Uri.parse(resourcePath), Object3D.Type.FBX, new AsyncObject3DListener() {
            @Override
            public void onObject3DLoaded(final Object3D object, final Object3D.Type type) {
                mObjLoaded = true;
            }

            @Override
            public void onObject3DFailed(final String error) {
                Log.e(TAG, "Black Panther Object Failed to load.");
            }
        });

        blackPantherNode.addChildNode(mBlackPantherModel);

        return blackPantherNode;
    }

    private Node initLightingNode() {
        Vector omniLightPositions[] = {new Vector(-3, 3, 0.3),
                new Vector(3, 3, 1),
                new Vector(-3, -3, 1),
                new Vector(3, -3, 1)};

        Node lightingNode = new Node();
        for (Vector pos : omniLightPositions) {
            final OmniLight light = new OmniLight();
            light.setPosition(pos);
            light.setColor(Color.parseColor("#FFFFFF"));
            light.setIntensity(20);
            light.setAttenuationStartDistance(6);
            light.setAttenuationEndDistance(9);

            lightingNode.addLight(light);
        }

        // The spotlight will cast the shadows
        Spotlight spotLight = new Spotlight();
        spotLight.setPosition(new Vector(0, 5, -0.5));
        spotLight.setColor(Color.parseColor("#FFFFFF"));
        spotLight.setDirection(new Vector(0, -1, 0));
        spotLight.setIntensity(50);
        spotLight.setShadowOpacity(0.4f);
        spotLight.setShadowMapSize(2048);
        spotLight.setShadowNearZ(2f);
        spotLight.setShadowFarZ(7f);
        spotLight.setInnerAngle(5);
        spotLight.setOuterAngle(20);
        spotLight.setCastsShadow(true);

        lightingNode.addLight(spotLight);

        // Add a lighting environment for realistic PBR rendering
        Texture environment = Texture.loadRadianceHDRTexture(Uri.parse("file:///android_asset/wakanda_360.hdr"));
        mScene.setLightingEnvironment(environment);

        // Add shadow planes: these are "invisible" surfaces on which virtual shadows will be cast,
        // simulating real-world shadows
        final Material material = new Material();
        material.setShadowMode(Material.ShadowMode.TRANSPARENT);

        Surface surface = new Surface(3, 3);
        surface.setMaterials(Arrays.asList(material));

        Node surfaceShadowNode = new Node();
        surfaceShadowNode.setRotation(new Vector(Math.toRadians(-90), 0, 0));
        surfaceShadowNode.setGeometry(surface);
        surfaceShadowNode.setPosition(new Vector(0, 0, 0.0));
        lightingNode.addChildNode(surfaceShadowNode);

        lightingNode.setRotation(new Vector(Math.toRadians(-90), 0, 0));
        return lightingNode;
    }

    // +---------------------------------------------------------------------------+
    //  Lifecycle
    // +---------------------------------------------------------------------------+

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
