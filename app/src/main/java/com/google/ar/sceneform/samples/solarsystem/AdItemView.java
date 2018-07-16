package com.google.ar.sceneform.samples.solarsystem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class AdItemView extends RelativeLayout {
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
    }
}
