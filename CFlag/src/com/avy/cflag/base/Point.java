package com.avy.cflag.base;

public class Point {
	public int x;
	public int y;

	public Point() {
		x = 0;
		y = 0;
	}

	public Point(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public Point add(final Point p) {
		return new Point(x + p.x, y + p.y);
	}

	public boolean equals(final Point p) {
		return (p.x == x) && (p.y == y);
	}
}
