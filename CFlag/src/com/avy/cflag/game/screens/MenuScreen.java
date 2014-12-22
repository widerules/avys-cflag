package com.avy.cflag.game.screens;

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
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.Utils;
import com.avy.cflag.game.graphics.MenuHeroAction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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

//	private Image versionStr;
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

	public ParticleEffect pe;
	public boolean explodeOn = false;

	public MenuScreen(CFlagGame game) {
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
		newGame = new AnimActor(new AnimDrawable(new Animation(0.24f, g.getFlipTexRegions("newgame"), PlayMode.LOOP_PINGPONG)));
		resume = new AnimActor(new AnimDrawable(new Animation(0.25f, g.getFlipTexRegions("resume"), PlayMode.LOOP_PINGPONG)));
		levelselect = new AnimActor(new AnimDrawable(new Animation(0.26f, g.getFlipTexRegions("levelselect"), PlayMode.LOOP_PINGPONG)));
		options = new AnimActor(new AnimDrawable(new Animation(0.21f, g.getFlipTexRegions("options"), PlayMode.LOOP_PINGPONG)));
		help = new AnimActor(new AnimDrawable(new Animation(0.22f, g.getFlipTexRegions("help"), PlayMode.LOOP_PINGPONG)));
		quit = new AnimActor(new AnimDrawable(new Animation(0.23f, g.getFlipTexRegions("quit"), PlayMode.LOOP_PINGPONG)));
		flagPole = new Image(g.getFlipTexRegion("flagpole"));
		flag = new AnimActor(new AnimDrawable(new Animation(0.1f, g.getFlipTexRegions("flag"), PlayMode.LOOP)));
//		versionStr = new Image(g.getFlipTexRegion("version"));
		avyStudiosStr = new Image(g.getFlipTexRegion("avystudios"));
		hero = new AnimActor(new AnimDrawable(new Animation(0.24f, g.getFlipTexRegions("hero"), PlayMode.LOOP)));
		bullet = new Image(g.getFlipTexRegion("bullet"));
		rateUs = new Image(g.getFlipTexRegion("rateus"));
		likeUs = new Image(g.getFlipTexRegion("likeus"));

		menuTopBar.setPosition(0, 0);
		newGame.setPosition(20, 63);
		resume.setPosition(20, 63);
		levelselect.setPosition(190, 63);
		options.setPosition(337, 66);
		help.setPosition(497, 67);
		quit.setPosition(639, 57);
		flagPole.setPosition(162, 227);
		flag.setPosition(171, 226);
//		versionStr.setPosition((bottomBar.getWidth() - versionStr.getWidth()) / 2, bottomBar.getY() + 20);
		avyStudiosStr.setPosition((bottomBar.getWidth() - avyStudiosStr.getWidth()) / 2, bottomBar.getY() + (bottomBar.getHeight()-avyStudiosStr.getHeight())/2);
		rateUs.setPosition(25, bottomBar.getY() + (bottomBar.getHeight() - rateUs.getHeight()) / 2);
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

		pe = new ParticleEffect();
		pe.load(Gdx.files.internal("atlas/explosion.p"), menuAtlas);
		pe.setPosition(200, 100);
		pe.start();

		stage.addActor(menuTopBar);
		stage.addActor(newGame);
		stage.addActor(resume);
		stage.addActor(levelselect);
		stage.addActor(options);
		stage.addActor(help);
		stage.addActor(quit);
		stage.addActor(flag);
		stage.addActor(flagPole);
//		stage.addActor(versionStr);
		stage.addActor(avyStudiosStr);
		stage.addActor(rateUs);
		stage.addActor(likeUs);
		stage.addActor(hero);
		stage.addActor(bullet);
		stage.addActor(argbFull);

		avyStudiosStr.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new AboutScreen(game));
					}
				})));
				return true;
			}
		});

		stage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(!touchActive){
					touchActive = true;
					if (Utils.inBoundsRect(new Point((int) x, (int) y), 28, 225, 156, 190, 166, 242, 40, 275)) {
						if (curUserOPTS.isFirstRun()) {
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
					} else {
						touchActive=false;
					}
				}
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (ngamePressed) {
					ngamePressed = false;
					final Action finalAction = new Action() {
						@Override
						public boolean act(float delta) {
							game.setScreen(new HelpScreen(game, "newgame"));
							return false;
						}
					};
					shootMenu(newGame, 28, 200, finalAction);
				} else if (resumePressed) {
					resumePressed = false;
					final Action finalAction = new Action() {
						@Override
						public boolean act(float delta) {
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
						public boolean act(float delta) {
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
						public boolean act(float delta) {
							pe.reset();
							game.setScreen(new OptionsScreen(game));
							return false;
						}
					};
					shootMenu(options, 348, 218, finalAction);
				} else if (helpPressed) {
					helpPressed = false;
					final Action finalAction = new Action() {
						@Override
						public boolean act(float delta) {
							game.setScreen(new HelpScreen(game, "help"));
							return false;
						}
					};
					shootMenu(help, 508, 163, finalAction);
				} else if (quitPressed) {
					quitPressed = false;
					final Action finalAction = new Action() {
						@Override
						public boolean act(float delta) {
							game.exitGame();
							return false;
						}
					};
					shootMenu(quit, 655, 250, finalAction);
				}
			}

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
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
	public void render(float delta) {
		super.render(delta);
		if (explodeOn) {
			batch.begin();
			pe.draw(batch, delta);
			batch.end();
			if (pe.isComplete()) {
				explodeOn = false;
			}
		}
	}

	@Override
	public void dispose() {
		if (menuAtlas != null) {
			menuAtlas.dispose();
		}
		pe.reset();
		pe.dispose();
		super.dispose();
	}

	public void shootMenu(final AnimActor clicked, final float firePosX, final float firePosY, final Action finalAction) {
		hero.clearActions();
		final SequenceAction sequence = new SequenceAction();
		final float targetPosX = clicked.getX() + clicked.getWidth() / 2;
		if (hero.getX() < targetPosX) {
			for (int i = (int) hero.getX(); i < (int) targetPosX - 20; i = i + 10) {
				sequence.addAction(parallel(moveTo(i, hero.getY()), rotateBy(20)));
			}
		} else {
			for (int i = (int) hero.getX(); i >= (int) targetPosX - 20; i = i - 10) {
				sequence.addAction(parallel(moveTo(i, hero.getY()), rotateBy(-20)));
			}
		}
		sequence.addAction(rotateTo(-90));
		hero.addAction(sequence(sequence, new Action() {
			@Override
			public boolean act(float delta) {
				bullet.setPosition(hero.getX() + hero.getWidth() / 2 - 2, hero.getY());
				bullet.addAction(sequence(visible(true), new Action() {
					@Override
					public boolean act(float delta) {
						Sounds.blast.play();
						return true;
					}
				}, moveTo(bullet.getX(), clicked.getY() + clicked.getHeight() - 30, 0.1f), visible(false), new Action() {
					@Override
					public boolean act(float delta) {
						pe.reset();
						pe.setPosition(firePosX + 65, firePosY + 38);
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
