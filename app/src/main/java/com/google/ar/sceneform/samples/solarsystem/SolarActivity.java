/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.solarsystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.samples.solarsystem.listener.OnItemAnimatorEndListener;
import java.util.ArrayList;

/**
 * This is a simple example that shows how to create an augmented reality (AR) application using the
 * ARCore and Sceneform APIs.
 */
public class SolarActivity extends AppCompatActivity implements OnItemAnimatorEndListener {
    private static final int RC_PERMISSIONS = 0x123;
    private boolean installRequested;
    private ArSceneView arSceneView;
    private boolean isAddAd;
    private ArrayList<MyAnimalNode> mMyAnimalNodes = new ArrayList<>();
    private ArrayList<AdItemVector3Info> mDefaultAdItemInfo;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solar);
        arSceneView = findViewById(R.id.ar_scene_view);
        mDefaultAdItemInfo = Utils.getDefaultAdItemInfo(this);
        // Set an update listener on the Scene that will hide the loading message once a Plane is
        // detected.
        arSceneView
                .getScene()
                .setOnUpdateListener(
                        frameTime -> {

                            Frame frame = arSceneView.getArFrame();
                            if (frame == null) {
                                return;
                            }
                            if (!isAddAd) {
                                arSceneView.getScene().addChild(createAdCircle());
                                isAddAd = true;
                            }

                            if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                                return;
                            }

                        });

        // Lastly request CAMERA permission which is required by ARCore.
        DemoUtils.requestCameraPermission(this, RC_PERMISSIONS);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (arSceneView.getSession() == null) {
            // If the session wasn't created yet, don't resume rendering.
            // This can happen if ARCore needs to be updated or permissions are not granted yet.
            try {
                Session session = DemoUtils.createArSession(this, installRequested);
                if (session == null) {
                    installRequested = DemoUtils.hasCameraPermission(this);
                    return;
                } else {
                    arSceneView.setupSession(session);
                }
            } catch (UnavailableException e) {
                DemoUtils.handleSessionException(this, e);
            }
        }

        try {
            arSceneView.resume();
        } catch (CameraNotAvailableException ex) {
            DemoUtils.displayError(this, "Unable to get camera", ex);
            finish();
            return;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        arSceneView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        arSceneView.destroy();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        if (!DemoUtils.hasCameraPermission(this)) {
            if (!DemoUtils.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                DemoUtils.launchPermissionSettings(this);
            } else {
                Toast.makeText(
                        this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                        .show();
            }
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Standard Android full-screen functionality.
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private Node createAdCircle() {
        Node mBase = new Node();
        mMyAnimalNodes.clear();
        for (int i = 0; i < mDefaultAdItemInfo.size(); i++) {
            AdItemVector3Info adItemVector3Info = mDefaultAdItemInfo.get(i);
            MyAnimalNode infoNode = new MyAnimalNode(this, adItemVector3Info.renderable.getView(), this);
            infoNode.setWorldPosition(adItemVector3Info.itemPosition);
            infoNode.setRenderable(adItemVector3Info.renderable);
            infoNode.setWorldRotation(adItemVector3Info.itemQuaternion);
            mBase.addChild(infoNode);
            mMyAnimalNodes.add(infoNode);
        }
        return mBase;
    }

    @Override
    public void onAnimalEndLister(MyAnimalNode myAnimalNode) {
        //bt_land.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAdItemClickLister(MyAnimalNode myAnimalNode) {
        for (int i = 0; i < mMyAnimalNodes.size(); i++) {
            if (mMyAnimalNodes.get(i) != myAnimalNode) {
                mMyAnimalNodes.get(i).setEnabled(false);
            }
        }
    }

}
