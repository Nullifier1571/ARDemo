package com.nullifier.virocoredemo.bean;

import com.viro.core.Vector;

public class AdInfo {
    public int picId;
    public int index;
    public String title;
    public String desc;
    public Vector position;
    public Vector rotation;

    public AdInfo( int picId, String title, String desc, Vector position, Vector rotation) {
        this.picId = picId;
        this.title = title;
        this.desc = desc;
        this.position = position;
        this.rotation = rotation;
    }
}
