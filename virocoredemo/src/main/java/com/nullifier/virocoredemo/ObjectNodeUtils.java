package com.nullifier.virocoredemo;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.nullifier.virocoredemo.bean.ObjectInfo;
import com.nullifier.virocoredemo.listener.OnObjectLoadedListener;
import com.viro.core.ARScene;
import com.viro.core.AsyncObject3DListener;
import com.viro.core.Material;
import com.viro.core.Node;
import com.viro.core.Object3D;
import com.viro.core.OmniLight;
import com.viro.core.Spotlight;
import com.viro.core.Surface;
import com.viro.core.Texture;
import com.viro.core.Vector;
import com.viro.core.ViroContext;

import java.util.Arrays;

public class ObjectNodeUtils {


    private static final String TAG = "ObjectNodeUtils";
   /* private final ViroContext viroContext;
    private final ARScene mScene;

    public ObjectNodeUtils(ViroContext viroContext, ARScene mScene) {
        this.viroContext = viroContext;
        this.mScene = mScene;
    }*/

    // +---------------------------------------------------------------------------+
    //  3D Scene Construction
    // +---------------------------------------------------------------------------+

    public static Object3D initObject(ViroContext viroContext,ObjectInfo objectInfo, final OnObjectLoadedListener onObjectLoadedListener) {

        Object3D mBlackPantherModel = new Object3D();
        if (objectInfo == null) {
            return mBlackPantherModel;
        }
        mBlackPantherModel.setPosition(objectInfo.position);
        mBlackPantherModel.setRotation(objectInfo.rotation);
        mBlackPantherModel.setScale(objectInfo.rotation);
        mBlackPantherModel.loadModel(viroContext, Uri.parse(objectInfo.resourcePath), Object3D.Type.FBX, new AsyncObject3DListener() {
            @Override
            public void onObject3DLoaded(final Object3D object, final Object3D.Type type) {
                //mObjLoaded = true;
                onObjectLoadedListener.onObject3DLoadedSuccessful();
            }

            @Override
            public void onObject3DFailed(final String error) {
                Log.e(TAG, "Black Panther Object Failed to load.");
            }
        });
        return mBlackPantherModel;
    }

    public static Node initLightingNode(ARScene mScene) {
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

}
