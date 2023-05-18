package com.atanava.bts.render;

public class Vec2 {

    public double x;
    public double y;


    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double cross(Vec2 v) {
        return x * v.y - y * v.x;
    }

}