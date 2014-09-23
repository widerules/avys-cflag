package com.avy.cflag.base;

public class Point {
	public int x;
	public int y;

	public Point() {
		x = 0;
		y = 0;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point add(Point p) {
		return new Point(this.x + p.x, this.y + p.y);

	}
}
