package com.avy.cflag.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import java.util.Random;

import com.avy.cflag.base.BaseScreen;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.graphics.Cloud;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class BackScreen extends BaseScreen {

	protected TextureAtlas commonAtlas;

	protected Image argbFull;
	protected Image argbMid;

	protected Image cloudImage1;
	protected Image cloudImage2;

	protected Image topBar;
	protected Image midBar;
	protected Image bottomBar;

	public BackScreen(CFlagGame game, boolean graphicsOn, boolean spritesOn, boolean shapesOn) {
		super(game, graphicsOn, spritesOn, shapesOn);

		commonAtlas = g.createImageAtlas("common");
	}

	@Override
	public void show() {
		super.show();

		final Random rand = new Random();
		final float cloudPosY = rand.nextInt(167) - 87;
		final int cloudAnimDirection = rand.nextInt(2);

		g.setImageAtlas(commonAtlas);

		argbFull = new Image(g.getFlipTexRegion("argbblack"));
		argbFull.setPosition(0, 0);
		argbFull.setSize(game.getSrcWidth(), game.getSrcHeight());

		topBar = new Image(g.getFlipTexRegion("topbar"));
		midBar = new Image(g.getFlipTexRegion("midbar"));
		argbMid = new Image(g.getFlipTexRegion("argbblack"));
		bottomBar = new Image(g.getFlipTexRegion("bottombar"));

		topBar.setPosition(0, 0);
		argbMid.setPosition(0, topBar.getHeight());
		argbMid.setSize(game.getSrcWidth(), game.getSrcHeight() - (bottomBar.getHeight() + bottomBar.getHeight()));
		argbMid.getColor().a = 0.5f;
		bottomBar.setPosition(0, game.getSrcHeight() - bottomBar.getHeight());
		midBar.setPosition(0, bottomBar.getY() - midBar.getHeight());

		cloudImage1 = new Image(g.getFlipTexRegion("cloud"));
		cloudImage1.setPosition(0, cloudPosY);

		cloudImage2 = new Image(g.getFlipTexRegion("cloud"));
		cloudImage2.setPosition(cloudImage1.getWidth(), cloudPosY);

		argbFull.addAction(sequence(fadeOut(1f), visible(false)));

		cloudImage1.addAction(new Cloud(cloudImage1, cloudAnimDirection));
		cloudImage2.addAction(new Cloud(cloudImage2, cloudAnimDirection));

		stage.addActor(cloudImage1);
		stage.addActor(cloudImage2);
		stage.addActor(topBar);
		stage.addActor(midBar);
		stage.addActor(bottomBar);
		stage.addActor(argbMid);
	}

	@Override
	public void dispose() {
		if (commonAtlas != null) {
			commonAtlas.dispose();
		}
		super.dispose();
	}

}
