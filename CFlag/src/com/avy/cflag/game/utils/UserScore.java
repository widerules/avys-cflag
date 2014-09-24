package com.avy.cflag.game.utils;

import java.util.ArrayList;

import com.avy.cflag.game.MemStore.Difficulty;

public class UserScore {
	private int maxPlayedLevel[];
	private ArrayList<LevelScore> scores[];

	@SuppressWarnings("unchecked")
	public UserScore() {
		maxPlayedLevel = new int[Difficulty.length()];
		scores = (ArrayList<LevelScore>[]) new ArrayList[Difficulty.length()];
		for (int i = 0; i < maxPlayedLevel.length; i++) {
			maxPlayedLevel[i] = 1;
			scores[i] = new ArrayList<LevelScore>();
			scores[i].add(new LevelScore(1,0,0,false));
		}
	}

	public int getMaxPlayedLevel(Difficulty dclty) {
		return maxPlayedLevel[dclty.ordinal()];
	}

	public void setMaxPlayedLevel(Difficulty dclty, int maxPlayedLevel) {
		this.maxPlayedLevel[dclty.ordinal()] = maxPlayedLevel;
	}

	public ArrayList<LevelScore> getScores(Difficulty dclty) {
		return scores[dclty.ordinal()];
	}

	public void setScores(Difficulty dclty, ArrayList<LevelScore> scores) {
		this.scores[dclty.ordinal()] = scores;
	}

	public void addScores(Difficulty dclty, LevelScore score) {
		scores[dclty.ordinal()].add(score);
	}

	public void updateScores(Difficulty dclty, LevelScore score) {
		scores[dclty.ordinal()].set(score.getLevelNo()-1, score);
	}

	public LevelScore getScores(Difficulty dclty, int levelNo) {
		if (levelNo < scores[dclty.ordinal()].size())
			return scores[dclty.ordinal()].get(levelNo);
		else
			return new LevelScore();
	}
}
