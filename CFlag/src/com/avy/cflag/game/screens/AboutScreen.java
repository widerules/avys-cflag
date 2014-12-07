package com.avy.cflag.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import java.util.Random;

import com.avy.cflag.base.Graphics;
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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class AboutScreen implements Screen {
	
	private CFlagGame game;
	private Graphics g;
	
	private TextureAtlas commonAtlas;
	private TextureAtlas aboutAtlas;
	
	private Stage stageForCloud;
	private Image argbMid;
	private Image cloudImage1;
	private Image cloudImage2;

	private Stage stageForBars;
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

	private SpriteBatch scrollBatch;
	private PerspectiveCamera scrollCam;
	private final float scrollSpeed;
	private boolean render;

	public AboutScreen(CFlagGame game) {
		this.game = game;
		g= new Graphics();
		
		commonAtlas = g.createImageAtlas("common");
		aboutAtlas = g.createImageAtlas("about");
		
		stageForCloud  = new Stage(new StretchViewport(game.getSrcWidth(), game.getSrcHeight(), game.getCamera()));
		stageForBars  = new Stage(new StretchViewport(game.getSrcWidth(), game.getSrcHeight(), game.getCamera()));
		
		scrollBatch = new SpriteBatch();
		scrollSpeed = 0.5f;
		render = true;
	}

	@Override
	public void show() {
		scrollCam = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		scrollCam.position.set(-0.0045920946f,-8.479108f,7.6459165f);
		scrollCam.lookAt(0.0f, 0.0f, 0.0f);
		scrollCam.near=0.1f;
		scrollCam.far=300f;
		scrollCam.update(true);
	      
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
		
		backButtonUp = new Image(g.getFlipTexRegion("rectbuttonup"));
		backButtonDown = new Image(g.getFlipTexRegion("rectbuttondown"));
		
		g.setImageAtlas(aboutAtlas);
		titleStr = new Image(g.getFlipTexRegion("ctf"));
		titleStr.setPosition((topBar.getWidth() - titleStr.getWidth()) / 2, (topBar.getHeight() - titleStr.getHeight()) / 2);
		backStr = new Image(g.getFlipTexRegion("back"));
		
		backButtonUp.setPosition((bottomBar.getWidth() - backButtonUp.getWidth()) / 2, bottomBar.getY() + (bottomBar.getHeight() - backButtonUp.getHeight()) / 2);
		backButtonDown.setPosition(backButtonUp.getX(), backButtonUp.getY());
		backButtonDown.setVisible(false);
		backStr.setPosition(backButtonUp.getX(), backButtonUp.getY());
		
		backButtonGroup = new Group();
		backButtonGroup.addActor(backButtonUp);
		backButtonGroup.addActor(backButtonDown);
		backButtonGroup.addActor(backStr);
		writeUs = new Image(g.getFlipTexRegion("writeus"));
		twitUs = new Image(g.getFlipTexRegion("twitus"));
		writeUs.setPosition(25, bottomBar.getY()+(bottomBar.getHeight()-writeUs.getHeight())/2);
		twitUs.setPosition(775-twitUs.getWidth(), writeUs.getY());
		
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

		twitUs.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.net.openURI("https://twitter.com/intent/tweet?screen_name=rssindian");
			}
		});

		writeUs.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.net.openURI("mailto:rssindian@gmail.com");
			}
		});

		backButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				backButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
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
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
						@Override
						public void run() {
							render=false;
							game.setScreen(new MenuScreen(game));
						}
					})));
				}
				return true;
			}
		});
		
//		Gdx.input.setInputProcessor(new CameraInputController(scrollCam));
		Gdx.input.setInputProcessor(stageForBars);
		Gdx.input.setCatchBackKey(true);
	}
	
	@Override
	public void render(float delta) {
		if(render){
		scrollCam.translate(0.0f, -delta * scrollSpeed, 0.0f);
		scrollCam.update(false);
	 		
		stageForCloud.act(delta);
		stageForBars.act(delta);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stageForCloud.draw();
		scrollBatch.setProjectionMatrix(scrollCam.combined);
		scrollBatch.begin();
		scrollBatch.draw(g.getTexRegion("aboutstr"),-7.5f, -29.7f, 15f, 25f);
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
	public void resize(int width, int height) {
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
