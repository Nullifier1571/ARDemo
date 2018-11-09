package com.nullifier.virocoredemo.bean;

import com.viro.core.Vector;

public class ObjectInfo {
    public Vector position;
    public Vector rotation;
    public Vector scale;
    public String resourcePath;

    public ObjectInfo(Vector position, Vector rotation, Vector scale, String resourcePath) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.resourcePath = resourcePath;
    }

    @Override
    public String toString() {
        return "ObjectInfo{" +
                "position=" + position +
                ", rotation=" + rotation +
                ", scale=" + scale +
                ", resourcePath='" + resourcePath + '\'' +
                '}';
    }
}
