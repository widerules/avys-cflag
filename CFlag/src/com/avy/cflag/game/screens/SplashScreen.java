package com.avy.cflag.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.avy.cflag.base.BaseScreen;
import com.avy.cflag.base.TouchListener;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.GameUtils;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SplashScreen extends BaseScreen {
	private final TextureAtlas imageAtlas;
	private Image splashImage;
	private Image titleImage;

	public SplashScreen(final CFlagGame game) {
		super(game, true, false, false);
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
		titleImage = new Image(g.getFlipTexRegion("title"));
		titleImage.setPosition((game.getSrcWidth() - titleImage.getWidth()) / 2, (game.getSrcHeight() - titleImage.getHeight()) / 2);

		stage.addActor(splashImage);
		stage.addActor(titleImage);

		stage.addAction(sequence(alpha(0), fadeIn(2f), new Action() {
			@Override
			public boolean act(final float delta) {
				GameUtils.loadUserOptions();
				GameUtils.loadGameAudio();
				GameUtils.loadUserScores();
				GameUtils.loadLevelData();
				GameUtils.loadAcraMap();
				GameUtils.loadThumbs();
				return true;
			}
		}, new Action() {
			@Override
			public boolean act(final float delta) {
				game.setScreen(new MenuScreen(game));
				return false;
			}
		}));

		stage.addListener(new TouchListener() {
			@Override
			public boolean keyDown(final InputEvent event, final int keycode) {
				if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
					game.exitGame();
				}
				return true;
			}
		});
	}

	@Override
	public void dispose() {
		if (imageAtlas != null) {
			imageAtlas.dispose();
		}
		splashImage = null;
		titleImage = null;
		super.dispose();
	}
}
