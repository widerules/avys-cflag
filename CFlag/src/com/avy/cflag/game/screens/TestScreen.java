package com.avy.cflag.game.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author Tony Pottier
 * 
 */
public class TestScreen implements ApplicationListener, InputProcessor {

	private SpriteBatch spriteBatch;
	private BitmapFont bitmapFont;
	private OrthographicCamera cam2d;
	private PerspectiveCamera cam3d;
	private int WIDTH, HEIGHT;

	private final float scrollSpeed = 1.0f; // unit per second

	private String text = "Episode IV\n\nA NEW HOPE\n\nIt is a period of civil war.\nRebel spaceships, striking\nfrom a hidden base, have\nwon their first victory\nagainst the evil Galactic\nEmpire.\n\nDuring the battle, Rebel\nspies managed to steal\nsecret plans to the Empire’s\nultimate weapon, the\nDEATH STAR, an armored\nspace station with enough\npower to destroy an entire\nplanet.\n\nPursued by the Empire's\nsinister agents, Princess\nLeia races home aboard her\nstarship, custodian of the\nstolen plans that can save\nher people and restore\nfreedom to the galaxy....";

	private Texture square;

	@Override
	public void create() {

		spriteBatch = new SpriteBatch();
		bitmapFont = new BitmapFont();
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bitmapFont.setUseIntegerPositions(false);
		bitmapFont.setScale(.05f);
		bitmapFont.setColor(Color.YELLOW);
		Gdx.input.setInputProcessor(this);

	}

	@Override
	public void resize(int width, int height) {

		WIDTH = width;
		HEIGHT = height;

		// define an ortho camera 10 unit wide with height depending on aspect
		// ratio
		float camWidth = 10.0f;
		float camHeight = camWidth * (float) HEIGHT / (float) WIDTH;
		cam2d = new OrthographicCamera(camWidth, camHeight);
		cam2d.position.set(camWidth / 2.0f, camHeight / 2.0f, 0.0f);
		cam2d.update();

		// define the perspective camera
		cam3d = new PerspectiveCamera(90.0f, camWidth, camHeight);
		cam3d.translate(0.0f, -10.0f, 3.0f);
		cam3d.lookAt(0.0f, 0.0f, 0.0f);
		cam3d.update(true);

	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();

		cam3d.translate(0.0f, -dt * scrollSpeed, 0.0f);
		cam3d.update(false);

		GL20 gl = Gdx.graphics.getGL20();
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.setProjectionMatrix(cam3d.combined);
		spriteBatch.begin();
		bitmapFont.drawMultiLine(spriteBatch, text, -cam3d.viewportWidth / 2f, -cam3d.viewportHeight, cam3d.viewportWidth, HAlignment.CENTER);
		spriteBatch.end();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
