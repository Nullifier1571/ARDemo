package com.nullifier.virocoredemo.bean;

import com.nullifier.virocoredemo.AdItemView;
import com.viro.core.Node;

public class AdModuleInfo {
    public AdItemView adItemView;
    public Node adItemNode;

    public AdModuleInfo(AdItemView adItemView, Node adItemNode) {
        this.adItemView = adItemView;
        this.adItemNode = adItemNode;
    }


}
