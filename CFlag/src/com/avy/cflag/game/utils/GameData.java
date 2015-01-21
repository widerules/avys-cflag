package com.avy.cflag.game.utils;

import com.avy.cflag.game.EnumStore.Difficulty;
import com.avy.cflag.game.EnumStore.GameState;
import com.avy.cflag.game.elements.Hero;
import com.avy.cflag.game.elements.Level;

public class GameData {
	private Difficulty currentDclty;
	private int currentLevel;
	private GameState gameState;
	private Level lvl;
	private Hero ltank;
	private UnDoData undoList[];
	private int undoCnt;
	private boolean hintUsed;
	private boolean stateChanged;

	public Difficulty getCurrentDclty() {
		return currentDclty;
	}

	public void setCurrentDclty(final Difficulty currentDclty) {
		this.currentDclty = currentDclty;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(final int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(final GameState gameState) {
		this.gameState = gameState;
	}

	public Level getLvl() {
		return lvl;
	}

	public void setLvl(final Level lvl) {
		this.lvl = lvl;
	}

	public Hero getLtank() {
		return ltank;
	}

	public void setLtank(final Hero ltank) {
		this.ltank = ltank;
	}

	public UnDoData[] getUndoList() {
		return undoList;
	}

	public void setUndoList(final UnDoData[] undoList) {
		this.undoList = undoList;
	}

	public int getUndoCnt() {
		return undoCnt;
	}

	public void setUndoCnt(final int undoCnt) {
		this.undoCnt = undoCnt;
	}

	public boolean isHintUsed() {
		return hintUsed;
	}

	public void setHintUsed(final boolean hintUsed) {
		this.hintUsed = hintUsed;
	}

	public boolean isStateChanged() {
		return stateChanged;
	}

	public void setStateChanged(final boolean stateChanged) {
		this.stateChanged = stateChanged;
	}

}
