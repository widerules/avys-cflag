package com.avy.cflag.game.utils;

import com.avy.cflag.game.GameUtils;

public class SaveGame implements Runnable {
	private static boolean isRunning = false;
	private GameData gData;
	Thread saveThread = null;

	public SaveGame(final GameData gData) {
		this.gData=gData;
	}

	public void start() {
		saveThread = new Thread(this);
		saveThread.start();
	}

	@Override
	public void run() {
		if(!isRunning) {
			isRunning=true;
			GameUtils.saveGame(gData);
			GameUtils.saveGameOptions();
			isRunning=false;
		}
	}

}
