package com.avy.cflag.game.utils;

import com.avy.cflag.game.MemStore.GameState;
import com.avy.cflag.game.elements.LTank;
import com.avy.cflag.game.elements.Level;
import com.avy.cflag.game.elements.Platform;

public class GameData {
	private int currentLevel;
	private GameState gameState;
	private Level lvl;
//	private Platform pltfrm;
	private LTank ltank;
	private LTank undoList[];
	private int undoCnt;
	private boolean hintUsed;
	private boolean stateChanged;

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Level getLvl() {
		return lvl;
	}

	public void setLvl(Level lvl) {
		this.lvl = lvl;
	}

//	public Platform getPltfrm() {
//		return pltfrm;
//	}
//
//	public void setPltfrm(Platform pltfrm) {
//		this.pltfrm = pltfrm;
//	}
//
	public LTank getLtank() {
		return ltank;
	}

	public void setLtank(LTank ltank) {
		this.ltank = ltank;
	}

	public LTank[] getUndoList() {
		return undoList;
	}

	public void setUndoList(LTank[] undoList) {
		this.undoList = undoList;
	}

	public int getUndoCnt() {
		return undoCnt;
	}

	public void setUndoCnt(int undoCnt) {
		this.undoCnt = undoCnt;
	}

	public boolean isHintUsed() {
		return hintUsed;
	}

	public void setHintUsed(boolean hintUsed) {
		this.hintUsed = hintUsed;
	}

	public boolean isStateChanged() {
		return stateChanged;
	}

	public void setStateChanged(boolean stateChanged) {
		this.stateChanged = stateChanged;
	}

}
