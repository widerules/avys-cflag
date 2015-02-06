package com.avy.cflag.base;

import com.avy.cflag.game.CFlagGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class BaseScreen implements Screen {

	public final CFlagGame game;
	protected final Stage stage;
	protected Graphics g;
	protected SpriteBatch batch;
	protected ShapeRenderer sr;

	public BaseScreen(final CFlagGame game) {
		this.game = game;
		stage = new Stage(new StretchViewport(game.getSrcWidth(), game.getSrcHeight(), game.getCamera()));
		batch = null;
		sr = null;
		g = null;
	}

	public BaseScreen(final CFlagGame game, final boolean graphicsOn, final boolean spritesOn, final boolean shapesOn) {
		this(game);
		if (graphicsOn) {
			g = new Graphics();
		}
		if (graphicsOn && spritesOn) {
			batch = new SpriteBatch();
			batch.setProjectionMatrix(game.getCamera().combined);
		}
		if (graphicsOn && shapesOn) {
			sr = new ShapeRenderer();
			sr.setProjectionMatrix(game.getCamera().combined);
			g.setSr(sr);
		}
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void resize(final int width, final int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render(final float delta) {
		stage.act(delta);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// Do Nothing
	}

	@Override
	public void resume() {
		// Do Nothing
	}

	@Override
	public void dispose() {
		stage.dispose();
		if (batch != null) {
			batch.dispose();
		}
		if (sr != null) {
			sr.dispose();
		}
	}
}
