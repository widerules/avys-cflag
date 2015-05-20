package com.avy.cflag.game.screens;

import static com.avy.cflag.game.Constants.GMAIL_LINK;
import static com.avy.cflag.game.Constants.TWITTER_LINK;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import java.util.Random;

import com.avy.cflag.base.Graphics;
import com.avy.cflag.base.TouchListener;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.graphics.Cloud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class AboutScreen implements Screen {

	private final CFlagGame game;
	private final Graphics g;

	private final TextureAtlas commonAtlas;
	private final TextureAtlas aboutAtlas;

	private final Stage stageForCloud;
	private Image argbMid;
	private Image cloudImage1;
	private Image cloudImage2;

	private final Stage stageForBars;
	private Image argbFull;
	private Image topBar;
	private Image midBar;
	private Image bottomBar;

	private Image titleStr;
	private Image backStr;
	private Image backButtonUp;
	private Image backButtonDown;
	private Group backButtonGroup;
	private Image twitUs;
	private Image writeUs;

	private final SpriteBatch scrollBatch;
	private PerspectiveCamera scrollCam;
	private final float scrollSpeed;
	private boolean render;

	public AboutScreen(final CFlagGame game) {
		this.game = game;
		g = new Graphics();

		commonAtlas = g.createImageAtlas("common");
		aboutAtlas = g.createImageAtlas("about");

		stageForCloud = new Stage(new StretchViewport(game.getSrcWidth(), game.getSrcHeight(), game.getCamera()));
		stageForBars = new Stage(new StretchViewport(game.getSrcWidth(), game.getSrcHeight(), game.getCamera()));

		scrollBatch = new SpriteBatch();
		scrollSpeed = 0.5f;
		render = true;
	}

	@Override
	public void show() {
		scrollCam = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		scrollCam.position.set(-0.0045920946f, -8.479108f, 7.6459165f);
		scrollCam.lookAt(0.0f, 0.0f, 0.0f);
		scrollCam.near = 0.1f;
		scrollCam.far = 300f;
		scrollCam.update(true);

		final Random rand = new Random();
		final float cloudPosY = rand.nextInt(167) - 87;
		final int cloudAnimDirection = rand.nextInt(2);

		g.setImageAtlas(commonAtlas);

		argbFull = new Image(g.getFlipYTexRegion("argbblack"));
		argbFull.setPosition(0, 0);
		argbFull.setSize(game.getSrcWidth(), game.getSrcHeight());

		topBar = new Image(g.getFlipYTexRegion("topbar"));
		midBar = new Image(g.getFlipYTexRegion("midbar"));
		argbMid = new Image(g.getFlipYTexRegion("argbblack"));
		bottomBar = new Image(g.getFlipYTexRegion("bottombar"));

		topBar.setPosition(0, 0);
		argbMid.setPosition(0, topBar.getHeight());
		argbMid.setSize(game.getSrcWidth(), game.getSrcHeight() - (bottomBar.getHeight() + bottomBar.getHeight()));
		argbMid.getColor().a = 0.5f;
		bottomBar.setPosition(0, game.getSrcHeight() - bottomBar.getHeight());
		midBar.setPosition(0, bottomBar.getY() - midBar.getHeight());

		cloudImage1 = new Image(g.getFlipYTexRegion("cloud"));
		cloudImage1.setPosition(0, cloudPosY);

		cloudImage2 = new Image(g.getFlipYTexRegion("cloud"));
		cloudImage2.setPosition(cloudImage1.getWidth(), cloudPosY);

		argbFull.addAction(sequence(fadeOut(1f), visible(false)));

		cloudImage1.addAction(new Cloud(cloudImage1, cloudAnimDirection));
		cloudImage2.addAction(new Cloud(cloudImage2, cloudAnimDirection));

		backButtonUp = new Image(g.getFlipYTexRegion("1_rectbuttonup"));
		backButtonDown = new Image(g.getFlipYTexRegion("1_rectbuttondown"));

		g.setImageAtlas(aboutAtlas);
		titleStr = new Image(g.getFlipYTexRegion("title"));
		titleStr.setPosition((topBar.getWidth() - titleStr.getWidth()) / 2, (topBar.getHeight() - titleStr.getHeight()) / 2);
		backStr = new Image(g.getFlipYTexRegion("back"));

		backButtonUp.setPosition((bottomBar.getWidth() - backButtonUp.getWidth()) / 2, bottomBar.getY() + ((bottomBar.getHeight() - backButtonUp.getHeight()) / 2));
		backButtonDown.setPosition(backButtonUp.getX(), backButtonUp.getY());
		backButtonDown.setVisible(false);
		backStr.setPosition(backButtonUp.getX(), backButtonUp.getY());

		backButtonGroup = new Group();
		backButtonGroup.addActor(backButtonUp);
		backButtonGroup.addActor(backButtonDown);
		backButtonGroup.addActor(backStr);
		writeUs = new Image(g.getFlipYTexRegion("writeus"));
		twitUs = new Image(g.getFlipYTexRegion("twitus"));
		writeUs.setPosition(25, bottomBar.getY() + ((bottomBar.getHeight() - writeUs.getHeight()) / 2));
		twitUs.setPosition(775 - twitUs.getWidth(), writeUs.getY());

		stageForCloud.addActor(cloudImage1);
		stageForCloud.addActor(cloudImage2);
		stageForCloud.addActor(midBar);
		stageForCloud.addActor(argbMid);

		stageForBars.addActor(topBar);
		stageForBars.addActor(titleStr);
		stageForBars.addActor(bottomBar);
		stageForBars.addActor(backButtonGroup);
		stageForBars.addActor(writeUs);
		stageForBars.addActor(twitUs);
		stageForBars.addActor(argbFull);

		twitUs.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				Gdx.net.openURI(TWITTER_LINK);
			}
		});

		writeUs.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				Gdx.net.openURI(GMAIL_LINK);
			}
		});

		backButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				backButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				backButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new MenuScreen(game));
					}
				})));
			}
		});

		stageForBars.addListener(new DragListener() {
			@Override
			public boolean keyDown(final InputEvent event, final int keycode) {
				if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
					argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
						@Override
						public void run() {
							render = false;
							game.setScreen(new MenuScreen(game));
						}
					})));
				}
				return true;
			}
		});

		// Gdx.input.setInputProcessor(new CameraInputController(scrollCam));
		Gdx.input.setInputProcessor(stageForBars);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void render(final float delta) {
		if (render) {
			scrollCam.translate(0.0f, -delta * scrollSpeed, 0.0f);
			scrollCam.update(false);

			stageForCloud.act(delta);
			stageForBars.act(delta);
			Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			stageForCloud.draw();
			scrollBatch.setProjectionMatrix(scrollCam.combined);
			scrollBatch.begin();
			scrollBatch.draw(g.getTexRegion("aboutstr"), -7.5f, -29.7f, 15f, 25f);
			scrollBatch.end();
			stageForBars.draw();
		}
	}

	@Override
	public void dispose() {
		if (scrollBatch != null) {
			scrollBatch.dispose();
		}
		if (aboutAtlas != null) {
			aboutAtlas.dispose();
		}
		if (commonAtlas != null) {
			commonAtlas.dispose();
		}
		stageForCloud.dispose();
		stageForBars.dispose();
	}

	@Override
	public void resize(final int width, final int height) {
		stageForCloud.getViewport().update(width, height, true);
		stageForBars.getViewport().update(width, height, true);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
