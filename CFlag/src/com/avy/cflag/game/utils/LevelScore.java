package com.avy.cflag.game.utils;

public class LevelScore {
	private int levelNo;
	private int moves;
	private int shots;
	private boolean hintUsed;

	public LevelScore() {
		levelNo = 0;
		moves = 0;
		shots = 0;
		hintUsed = false;
	}

	public LevelScore(final int levelNo, final int moves, final int shots, final boolean hintUsed) {
		this.levelNo = levelNo;
		this.moves = moves;
		this.shots = shots;
		this.hintUsed = hintUsed;
	}

	public int getLevelNo() {
		return levelNo;
	}

	public void setLevelNo(final int levelNo) {
		this.levelNo = levelNo;
	}

	public int getMoves() {
		return moves;
	}

	public void setMoves(final int moves) {
		this.moves = moves;
	}

	public int getShots() {
		return shots;
	}

	public void setShots(final int shots) {
		this.shots = shots;
	}

	public boolean isHintUsed() {
		return hintUsed;
	}

	public void setHintUsed(final boolean hintUsed) {
		this.hintUsed = hintUsed;
	}
}
