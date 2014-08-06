package com.avy.cflag.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.base.AnimActor;
import com.avy.cflag.base.AnimDrawable;
import com.avy.cflag.base.Point;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.MemStore;
import com.avy.cflag.game.Utils;
import com.avy.cflag.game.graphics.MenuHero;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MenuScreen extends BackScreen {

	private final TextureAtlas menuAtlas;

	private Image menuTopBar;
	private AnimActor newGame;
	private AnimActor resume;
	private AnimActor levelselect;
	private AnimActor options;
	private AnimActor help;
	private AnimActor quit;

	private Image flagPole;
	private AnimActor flag;

	private Image versionStr;
	private Image avyStudiosStr;

	private Image hero;

	public boolean ngamePressed = false;
	public boolean lselectPressed = false;
	public boolean resumePressed = false;
	public boolean optionsPressed = false;
	public boolean helpPressed = false;
	public boolean quitPressed = false;

	public MenuScreen(CFlagGame game) {
		super(game, true, false, false);
		menuAtlas = g.createImageAtlas("menu");
	}

	@Override
	public void show() {
		super.show();

		topBar.setVisible(false);
		argbMid.setVisible(false);

		g.setImageAtlas(menuAtlas);
		menuTopBar = new Image(g.getFlipTexRegion("topbar"));
		newGame = new AnimActor(new AnimDrawable(new Animation(0.24f, g.getFlipTexRegions("newgame"), PlayMode.LOOP_PINGPONG)));
		resume = new AnimActor(new AnimDrawable(new Animation(0.25f, g.getFlipTexRegions("resume"), PlayMode.LOOP_PINGPONG)));
		levelselect = new AnimActor(new AnimDrawable(new Animation(0.26f, g.getFlipTexRegions("levelselect"), PlayMode.LOOP_PINGPONG)));
		options = new AnimActor(new AnimDrawable(new Animation(0.21f, g.getFlipTexRegions("options"), PlayMode.LOOP_PINGPONG)));
		help = new AnimActor(new AnimDrawable(new Animation(0.22f, g.getFlipTexRegions("help"), PlayMode.LOOP_PINGPONG)));
		quit = new AnimActor(new AnimDrawable(new Animation(0.23f, g.getFlipTexRegions("quit"), PlayMode.LOOP_PINGPONG)));
		flagPole = new Image(g.getFlipTexRegion("flagpole"));
		flag = new AnimActor(new AnimDrawable(new Animation(0.1f, g.getFlipTexRegions("flag"), PlayMode.LOOP)));
		versionStr = new Image(g.getFlipTexRegion("version"));
		avyStudiosStr = new Image(g.getFlipTexRegion("avystudios"));
		hero = new Image(g.getFlipTexRegion("hero"));

		menuTopBar.setPosition(0, 0);
		newGame.setPosition(20, 63);
		resume.setPosition(20, 63);
		levelselect.setPosition(190, 63);
		options.setPosition(337, 66);
		help.setPosition(497, 67);
		quit.setPosition(639, 57);
		flagPole.setPosition(162, 227);
		flag.setPosition(171, 226);
		versionStr.setPosition((bottomBar.getWidth() - versionStr.getWidth()) / 2, bottomBar.getY() + 20);
		avyStudiosStr.setPosition((bottomBar.getWidth() - avyStudiosStr.getWidth()) / 2, bottomBar.getY() + 40);
		hero.setPosition(0, 354);
		hero.setOrigin(hero.getWidth() / 2, hero.getHeight() / 2);

		if (MemStore.gameOPTS.isFirstRun()) {
			resume.setVisible(false);
		} else {
			newGame.setVisible(false);
		}

		hero.addAction(new MenuHero(hero));

		stage.addActor(menuTopBar);
		stage.addActor(newGame);
		stage.addActor(resume);
		stage.addActor(levelselect);
		stage.addActor(options);
		stage.addActor(help);
		stage.addActor(quit);
		stage.addActor(flag);
		stage.addActor(flagPole);
		stage.addActor(versionStr);
		stage.addActor(avyStudiosStr);
		stage.addActor(hero);
		stage.addActor(argbFull);

		stage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (Utils.inBoundsRect(new Point((int) x, (int) y), 28, 225, 156, 190, 166, 242, 40, 275)) {
					if (MemStore.gameOPTS.isFirstRun()) {
						ngamePressed = true;
					} else {
						resumePressed = true;
					}
				} else if (Utils.inBoundsRect(new Point((int) x, (int) y), 199, 151, 328, 145, 329, 196, 201, 205)) {
					lselectPressed = true;
				} else if (Utils.inBoundsRect(new Point((int) x, (int) y), 348, 218, 478, 219, 474, 273, 346, 270)) {
					optionsPressed = true;
				} else if (Utils.inBoundsRect(new Point((int) x, (int) y), 500, 163, 632, 146, 637, 198, 508, 213)) {
					helpPressed = true;
				} else if (Utils.inBoundsRect(new Point((int) x, (int) y), 655, 239, 779, 262, 770, 310, 646, 286)) {
					quitPressed = true;
				}
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (ngamePressed) {
					ngamePressed = false;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
							game.setScreen(new HelpScreen(game,"firstplay"));
							return false;
						}
					}));
				} else if (resumePressed) {
					resumePressed = false;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
							game.setScreen(new PlayScreen(game,true));
							return false;
						}
					}));
				} else if (lselectPressed) {
					lselectPressed = false;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
							game.setScreen(new LevelScreen(game));
							return false;
						}
					}));
				} else if (optionsPressed) {
					optionsPressed = false;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
							game.setScreen(new OptionsScreen(game));
							return false;
						}
					}));
				} else if (helpPressed) {
					helpPressed = false;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
							game.setScreen(new HelpScreen(game,"help"));
							return false;
						}
					}));
				} else if (quitPressed) {
					quitPressed = false;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
							Gdx.app.exit();
							return false;
						}
					}));

				}
			}

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.BACK) {
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
							Gdx.app.exit();
							return false;
						}
					}));
				}
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		super.render(delta);
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
		if (menuAtlas != null) {
			menuAtlas.dispose();
		}
	}
}
