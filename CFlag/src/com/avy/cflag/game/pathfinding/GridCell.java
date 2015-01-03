package com.avy.cflag.game.pathfinding;

public class GridCell {
	public int x;
	public int y;

	/* for path finders */
	private float f, g, h;
	private boolean isWalkable;
	private int closedOnJob, openedOnJob;
	private GridCell parent;

	// for BTree
	private int index;

	public GridCell(final int column, final int row, final boolean isWalkable) {
		y = row;
		x = column;
		this.isWalkable = isWalkable;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public boolean isWalkable() {
		return isWalkable;
	}

	public void setWalkable(final boolean isWalkable) {
		this.isWalkable = isWalkable;
	}

	public float getF() {
		return f;
	}

	public void setF(final float f) {
		this.f = f;
	}

	public float getG() {
		return g;
	}

	public void setG(final float g) {
		this.g = g;
	}

	public float getH() {
		return h;
	}

	public void setH(final float h) {
		this.h = h;
	}

	public GridCell getParent() {
		return parent;
	}

	public void setParent(final GridCell parent) {
		this.parent = parent;
	}

	public int getClosedOnJob() {
		return closedOnJob;
	}

	public void setClosedOnJob(final int closedOnJob) {
		this.closedOnJob = closedOnJob;
	}

	public int getOpenedOnJob() {
		return openedOnJob;
	}

	public void setOpenedOnJob(final int openedOnJob) {
		this.openedOnJob = openedOnJob;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

}
