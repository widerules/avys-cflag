package com.avy.cflag.game.utils;

import com.avy.cflag.game.EnumStore.Medals;

public class LevelScore {
	private int levelNo;
	private int moves;
	private int shots;
	private int hintsUsed;
	private Medals medalWon;
	
	public LevelScore() {
		levelNo = 0;
		moves = 0;
		shots = 0;
		hintsUsed = 0;
		medalWon = Medals.None;
	}

	public LevelScore(final int levelNo, final int moves, final int shots, final int hintsUsed, final Medals medalWon) {
		this.levelNo = levelNo;
		this.moves = moves;
		this.shots = shots;
		this.hintsUsed = hintsUsed;
		this.medalWon=medalWon;
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

	public Medals getMedalWon() {
		return medalWon;
	}

	public void setMedalWon(Medals medalWon) {
		this.medalWon = medalWon;
	}
}
