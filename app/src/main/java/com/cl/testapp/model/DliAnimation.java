package com.cl.testapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * 嘀哩嘀哩动画
 */
public class DliAnimation implements Serializable {

    private String category;
    private List<Animation> animationList;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Animation> getAnimationList() {
        return animationList;
    }

    public void setAnimationList(List<Animation> animationList) {
        this.animationList = animationList;
    }

}
