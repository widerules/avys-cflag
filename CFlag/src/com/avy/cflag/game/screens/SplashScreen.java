package com.avy.cflag.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.avy.cflag.base.BaseScreen;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.Utils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SplashScreen extends BaseScreen {
	private TextureAtlas imageAtlas;
	private Image splashImage;
	private Image titleImage;
	private boolean isRunning;

	public SplashScreen(CFlagGame game) {
		super(game, true, false, false);
		isRunning = true;
		imageAtlas = g.createImageAtlas("splash");
	}

	@Override
	public void show() {
		super.show();

		g.setImageAtlas(imageAtlas);
		splashImage = new Image(g.getFlipTexRegion("background"));
		splashImage.rotateBy(90);
		splashImage.setScaleY(game.getSrcWidth());
		splashImage.setPosition(game.getSrcWidth(), 0);
		stage.addActor(splashImage);

		titleImage = new Image(g.getFlipTexRegion("title"));
		titleImage.setPosition((game.getSrcWidth() - titleImage.getWidth()) / 2, (game.getSrcHeight() - titleImage.getHeight()) / 2);
		stage.addActor(titleImage);
		stage.addAction(sequence(alpha(0), fadeIn(2f), new Action() {
			@Override
			public boolean act(float delta) {
				Utils.loadLevelData();
				Utils.loadGameOptions();
				Utils.loadUserScores();
				Utils.loadGameAudio();
				return true;
			}
		}, new Action() {
			@Override
			public boolean act(float delta) {
				isRunning = false;
				return true;
			}
		}));

		stage.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					game.exitGame();
				}
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (!isRunning) {
			game.setScreen(new MenuScreen(game));
		}
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		super.dispose();
		if (imageAtlas != null)
			imageAtlas.dispose();
		splashImage = null;
		titleImage = null;
	}
}
