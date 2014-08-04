package com.avy.cflag.game.utils;

import java.util.ArrayList;


public class UserScore {
	private int currentLevel;
	private int maxPlayedLevel;
	private ArrayList<LevelScore> scores;
	
	public UserScore() {
		currentLevel=0;
		maxPlayedLevel=0;
		scores = new ArrayList<LevelScore>();
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public int getMaxPlayedLevel() {
		return maxPlayedLevel;
	}

	public void setMaxPlayedLevel(int maxPlayedLevel) {
		this.maxPlayedLevel = maxPlayedLevel;
	}

	public ArrayList<LevelScore> getScores() {
		return scores;
	}
	public void setScores(ArrayList<LevelScore> scores) {
		this.scores = scores;
	}
	
	public void addScores(LevelScore score){
		scores.add(score);
	}

	public void updateScores(LevelScore score){
		scores.set(score.getLevelNo()-1, score);
	}
	
	public LevelScore getScores(int levelNo){
		if(levelNo<scores.size())
			return scores.get(levelNo);
		else
			return null;
	}
}


