package com.nullifier.virocoredemo.utils;

import android.content.Context;
import android.widget.Toast;

import com.nullifier.virocoredemo.AdItemView;
import com.nullifier.virocoredemo.bean.AdModuleInfo;
import com.nullifier.virocoredemo.bean.AdInfo;
import com.nullifier.virocoredemo.listener.OnItemAnimatorEndListener;
import com.viro.core.AndroidViewTexture;
import com.viro.core.ClickListener;
import com.viro.core.ClickState;
import com.viro.core.Material;
import com.viro.core.Node;
import com.viro.core.Quad;
import com.viro.core.Vector;
import com.viro.core.ViroView;

import java.util.Arrays;

public class AditemViewUitls {

    private Context mContext;
    private ViroView mViroView;

    public AditemViewUitls(Context context, ViroView viroView) {
        this.mContext = context;
        this.mViroView = viroView;
    }


    public AdModuleInfo createAdView(AdInfo adInfo, OnItemAnimatorEndListener onItemAnimatorEndListener) {
        // Basic Android view setting (Create / bind your layouts here).
        final AdItemView androidViews = new AdItemView(mContext);
        androidViews.setAdInfo(adInfo);
        androidViews.setOnItemAnimatorEndListener(onItemAnimatorEndListener);
        // Create a Viro AndroidViewTexture that represents our Views.
        int pxWidth = 400;
        int pxHeight = 190;
        boolean isAccelerated = true;
        AndroidViewTexture androidTexture = new AndroidViewTexture(mViroView, pxWidth, pxHeight, isAccelerated);
        androidTexture.attachView(androidViews);

        // Set the Texture to be used on our surface in 3D.
        final Material material = new Material();
        material.setDiffuseTexture(androidTexture);

        Quad surface = new Quad(0.9f, 0.5f);
        surface.setMaterials(Arrays.asList(material));
        Node surfaceNode = new Node();
        surfaceNode.setGeometry(surface);
        surfaceNode.setPosition(adInfo.position);
        surfaceNode.setRotation(adInfo.rotation);
        // Add clicklisteners to
        //surfaceNode.setClickListener(androidTexture.getClickListenerWithQuad(surface));

        surfaceNode.setClickListener(new ClickListener() {
            @Override
            public void onClick(int i, Node node, Vector vector) {
                Toast.makeText(mContext, "onClick:x" + vector.x + "y" + vector.y + "z" + vector.z, Toast.LENGTH_SHORT).show();
                androidViews.playAnimation(true);
                androidViews.canPlayAnimation(true);
            }

            @Override
            public void onClickState(int i, Node node, ClickState clickState, Vector vector) {

            }
        });

        return new AdModuleInfo(androidViews, surfaceNode);
    }

}
