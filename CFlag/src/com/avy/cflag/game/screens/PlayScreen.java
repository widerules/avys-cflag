package com.avy.cflag.game.screens;

import static com.avy.cflag.game.MemStore.curUserOPTS;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.base.BaseScreen;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.MemStore.Difficulty;
import com.avy.cflag.game.MemStore.Direction;
import com.avy.cflag.game.MemStore.GameState;
import com.avy.cflag.game.MemStore.PlayImages;
import com.avy.cflag.game.Utils;
import com.avy.cflag.game.elements.LTank;
import com.avy.cflag.game.elements.Level;
import com.avy.cflag.game.elements.Platform;
import com.avy.cflag.game.graphics.HintMenu;
import com.avy.cflag.game.graphics.ShortMenu;
import com.avy.cflag.game.utils.GameData;
import com.avy.cflag.game.utils.SaveThumbs;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PlayScreen extends BaseScreen {

	private static enum GameButtons {
		None, ArrowUp, ArrowRight, ArrowDown, ArrowLeft, Hint, UnDo, Fire
	}

	private int currentLevel;
	private Difficulty currentDclty;

	private GameState gameState;
	private Level lvl;
	private Platform pltfrm;
	private LTank ltank;
	private LTank undoList[];
	private int undoCnt;

	boolean hintUsed;
	boolean stateChanged;

	private TextureAtlas playAtlas;
	private final BitmapFont dfltFont;
	private final BitmapFont lvlNmeFont;

	private Image argbFull;
	private Image leftPanel;
	private Group midPanel;
	private Image rightPanel;

	private Group arrowButton_Left;
	private Image arrowButton_Left_Up;
	private Image arrowButton_Left_Down;

	private Group arrowButton_Up;
	private Image arrowButton_Up_Up;
	private Image arrowButton_Up_Down;

	private Group arrowButton_Right;
	private Image arrowButton_Right_Up;
	private Image arrowButton_Right_Down;

	private Group arrowButton_Down;
	private Image arrowButton_Down_Up;
	private Image arrowButton_Down_Down;

	private Group hintButton;
	private Image hintButton_Up;
	private Image hintButton_Down;

	private Group undoButton;
	private Image undoButton_Up;
	private Image undoButton_Down;

	private Group fireButton;
	private Image fireButton_Up;
	private Image fireButton_Down;

	private Group pausedMenu;
	private Group drownedMenu;
	private Group deadMenu;
	private Group wonMenu;
	private Group hintMenu;

	private GameButtons pressedButton;

	public PlayScreen(CFlagGame game) {
		super(game, true, true, true);

		playAtlas = g.createImageAtlas("play");
		g.setImageAtlas(playAtlas);
		PlayImages.load(g);
		pressedButton = GameButtons.None;

		dfltFont = g.createFont("ceaser", 20, true);
		g.setFont(dfltFont);

		lvlNmeFont = g.createFont("ceaser", 13, true);

		GameData savedGame = Utils.loadGame();

		currentDclty = savedGame.getCurrentDclty();
		currentLevel = savedGame.getCurrentLevel();
		gameState = GameState.Running;
		lvl = savedGame.getLvl();
		ltank = savedGame.getLtank();
		undoCnt = savedGame.getUndoCnt();
		undoList = savedGame.getUndoList();
		hintUsed = savedGame.isHintUsed();
		stateChanged = savedGame.isStateChanged();

	}

	public PlayScreen(CFlagGame game, Difficulty selectedDclty, int selectedLevel) {
		super(game, true, true, true);

		playAtlas = g.createImageAtlas("play");
		g.setImageAtlas(playAtlas);
		PlayImages.load(g);
		pressedButton = GameButtons.None;

		dfltFont = g.createFont("ceaser", 20, true);
		g.setFont(dfltFont);

		lvlNmeFont = g.createFont("ceaser", 13, true);

		currentDclty = selectedDclty;
		currentLevel = selectedLevel;
		gameState = GameState.Ready;

		if (currentLevel == 0) {
			currentLevel = 1;
			Utils.saveUserScores(currentDclty, currentLevel, 0, 0, false);
			SaveThumbs st = new SaveThumbs(g, currentDclty, currentLevel);
			st.run();
		}

		lvl = new Level();
		lvl.loadLevel(currentDclty, currentLevel);

		ltank = new LTank(lvl);

		undoCnt = 0;
		undoList = new LTank[undoCnt + 1];
		undoList[undoCnt] = ltank.clone();

		hintUsed = false;
		stateChanged = false;
	}

	@Override
	public void show() {
		super.show();

		pausedMenu = new ShortMenu(g, this, "gamepaused", "resume", "restart", "savenquit");
		pausedMenu.setVisible(false);
		pausedMenu.getColor().a = 0;
		drownedMenu = new ShortMenu(g, this, "drowned", "undo", "restart", "mainmenu");
		drownedMenu.setVisible(false);
		drownedMenu.getColor().a = 0;
		deadMenu = new ShortMenu(g, this, "shotdead", "undo", "restart", "mainmenu");
		deadMenu.setVisible(false);
		deadMenu.getColor().a = 0;
		wonMenu = new ShortMenu(g, this, "youwon", "nextlevel", "restart", "mainmenu");
		wonMenu.setVisible(false);
		wonMenu.getColor().a = 0;
		hintMenu = new HintMenu(g, this, "gamehint");
		hintMenu.setVisible(false);
		hintMenu.getColor().a = 0;

		argbFull = new Image(g.getFlipTexRegion("argbblack"));
		leftPanel = new Image(g.getFlipTexRegion("leftpanel"));
		midPanel = new Group();
		rightPanel = new Image(g.getFlipTexRegion("rightpanel"));

		arrowButton_Left_Up = new Image(g.getFlipTexRegion("arrow_l_up"));
		arrowButton_Left_Down = new Image(g.getFlipTexRegion("arrow_l_down"));
		arrowButton_Left_Down.setVisible(false);
		arrowButton_Left = new Group();
		arrowButton_Left.addActor(arrowButton_Left_Up);
		arrowButton_Left.addActor(arrowButton_Left_Down);

		arrowButton_Up_Up = new Image(g.getFlipTexRegion("arrow_u_up"));
		arrowButton_Up_Down = new Image(g.getFlipTexRegion("arrow_u_down"));
		arrowButton_Up_Down.setVisible(false);
		arrowButton_Up = new Group();
		arrowButton_Up.addActor(arrowButton_Up_Up);
		arrowButton_Up.addActor(arrowButton_Up_Down);

		arrowButton_Right_Up = new Image(g.getFlipTexRegion("arrow_r_up"));
		arrowButton_Right_Down = new Image(g.getFlipTexRegion("arrow_r_down"));
		arrowButton_Right_Down.setVisible(false);
		arrowButton_Right = new Group();
		arrowButton_Right.addActor(arrowButton_Right_Up);
		arrowButton_Right.addActor(arrowButton_Right_Down);

		arrowButton_Down_Up = new Image(g.getFlipTexRegion("arrow_d_up"));
		arrowButton_Down_Down = new Image(g.getFlipTexRegion("arrow_d_down"));
		arrowButton_Down_Down.setVisible(false);
		arrowButton_Down = new Group();
		arrowButton_Down.addActor(arrowButton_Down_Up);
		arrowButton_Down.addActor(arrowButton_Down_Down);

		hintButton_Up = new Image(g.getFlipTexRegion("hint_up"));
		hintButton_Down = new Image(g.getFlipTexRegion("hint_down"));
		hintButton_Down.setVisible(false);
		hintButton = new Group();
		hintButton.addActor(hintButton_Up);
		hintButton.addActor(hintButton_Down);

		undoButton_Up = new Image(g.getFlipTexRegion("undo_up"));
		undoButton_Down = new Image(g.getFlipTexRegion("undo_down"));
		undoButton_Down.setVisible(false);
		undoButton = new Group();
		undoButton.addActor(undoButton_Up);
		undoButton.addActor(undoButton_Down);

		fireButton_Up = new Image(g.getFlipTexRegion("fire_up"));
		fireButton_Down = new Image(g.getFlipTexRegion("fire_down"));
		fireButton_Down.setVisible(false);
		fireButton = new Group();
		fireButton.addActor(fireButton_Up);
		fireButton.addActor(fireButton_Down);

		argbFull.setPosition(0, 0);
		argbFull.setSize(game.getSrcWidth(), game.getSrcHeight());
		leftPanel.setPosition(0, 0);
		midPanel.setSize(480, 480);
		midPanel.setPosition(leftPanel.getWidth(), 0);
		rightPanel.setPosition(leftPanel.getWidth() + midPanel.getWidth(), 0);

		arrowButton_Up.setPosition(49, 293);
		arrowButton_Right.setPosition(86, 348);
		arrowButton_Down.setPosition(48, 400);
		arrowButton_Left.setPosition(10, 348);
		hintButton.setPosition(690, 206);
		undoButton.setPosition(690, 293);
		fireButton.setPosition(690, 390);

		pltfrm = new Platform(midPanel);
		pltfrm.paintPlatform(ltank);

		argbFull.addAction(sequence(fadeOut(1f), visible(false)));

		stage.addActor(leftPanel);
		stage.addActor(midPanel);
		stage.addActor(rightPanel);
		stage.addActor(arrowButton_Up);
		stage.addActor(arrowButton_Right);
		stage.addActor(arrowButton_Down);
		stage.addActor(arrowButton_Left);
		stage.addActor(hintButton);
		stage.addActor(undoButton);
		stage.addActor(fireButton);
		stage.addActor(pausedMenu);
		stage.addActor(drownedMenu);
		stage.addActor(deadMenu);
		stage.addActor(wonMenu);
		stage.addActor(hintMenu);
		stage.addActor(argbFull);

		arrowButton_Up.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Up_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Up_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				pressedButton = GameButtons.ArrowUp;
			};
		});
		arrowButton_Right.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Right_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Right_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				pressedButton = GameButtons.ArrowRight;
			};
		});
		arrowButton_Down.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Down_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Down_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				pressedButton = GameButtons.ArrowDown;
			};
		});
		arrowButton_Left.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Left_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Left_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				pressedButton = GameButtons.ArrowLeft;
			};
		});
		hintButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				hintButton_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				hintButton_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				gameState = GameState.Hint;
			};
		});
		undoButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				undoButton_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				undoButton_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				pressedButton = GameButtons.UnDo;
			};
		});
		fireButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				fireButton_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				fireButton_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				pressedButton = GameButtons.Fire;
			};
		});
		stage.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (gameState == GameState.Hint) {
					resume();
				}
				return true;
			};

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.BACK || keycode == Keys.ESCAPE)
					if (gameState == GameState.Running)
						pause();
					else if (gameState == GameState.Paused || gameState == GameState.Hint) {
						resume();
					}
				return true;
			}
		});
		setTouchEnabled(false);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		update(delta);
		if (stateChanged) {
			pltfrm.paintPlatform(ltank);
			stateChanged = false;
		}
		if (pltfrm != null)
			pltfrm.randomizeAnims();

		if (ltank != null)
			drawGameUI();
	}

	public void update(float delta) {
		switch (gameState) {
			case Ready:
				updateReady();
				break;
			case Running:
				updateRunning();
				break;
			case Paused:
				updatePaused();
				break;
			case Drowned:
				updateDrowned();
				break;
			case Dead:
				updateDead();
				break;
			case Won:
				updateWon();
				break;
			case Hint:
				updateHint();
				break;
			case Menu:
			case Restart:
			case NextLevel:
				updateFadeOut(delta);
				break;
			default:
				break;
		}
	}

	private void updateReady() {
		if (pltfrm.heroReady) {
			setTouchEnabled(true);
			gameState = GameState.Running;
		}
	}

	private void updateRunning() {
		int stateChangeCount = ltank.getStateChangeCount();

		switch (ltank.getCurTankState()) {
			case PrevATankFired:
				ltank.fireATankPrev();
				break;
			case CurATankFired:
				ltank.fireATankCur();
				break;
			case BothATankFired:
				ltank.fireBothATanks();
				break;
			case OnStream:
				setTouchEnabled(false);
				undoButton.setTouchable(Touchable.enabled);
				ltank.moveTank(ltank.getCurTankDirection());
				stateChanged = true;
				break;
			case OnIce:
				ltank.moveTank(ltank.getCurTankDirection());
				break;
			case OnTunnel:
				ltank.moveTank(ltank.getCurTankDirection());
				break;
			case OnWater:
				gameState = GameState.Drowned;
				break;
			case ShotDead:
				gameState = GameState.Dead;
				break;
			case ReachedFlag:
				gameState = GameState.Won;
				break;
			case Firing:
				ltank.fireTank();
				break;
			case Moving:
			case Blocked:
				setTouchEnabled(true);
				switch (pressedButton) {
					case ArrowUp:
						moveHero(Direction.Up);
						break;
					case ArrowRight:
						moveHero(Direction.Right);
						break;
					case ArrowDown:
						moveHero(Direction.Down);
						break;
					case ArrowLeft:
						moveHero(Direction.Left);
						break;
					case UnDo:
						undoInGame();
						stateChanged = true;
						break;
					case Fire:
						fireHero();
						break;
					default:
						break;
				}
				pressedButton = GameButtons.None;
				break;
			default:
				break;
		}

		if (ltank.getStateChangeCount() > stateChangeCount) {
			stateChanged = true;
			undoCnt++;
			undoList = (LTank[]) Utils.resizeArray(undoList, undoCnt + 1);
			undoList[undoCnt] = ltank.clone();
		}
	}

	private void updatePaused() {
		pausedMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
	}

	private void updateDrowned() {
		drownedMenu.addAction(sequence(visible(true), fadeIn(0.1f)));
	}

	private void updateDead() {
		deadMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
	}

	private void updateWon() {
		wonMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
	}

	private void updateHint() {
		hintMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
	}

	private void updateFadeOut(float delta) {
		switch (gameState) {
			case Menu:
			case Quit:
				game.setScreen(new MenuScreen(game));
				break;
			case Restart:
				game.setScreen(new PlayScreen(game, currentDclty, currentLevel));
				break;
			case NextLevel:
				saveScores();
				game.setScreen(new PlayScreen(game, currentDclty, currentLevel));
				break;
			default:
				break;
		}
	}

	public void drawGameUI() {
		batch.begin();
		g.drawString(Integer.toString(currentLevel), 82, 72, Color.BLACK);
		g.drawStringWrapped(lvlNmeFont, lvl.getLvlName(), 82, 152, Color.BLACK);
		g.drawString(lvl.getLvlDclty().toString(), 80, 241, Color.BLACK);
		g.drawString(Integer.toString(ltank.getTankMoves()), 720, 72, Color.BLACK);
		g.drawString(Integer.toString(ltank.getTankShots()), 720, 158, Color.BLACK);
		if (gameState == GameState.Hint) {
			g.drawStringWrapped(lvlNmeFont, lvl.getLvlHint(), 198, 234, 404, Color.BLACK);
		}
		batch.end();

		g.setSr(sr);
		sr.begin(ShapeType.Filled);
		g.drawRectWithBorder(ltank.getCurTankBullet().getCurBulletRect(), Color.GREEN);
		g.drawRectWithBorder(ltank.getaTankPrev().getTankBullet().getCurBulletRect(), Color.RED);
		g.drawRectWithBorder(ltank.getaTankCur().getTankBullet().getCurBulletRect(), Color.RED);
		sr.end();
	}

	public void undo() {
		setTouchEnabled(true);
		if (undoCnt > 0) {
			undoCnt--;
			ltank = null;
			ltank = undoList[undoCnt].clone();
		}

		if (gameState == GameState.Dead) {
			deadMenu.addAction(sequence(fadeOut(0.2f), visible(false)));
		} else if (gameState == GameState.Drowned) {
			drownedMenu.addAction(sequence(fadeOut(0.2f), visible(false)));
		}
		gameState = GameState.Running;
		stateChanged = true;
	}

	public void undoInGame() {
		if (undoCnt > 0) {
			undoCnt--;
			ltank = null;
			ltank = undoList[undoCnt].clone();
		}
	}

	public void fireHero() {
		ltank.fireTank();
		Sounds.shoot.play();
		ltank.incrementTankShots();
	}

	public void moveHero(Direction drc) {
		if (ltank.moveTank(drc)) {
			ltank.incrementTankMoves();
		}
	}

	public void nextlevel() {
		gameState = GameState.NextLevel;
	}

	public void restart() {
		gameState = GameState.Restart;
	}

	public void mainmenu() {
		gameState = GameState.Menu;
	}

	public void showHint() {
		gameState = GameState.Hint;
	}

	public void pause() {
		if (gameState == GameState.Running)
			gameState = GameState.Paused;
	}

	public void resume() {
		if (gameState == GameState.Paused) {
			pausedMenu.addAction(sequence(fadeOut(0.2f), visible(false)));
			gameState = GameState.Running;
		} else if (gameState == GameState.Hint) {
			hintMenu.addAction(sequence(fadeOut(0.2f), visible(false)));
			gameState = GameState.Running;
		}
	}

	public void savenquit() {
		GameData gData = new GameData();
		gData.setCurrentDclty(currentDclty);
		gData.setCurrentLevel(currentLevel);
		gData.setGameState(gameState);
		gData.setHintUsed(hintUsed);
		gData.setLtank(ltank);
		gData.setLvl(lvl);
		gData.setStateChanged(stateChanged);
		gData.setUndoCnt(undoCnt);
		gData.setUndoList(undoList);

		Utils.saveGame(gData);
		curUserOPTS.setFirstRun(false);
		Utils.saveGameOptions();
		mainmenu();
	}

	@Override
	public void dispose() {
		nullify();
	}

	private void saveScores() {
		Utils.saveUserScores(currentDclty, currentLevel, ltank.getTankMoves(), ltank.getTankShots(), hintUsed);
		SaveThumbs st1 = new SaveThumbs(g, currentDclty, currentLevel);
		st1.run();
		currentLevel++;
		Utils.saveUserScores(currentDclty, currentLevel, 0, 0, false);
		SaveThumbs st2 = new SaveThumbs(g, currentDclty, currentLevel);
		st2.run();
	}

	private void nullify() {
		if (playAtlas != null)
			playAtlas.dispose();
		if (dfltFont != null)
			dfltFont.dispose();
		lvl = null;
		pltfrm = null;
		ltank = null;
		undoList = null;
		System.gc();
	}

	private void setTouchEnabled(boolean isEnabled) {
		if (isEnabled) {
			arrowButton_Up.setTouchable(Touchable.enabled);
			arrowButton_Right.setTouchable(Touchable.enabled);
			arrowButton_Down.setTouchable(Touchable.enabled);
			arrowButton_Left.setTouchable(Touchable.enabled);
			hintButton.setTouchable(Touchable.enabled);
			undoButton.setTouchable(Touchable.enabled);
			fireButton.setTouchable(Touchable.enabled);
		} else {
			arrowButton_Up.setTouchable(Touchable.disabled);
			arrowButton_Right.setTouchable(Touchable.disabled);
			arrowButton_Down.setTouchable(Touchable.disabled);
			arrowButton_Left.setTouchable(Touchable.disabled);
			hintButton.setTouchable(Touchable.disabled);
			undoButton.setTouchable(Touchable.disabled);
			fireButton.setTouchable(Touchable.disabled);
		}
	}
}