package com.google.ar.sceneform.samples.solarsystem;

import android.content.Context;

import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class Utils {
    public static ArrayList<AdItemVector3Info> getDefaultAdItemInfo(Context context) {
        ArrayList<AdItemVector3Info> list = new ArrayList<>();

        AdItemVector3Info adItemVector3Info = createInfo(context, new Vector3(0f, -1f, -2f), new Vector3(0f, 1f, 0f), 0);
        list.add(adItemVector3Info);

        AdItemVector3Info adItemVector3Info1 = createInfo(context, new Vector3(1f, -1f, -2f), new Vector3(0f, 1f, 0f), 0);
        list.add(adItemVector3Info1);

        AdItemVector3Info adItemVector3Info2 = createInfo(context, new Vector3(2f, -0.5f, -1.5f), new Vector3(0f, 1f, 0f), 80);
        list.add(adItemVector3Info2);

        AdItemVector3Info adItemVector3Info3 = createInfo(context, new Vector3(1.5f, -1f, -0.5f), new Vector3(0f, 1f, 0f), 80);
        list.add(adItemVector3Info3);

        AdItemVector3Info adItemVector3Info4 = createInfo(context, new Vector3(-2f, -1f, -0.7f), new Vector3(0f, 1f, 0f), -80);
        list.add(adItemVector3Info4);

        AdItemVector3Info adItemVector3Info5 = createInfo(context, new Vector3(0.2f, -0.5f, -2f), new Vector3(0f, 1f, 0f), 0);
        list.add(adItemVector3Info5);
        return list;
    }

    private static AdItemVector3Info createInfo(Context context, Vector3 positionVector, Vector3 roationVector, int roation) {
        AdItemVector3Info adItemVector3Info = new AdItemVector3Info();
        adItemVector3Info.itemPosition = positionVector;
        adItemVector3Info.itemQuaternion = Quaternion.axisAngle(roationVector, roation);
        CompletableFuture<ViewRenderable> itemStage = ViewRenderable.builder().setView(context, R.layout.aditem_view).build();
        CompletableFuture.allOf(itemStage).handle(
                (notUsed, throwable) -> {

                    if (throwable != null) {
                        DemoUtils.displayError(context, "Unable to load renderable", throwable);
                        return null;
                    }
                    try {
                        adItemVector3Info.renderable = itemStage.get();
                    } catch (InterruptedException | ExecutionException ex) {
                        DemoUtils.displayError(context, "Unable to load renderable", ex);
                    }
                    return null;
                });
        return adItemVector3Info;
    }

}
