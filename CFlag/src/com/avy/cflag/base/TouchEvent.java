package com.avy.cflag.base;

public class TouchEvent {
	public static final int TOUCH_DOWN=1;
	public static final int TOUCH_UP=2;
	public static final int NOT_TOUCHED=3;
	
	public int x;
	public int y;
	public int type;
	
	public TouchEvent() {
		x=0;
		y=0;
		type=NOT_TOUCHED;
	}
	
	public TouchEvent(int x, int y, int type) {
		this.x=x;
		this.y=y;
		this.type=type;
	}
	
	public Point getPos() {
		return new Point(x,y);
	}
}
