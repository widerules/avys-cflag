package com.avy.cflag.base;

public class Rect {
    public int left;
    public int top;
    public int right;
    public int bottom;
    
    public Rect() {
        this.left = 0;
        this.top = 0;
        this.right = 0;
        this.bottom = 0;
    }
    
    public Rect(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
