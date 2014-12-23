package com.avy.cflag.desktop;

import com.avy.cflag.base.AcraMap;
import com.avy.cflag.game.CFlagGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class CFlagDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "CaptureTheFlag";
		cfg.width = 800;
		cfg.height = 480;
		cfg.resizable = false;
		new LwjglApplication(new CFlagGame(new AcraMap() {
			
			@Override
			public void putCustomData(String key, String value) {
				// Do Nothing - As this is just a interface for Android Acra Map				
			}
		}), cfg);
	}
}
