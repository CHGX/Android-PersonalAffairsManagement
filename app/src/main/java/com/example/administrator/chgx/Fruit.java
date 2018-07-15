package com.example.administrator.chgx;

import java.sql.Time;

/**
 * Created by Administrator on 2018/7/12.
 */

public class Fruit {
    private String name;
    private int imageId;
    private String Time1;

    public Fruit(String name, int imageId, String Time1) {
        this.name = name;
        this.imageId = imageId;
        this.Time1 = Time1;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTime1() {
        return Time1;
    }
}
