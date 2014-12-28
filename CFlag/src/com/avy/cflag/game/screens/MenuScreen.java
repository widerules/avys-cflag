package com.avy.cflag.game.screens;

import static com.avy.cflag.game.Constants.FB_LINK;
import static com.avy.cflag.game.Constants.PLAYSTORE_LINK;
import static com.avy.cflag.game.MemStore.curUserOPTS;
import static com.avy.cflag.game.MemStore.curUserSCORE;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.base.AnimActor;
import com.avy.cflag.base.AnimDrawable;
import com.avy.cflag.base.Point;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.base.TouchListener;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.PlayUtils;
import com.avy.cflag.game.graphics.MenuHeroAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
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

	private Image avyStudiosStr;
	private Image rateUs;
	private Image likeUs;

	private AnimActor hero;
	private Image bullet;

	public boolean ngamePressed = false;
	public boolean lselectPressed = false;
	public boolean resumePressed = false;
	public boolean optionsPressed = false;
	public boolean helpPressed = false;
	public boolean quitPressed = false;
	public boolean touchActive = false;

	public ParticleEffect explodeEffect;
	public boolean explodeOn = false;

	public MenuScreen(final CFlagGame game) {
		super(game, true, true, false);
		menuAtlas = g.createImageAtlas("menu");
	}

	@Override
	public void show() {
		super.show();

		topBar.setVisible(false);
		argbMid.setVisible(false);

		g.setImageAtlas(menuAtlas);

		menuTopBar = new Image(g.getFlipTexRegion("topbar"));
		flagPole = new Image(g.getFlipTexRegion("flagpole"));
		avyStudiosStr = new Image(g.getFlipTexRegion("avystudios"));
		bullet = new Image(g.getFlipTexRegion("bullet"));
		rateUs = new Image(g.getFlipTexRegion("rateus"));
		likeUs = new Image(g.getFlipTexRegion("likeus"));

		newGame = new AnimActor(new AnimDrawable(new Animation(0.24f, g.getFlipTexRegions("newgame"), PlayMode.LOOP_PINGPONG)));
		resume = new AnimActor(new AnimDrawable(new Animation(0.25f, g.getFlipTexRegions("resume"), PlayMode.LOOP_PINGPONG)));
		levelselect = new AnimActor(new AnimDrawable(new Animation(0.26f, g.getFlipTexRegions("levelselect"), PlayMode.LOOP_PINGPONG)));
		options = new AnimActor(new AnimDrawable(new Animation(0.21f, g.getFlipTexRegions("options"), PlayMode.LOOP_PINGPONG)));
		help = new AnimActor(new AnimDrawable(new Animation(0.22f, g.getFlipTexRegions("help"), PlayMode.LOOP_PINGPONG)));
		quit = new AnimActor(new AnimDrawable(new Animation(0.23f, g.getFlipTexRegions("quit"), PlayMode.LOOP_PINGPONG)));
		flag = new AnimActor(new AnimDrawable(new Animation(0.1f, g.getFlipTexRegions("flag"), PlayMode.LOOP)));
		hero = new AnimActor(new AnimDrawable(new Animation(0.24f, g.getFlipTexRegions("hero"), PlayMode.LOOP)));

		menuTopBar.setPosition(0, 0);
		newGame.setPosition(20, 63);
		resume.setPosition(20, 63);
		levelselect.setPosition(190, 63);
		options.setPosition(333, 68);
		help.setPosition(497, 68);
		quit.setPosition(633, 64);
		flagPole.setPosition(162, 227);
		flag.setPosition(171, 226);
		avyStudiosStr.setPosition((bottomBar.getWidth() - avyStudiosStr.getWidth()) / 2, bottomBar.getY() + ((bottomBar.getHeight() - avyStudiosStr.getHeight()) / 2));
		rateUs.setPosition(25, (bottomBar.getY() + ((bottomBar.getHeight() - rateUs.getHeight()) / 2)) - 3);
		likeUs.setPosition(775 - likeUs.getWidth(), rateUs.getY());
		hero.setPosition(0, 354);
		hero.setOrigin(hero.getWidth() / 2, hero.getHeight() / 2);

		bullet.setVisible(false);

		if (curUserOPTS.isFirstRun()) {
			resume.setVisible(false);
		} else {
			newGame.setVisible(false);
		}

		hero.addAction(new MenuHeroAction(hero, 0));

		explodeEffect = new ParticleEffect();
		explodeEffect.load(Gdx.files.internal("atlas/explosion.p"), menuAtlas);
		explodeEffect.setPosition(200, 100);
		explodeEffect.start();

		stage.addActor(menuTopBar);
		stage.addActor(newGame);
		stage.addActor(resume);
		stage.addActor(levelselect);
		stage.addActor(options);
		stage.addActor(help);
		stage.addActor(quit);
		stage.addActor(flag);
		stage.addActor(flagPole);
		stage.addActor(avyStudiosStr);
		stage.addActor(rateUs);
		stage.addActor(likeUs);
		stage.addActor(hero);
		stage.addActor(bullet);
		stage.addActor(argbFull);

		avyStudiosStr.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new AboutScreen(game));
					}
				})));
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		likeUs.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				Gdx.net.openURI(FB_LINK);
			}
		});

		rateUs.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				Gdx.net.openURI(PLAYSTORE_LINK);
			}
		});

		stage.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				if (!touchActive) {
					touchActive = true;
					if (PlayUtils.inBoundsRect(new Point((int) x, (int) y), 28, 225, 156, 190, 166, 242, 40, 275)) {
						if (curUserOPTS.isFirstRun()) {
							ngamePressed = true;
						} else {
							resumePressed = true;
						}
						super.touchDown(event, x, y, pointer, button);
					} else if (PlayUtils.inBoundsRect(new Point((int) x, (int) y), 199, 151, 328, 145, 329, 196, 201, 205)) {
						lselectPressed = true;
						super.touchDown(event, x, y, pointer, button);
					} else if (PlayUtils.inBoundsRect(new Point((int) x, (int) y), 348, 218, 478, 219, 474, 273, 346, 270)) {
						optionsPressed = true;
						super.touchDown(event, x, y, pointer, button);
					} else if (PlayUtils.inBoundsRect(new Point((int) x, (int) y), 500, 163, 632, 146, 637, 198, 508, 213)) {
						helpPressed = true;
						super.touchDown(event, x, y, pointer, button);
					} else if (PlayUtils.inBoundsRect(new Point((int) x, (int) y), 655, 239, 779, 262, 770, 310, 646, 286)) {
						quitPressed = true;
						super.touchDown(event, x, y, pointer, button);
					} else {
						touchActive = false;
					}
				}
				return true;
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				if (ngamePressed) {
					ngamePressed = false;
					final Action finalAction = new Action() {
						@Override
						public boolean act(final float delta) {
							game.setScreen(new HelpScreen(game, "newgame"));
							return false;
						}
					};
					shootMenu(newGame, 28, 200, finalAction);
				} else if (resumePressed) {
					resumePressed = false;
					final Action finalAction = new Action() {
						@Override
						public boolean act(final float delta) {
							if (curUserOPTS.isGameSaved()) {
								game.setScreen(new PlayScreen(game));
							} else {
								game.setScreen(new PlayScreen(game, curUserOPTS.getLastDifficulty(), curUserSCORE.getMaxPlayedLevel(curUserOPTS.getLastDifficulty())));
							}
							return false;
						}
					};
					shootMenu(resume, 28, 200, finalAction);
				} else if (lselectPressed) {
					lselectPressed = false;
					final Action finalAction = new Action() {
						@Override
						public boolean act(final float delta) {
							if (curUserOPTS.isFirstRun()) {
								game.setScreen(new HelpScreen(game, "levelselect"));
							} else {
								game.setScreen(new LevelScreen(game, false));
							}
							return false;
						}
					};
					shootMenu(levelselect, 199, 151, finalAction);
				} else if (optionsPressed) {
					optionsPressed = false;
					final Action finalAction = new Action() {
						@Override
						public boolean act(final float delta) {
							explodeEffect.reset();
							game.setScreen(new OptionsScreen(game));
							return false;
						}
					};
					shootMenu(options, 348, 218, finalAction);
				} else if (helpPressed) {
					helpPressed = false;
					final Action finalAction = new Action() {
						@Override
						public boolean act(final float delta) {
							game.setScreen(new HelpScreen(game, "help"));
							return false;
						}
					};
					shootMenu(help, 508, 163, finalAction);
				} else if (quitPressed) {
					quitPressed = false;
					final Action finalAction = new Action() {
						@Override
						public boolean act(final float delta) {
							game.exitGame();
							return false;
						}
					};
					shootMenu(quit, 655, 250, finalAction);
				}
			}

			@Override
			public boolean keyDown(final InputEvent event, final int keycode) {
				if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(final float delta) {
							game.exitGame();
							return false;
						}
					}));
				}
				return true;
			}
		});
	}

	@Override
	public void render(final float delta) {
		super.render(delta);
		if (explodeOn) {
			batch.begin();
			explodeEffect.draw(batch, delta);
			batch.end();
			if (explodeEffect.isComplete()) {
				explodeOn = false;
			}
		}
	}

	@Override
	public void dispose() {
		if (menuAtlas != null) {
			menuAtlas.dispose();
		}
		explodeEffect.reset();
		explodeEffect.dispose();
		super.dispose();
	}

	public void shootMenu(final AnimActor clicked, final float firePosX, final float firePosY, final Action finalAction) {
		hero.clearActions();
		final SequenceAction sequence = new SequenceAction();
		final float targetPosX = clicked.getX() + (clicked.getWidth() / 2);
		if (hero.getX() < targetPosX) {
			for (int i = (int) hero.getX(); i < ((int) targetPosX - 20); i = i + 10) {
				sequence.addAction(parallel(moveTo(i, hero.getY()), rotateBy(20)));
			}
		} else {
			for (int i = (int) hero.getX(); i >= ((int) targetPosX - 20); i = i - 10) {
				sequence.addAction(parallel(moveTo(i, hero.getY()), rotateBy(-20)));
			}
		}
		sequence.addAction(rotateTo(-90));
		hero.addAction(sequence(sequence, new Action() {
			@Override
			public boolean act(final float delta) {
				bullet.setPosition((hero.getX() + (hero.getWidth() / 2)) - 2, hero.getY());
				bullet.addAction(sequence(visible(true), new Action() {
					@Override
					public boolean act(final float delta) {
						Sounds.blast.play();
						return true;
					}
				}, moveTo(bullet.getX(), (clicked.getY() + clicked.getHeight()) - 30, 0.1f), visible(false), new Action() {
					@Override
					public boolean act(final float delta) {
						explodeEffect.reset();
						explodeEffect.setPosition(firePosX + 65, firePosY + 38);
						explodeOn = true;
						Sounds.burn.play();
						argbFull.addAction(sequence(delay(1f), visible(true), fadeIn(0.5f), finalAction));
						return true;
					}
				}));
				return true;
			}
		}));
	}
}
