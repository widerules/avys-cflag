package com.avy.cflag.game;

import com.avy.cflag.base.ErrorHandler;
import com.avy.cflag.base.Musics;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.game.screens.SplashScreen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CFlagGame extends Game {

	public static String packageName;

	private final float srcWidth = 800;
	private final float srcHeight = 480;

	private final ErrorHandler eh = new ErrorHandler();
	private OrthographicCamera camera;

	public CFlagGame() {
		packageName = this.getClass().getPackage().getName();
	}

	@Override
	public void create() {
		Thread.setDefaultUncaughtExceptionHandler(eh);
		Gdx.app.setLogLevel(Application.LOG_INFO);

		camera = new OrthographicCamera(srcWidth, srcHeight);
		camera.setToOrtho(true);

		setScreen(new SplashScreen(this));
	}

	public float getSrcWidth() {
		return srcWidth;
	}

	public float getSrcHeight() {
		return srcHeight;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void exitGame() {
		Musics.dispose();
		Sounds.dispose();
		Gdx.app.exit();
	}
}
