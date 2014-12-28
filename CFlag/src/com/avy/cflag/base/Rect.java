package com.avy.cflag.base;

public class Rect {
	public int left;
	public int top;
	public int right;
	public int bottom;

	public Rect() {
		left = 0;
		top = 0;
		right = 0;
		bottom = 0;
	}

	public Rect(final int left, final int top, final int right, final int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
}
