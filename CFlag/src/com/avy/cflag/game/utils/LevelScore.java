package com.avy.cflag.game.utils;

public class LevelScore {
	private int levelNo;
	private int moves;
	private int shots;
	private int hintsUsed;

	public LevelScore() {
		levelNo = 0;
		moves = 0;
		shots = 0;
		hintsUsed = 0;
	}

	public LevelScore(final int levelNo, final int moves, final int shots, final int hintsUsed) {
		this.levelNo = levelNo;
		this.moves = moves;
		this.shots = shots;
		this.hintsUsed = hintsUsed;
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

	public int getHintsUsed() {
		return hintsUsed;
	}

	public void setHintsUsed(int hintsUsed) {
		this.hintsUsed = hintsUsed;
	}

}
