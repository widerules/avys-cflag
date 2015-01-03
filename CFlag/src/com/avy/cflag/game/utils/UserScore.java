package com.avy.cflag.game.utils;

import java.util.ArrayList;

import com.avy.cflag.game.EnumStore.Difficulty;

public class UserScore {
	private final int maxPlayedLevel[];
	private final ArrayList<LevelScore> scores[];

	@SuppressWarnings("unchecked")
	public UserScore() {
		maxPlayedLevel = new int[Difficulty.length()];
		scores = new ArrayList[Difficulty.length()];
		for (int i = 0; i < maxPlayedLevel.length; i++) {
			maxPlayedLevel[i] = 1;
			scores[i] = new ArrayList<LevelScore>();
			scores[i].add(new LevelScore(1, 0, 0, false));
		}
	}

	public int getMaxPlayedLevel(final Difficulty dclty) {
		return maxPlayedLevel[dclty.ordinal()];
	}

	public void setMaxPlayedLevel(final Difficulty dclty, final int maxPlayedLevel) {
		this.maxPlayedLevel[dclty.ordinal()] = maxPlayedLevel;
	}

	public void addScores(final Difficulty dclty, final LevelScore score) {
		scores[dclty.ordinal()].add(score);
	}

	public void updateScores(final Difficulty dclty, final LevelScore score) {
		final LevelScore existingScore = scores[dclty.ordinal()].get(score.getLevelNo() - 1);
		if (score.getMoves() < existingScore.getMoves()) {
			score.setMoves(existingScore.getMoves());
		}
		if (score.getShots() < existingScore.getShots()) {
			score.setShots(existingScore.getShots());
		}
		if (!score.isHintUsed() && existingScore.isHintUsed()) {
			score.setHintUsed(true);
		}
		scores[dclty.ordinal()].set(score.getLevelNo() - 1, score);
	}

	public LevelScore getScores(final Difficulty dclty, final int levelNo) {
		if (levelNo - 1< scores[dclty.ordinal()].size()) {
			return scores[dclty.ordinal()].get(levelNo - 1);
		} else {
			return new LevelScore();
		}
	}
}
