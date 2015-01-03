package com.avy.cflag.game.screens;

import static com.avy.cflag.game.MemStore.curUserOPTS;
import static com.avy.cflag.game.MemStore.curUserSCORE;
import static com.avy.cflag.game.MemStore.lvlCntPerDCLTY;
import static com.avy.cflag.game.MemStore.savedGame;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import java.util.ArrayList;

import com.avy.cflag.base.BaseScreen;
import com.avy.cflag.base.Point;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.base.TouchListener;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.EnumStore.Difficulty;
import com.avy.cflag.game.EnumStore.Direction;
import com.avy.cflag.game.EnumStore.GameState;
import com.avy.cflag.game.EnumStore.PlayImages;
import com.avy.cflag.game.EnumStore.TankState;
import com.avy.cflag.game.GameUtils;
import com.avy.cflag.game.PlayUtils;
import com.avy.cflag.game.elements.LTank;
import com.avy.cflag.game.elements.Level;
import com.avy.cflag.game.elements.Platform;
import com.avy.cflag.game.graphics.HintMenu;
import com.avy.cflag.game.graphics.ShortMenu;
import com.avy.cflag.game.pathfinding.PathFinder;
import com.avy.cflag.game.utils.GameData;
import com.avy.cflag.game.utils.SaveGame;
import com.avy.cflag.game.utils.SaveThumbs;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class PlayScreen extends BaseScreen {

	private static enum GameButtons {
		None, ArrowUp, ArrowRight, ArrowDown, ArrowLeft, Hint, UnDo, Fire
	}

	private int currentLevel;
	private Difficulty currentDclty;

	private GameState gameState;
	private Level lVl;
	private Platform pltFrm;
	private LTank lTank;
	private LTank undoList[];
	private int undoCnt;

	boolean hintUsed;
	boolean stateChanged;
	boolean aTankStateChanged;

	boolean fadeOutActive;

	private TextureAtlas playAtlas;
	private BitmapFont dfltFont;
	private BitmapFont lvlNmeFont;

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

	private float fontAlpha = 0.3f;

	private boolean updateInProgress = false;
	private GameButtons pressedButton = GameButtons.None;
	private GameButtons longPressButton = GameButtons.None;
	private final int longPressReflexDelay = 2;
	private int longPressTimer = 0;
	private float dragStartX = 0, dragStartY = 0;
	DragListener dragListener = null;
	private float swipeSensitivity = 40f;
	private boolean undoInProgress = false;
	private boolean touchEnabled = true;
	private final PathFinder pathFinder = new PathFinder();;
	private ArrayList<Direction> autoMovePath = new ArrayList<Direction>();;
	private int autoMoveCounter = 0;
	private boolean autoMoveActive = false;

	public PlayScreen(final CFlagGame game) {
		super(game, true, true, true);
		initImages();
		if (savedGame == null) {
			initVariables(curUserOPTS.getLastDifficulty(), curUserSCORE.getMaxPlayedLevel(curUserOPTS.getLastDifficulty()));
		} else {
			currentDclty = savedGame.getCurrentDclty();
			currentLevel = savedGame.getCurrentLevel();
			gameState = GameState.Ready;
			lVl = savedGame.getLvl();
			lTank = savedGame.getLtank();
			undoCnt = savedGame.getUndoCnt();
			undoList = savedGame.getUndoList();
			hintUsed = savedGame.isHintUsed();
			stateChanged = savedGame.isStateChanged();
		}
	}

	public PlayScreen(final CFlagGame game, final Difficulty selectedDclty, final int selectedLevel) {
		super(game, true, true, true);
		initImages();
		initVariables(selectedDclty, selectedLevel);
	}

	private void initImages() {
		playAtlas = g.createImageAtlas("play");
		g.setImageAtlas(playAtlas);
		PlayImages.load(g);
		pressedButton = GameButtons.None;

		dfltFont = g.createFont("salsa", 20, true);
		g.setFont(dfltFont);

		lvlNmeFont = g.createFont("salsa", 13, true);
		fadeOutActive = false;
	}

	private void initVariables(final Difficulty selectedDclty, final int selectedLevel) {
		currentDclty = selectedDclty;
		currentLevel = selectedLevel;
		gameState = GameState.Ready;

		curUserOPTS.setlastDclty(selectedDclty);
		curUserOPTS.setFirstRun(false);

		GameUtils.saveGameOptions();

		lVl = new Level();
		lVl.loadLevel(currentDclty, currentLevel);

		lTank = new LTank(lVl);

		undoCnt = 0;
		undoList = new LTank[undoCnt + 1];
		undoList[undoCnt] = lTank.clone();

		hintUsed = false;
		stateChanged = false;
		aTankStateChanged = false;
		swipeSensitivity = 120 - curUserOPTS.getSwipeSensitivity();
	}

	@Override
	public void show() {
		super.show();

		pausedMenu = new ShortMenu(g, this, "gamepaused", "resumeme", "restart", "mainmenu");
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
		arrowButton_Left.setName("Left");

		arrowButton_Up_Up = new Image(g.getFlipTexRegion("arrow_u_up"));
		arrowButton_Up_Up.setName("UpUp");
		arrowButton_Up_Down = new Image(g.getFlipTexRegion("arrow_u_down"));
		arrowButton_Up_Down.setVisible(false);
		arrowButton_Up_Down.setName("UpDown");
		arrowButton_Up = new Group();
		arrowButton_Up.addActor(arrowButton_Up_Up);
		arrowButton_Up.addActor(arrowButton_Up_Down);
		arrowButton_Up.setName("Up");

		arrowButton_Right_Up = new Image(g.getFlipTexRegion("arrow_r_up"));
		arrowButton_Right_Down = new Image(g.getFlipTexRegion("arrow_r_down"));
		arrowButton_Right_Down.setVisible(false);
		arrowButton_Right = new Group();
		arrowButton_Right.addActor(arrowButton_Right_Up);
		arrowButton_Right.addActor(arrowButton_Right_Down);
		arrowButton_Right.setName("Right");

		arrowButton_Down_Up = new Image(g.getFlipTexRegion("arrow_d_up"));
		arrowButton_Down_Down = new Image(g.getFlipTexRegion("arrow_d_down"));
		arrowButton_Down_Down.setVisible(false);
		arrowButton_Down = new Group();
		arrowButton_Down.addActor(arrowButton_Down_Up);
		arrowButton_Down.addActor(arrowButton_Down_Down);
		arrowButton_Down.setName("Down");

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

		pltFrm = new Platform(midPanel);
		pltFrm.paintPlatform(lTank);

		argbFull.addAction(sequence(fadeOut(1f), visible(false), new Action() {
			@Override
			public boolean act(final float delta) {
				fontAlpha = 1f;
				return true;
			}
		}));
		argbFull.setName("Test");
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

		arrowButton_Up.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				arrowButton_Up_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.ArrowUp;
				return super.touchDown(event, x, y, pointer, button);
			};

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				arrowButton_Up_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton = GameButtons.None;
			};
		});

		arrowButton_Up.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f) {
			@Override
			public boolean longPress(final Actor actor, final float x, final float y) {
				longPressButton = GameButtons.ArrowUp;
				return super.longPress(actor, x, y);
			}
		});

		arrowButton_Right.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				arrowButton_Right_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.ArrowRight;
				return super.touchDown(event, x, y, pointer, button);
			};

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				arrowButton_Right_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton = GameButtons.None;
			};
		});

		arrowButton_Right.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f) {
			@Override
			public boolean longPress(final Actor actor, final float x, final float y) {
				longPressButton = GameButtons.ArrowRight;
				return super.longPress(actor, x, y);
			}
		});

		arrowButton_Down.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				arrowButton_Down_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.ArrowDown;
				return super.touchDown(event, x, y, pointer, button);
			};

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				arrowButton_Down_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton = GameButtons.None;
			};
		});

		arrowButton_Down.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f) {
			@Override
			public boolean longPress(final Actor actor, final float x, final float y) {
				longPressButton = GameButtons.ArrowDown;
				return super.longPress(actor, x, y);
			}
		});

		arrowButton_Left.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				arrowButton_Left_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.ArrowLeft;
				return super.touchDown(event, x, y, pointer, button);
			};

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				arrowButton_Left_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton = GameButtons.None;
			};
		});

		arrowButton_Left.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f) {
			@Override
			public boolean longPress(final Actor actor, final float x, final float y) {
				longPressButton = GameButtons.ArrowLeft;
				return super.longPress(actor, x, y);
			}
		});

		hintButton.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				hintButton_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.Hint;
				return super.touchDown(event, x, y, pointer, button);
			};

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				hintButton_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
			};
		});

		undoButton.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				undoButton_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.UnDo;
				return super.touchDown(event, x, y, pointer, button);
			};

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				undoButton_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton = GameButtons.None;
			};
		});

		undoButton.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f) {
			@Override
			public boolean longPress(final Actor actor, final float x, final float y) {
				if (undoCnt > 0) {
					longPressButton = GameButtons.UnDo;
				} else {
					longPressButton = GameButtons.None;
				}
				return super.longPress(actor, x, y);
			}
		});

		fireButton.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				fireButton_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.Fire;
				return super.touchDown(event, x, y, pointer, button);
			};

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				fireButton_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton = GameButtons.None;
			};
		});

		fireButton.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f) {
			@Override
			public boolean longPress(final Actor actor, final float x, final float y) {
				longPressButton = GameButtons.Fire;
				return super.longPress(actor, x, y);
			}
		});

		stage.addListener(new ActorGestureListener() {
			@Override
			public void fling(final InputEvent event, final float velocityX, final float velocityY, final int button) {
				if (!undoInProgress) {
					if (Math.abs(velocityX) > (Math.abs(velocityY) + 10)) {
						if (velocityX > 0) {
							pressedButton = GameButtons.ArrowRight;
						} else {
							pressedButton = GameButtons.ArrowLeft;
						}
					} else if (Math.abs(velocityY) > (Math.abs(velocityX) + 10)) {
						if (velocityY > 0) {
							pressedButton = GameButtons.ArrowDown;
						} else {
							pressedButton = GameButtons.ArrowUp;
						}
					}
				}
				super.fling(event, velocityX, velocityY, button);
			}
		});

		midPanel.addListener(new DragListener() {
			Point touchDownPos = new Point();

			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				touchDownPos = new Point((int) x, (int) y);
				return true;
			};

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				final Point touchUpPos = new Point((int) x, (int) y);
				if (!autoMoveActive && PlayUtils.isTouchDownUpInSameSquare(touchUpPos, touchDownPos)) {
					if (initAutoMove(new Point((int) x, (int) y))) {
						autoMoveActive = true;
					}
				}
			}
		});

		dragListener = new DragListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				if (gameState == GameState.Hint) {
					resumeme();
				}
				return super.touchDown(event, x, y, pointer, button);
			};

			@Override
			public boolean keyDown(final InputEvent event, final int keycode) {
				if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
					if (gameState == GameState.Running) {
						pauseme();
					} else if ((gameState == GameState.Paused) || (gameState == GameState.Hint)) {
						resumeme();
					}
				}
				return true;
			}

			@Override
			public void drag(final InputEvent event, final float x, final float y, final int pointer) {
				if (touchEnabled && !undoInProgress) {
					final float velocityX = x - dragStartX;
					final float velocityY = y - dragStartY;
					if ((Math.abs(velocityX) > swipeSensitivity) || (Math.abs(velocityY) > swipeSensitivity)) {
						if (Math.abs(velocityX) > (Math.abs(velocityY) + 10)) {
							if (velocityX > 0) {
								longPressButton = GameButtons.ArrowRight;
							} else {
								longPressButton = GameButtons.ArrowLeft;
							}
						} else if (Math.abs(velocityY) > (Math.abs(velocityX) + 10)) {
							if (velocityY > 0) {
								longPressButton = GameButtons.ArrowDown;
							} else {
								longPressButton = GameButtons.ArrowUp;
							}
						}
					}
				}
				super.drag(event, x, y, pointer);
			}

			@Override
			public void dragStart(final InputEvent event, final float x, final float y, final int pointer) {
				dragStartX = x;
				dragStartY = y;
				super.dragStart(event, x, y, pointer);
			}

			@Override
			public void dragStop(final InputEvent event, final float x, final float y, final int pointer) {
				dragStartX = 0;
				dragStartY = 0;
				longPressButton = GameButtons.None;
				super.dragStop(event, x, y, pointer);
			}
		};

		final ClickListener hackListener = new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				if (inTapSquare(780, 20) && (getTapCount() == 3)) {
					gameState = GameState.Won;
				}
				super.clicked(event, x, y);
			}
		};
		hackListener.setTapSquareSize(20);
		stage.addListener(hackListener);
		stage.addListener(dragListener);
		setTouchEnabled(false);
	}

	@Override
	public void render(final float delta) {
		super.render(delta);
		if (!updateInProgress) {
			update(delta);
		}
		if (stateChanged || aTankStateChanged) {
			pltFrm.paintPlatform(lTank);
			stateChanged = false;
			aTankStateChanged = false;
		}
		if (pltFrm != null) {
			pltFrm.randomizeAnims();
		}

		if (lTank != null) {
			drawGameUI();
		}
	}

	public void update(final float delta) {
		updateInProgress = true;
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
				updateFadeOut();
				break;
			default:
				break;
		}
		updateInProgress = false;
	}

	private void updateReady() {
		if (pltFrm.heroReady) {
			setTouchEnabled(true);
			gameState = GameState.Running;
		}
	}

	private void updateRunning() {
		final int stateChangeCount = lTank.getStateChangeCount();

		switch (lTank.getCurTankState()) {
			case PrevATankFired:
				aTankStateChanged = lTank.fireATankPrev();
				break;
			case CurATankFired:
				undoInProgress = true;
				aTankStateChanged = lTank.fireATankCur();
				break;
			case BothATankFired:
				undoInProgress = true;
				aTankStateChanged = lTank.fireBothATanks();
				break;
			case OnStream:
				setTouchEnabled(false);
				undoButton.setTouchable(Touchable.enabled);
				lTank.moveTank(lTank.getCurTankDirection());
				stateChanged = true;
				if (pressedButton == GameButtons.UnDo) {
					undoInGame();
				}
				longPressButton = GameButtons.None;
				pressedButton = GameButtons.None;
				break;
			case OnIce:
				setTouchEnabled(false);
				lTank.moveTank(lTank.getCurTankDirection());
				stateChanged = true;
				break;
			case ObjOnIce:
				setTouchEnabled(false);
				lTank.slideObjOnIce();
				stateChanged = true;
				break;
			case OnTunnel:
				lTank.moveTank(lTank.getCurTankDirection());
				stateChanged = true;
				break;
			case OnWater:
				undoInProgress = true;
				gameState = GameState.Drowned;
				break;
			case ShotDead:
				undoInProgress = true;
				gameState = GameState.Dead;
				break;
			case ReachedFlag:
				gameState = GameState.Won;
				break;
			case Firing:
				lTank.fireTank();
				break;
			case Moving:
			case Blocked:
				if (autoMoveActive) {
					final Direction autoMoveDirection = getAutoMoveDirection();
					if (autoMoveDirection != null) {
						moveHero(autoMoveDirection);
					} else {
						autoMoveActive = false;
						setTouchEnabled(true);
					}
				} else {
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
						case Hint:
							gameState = GameState.Hint;
							break;
						default:
							if (longPressTimer < longPressReflexDelay) {
								longPressTimer++;
							} else {
								longPressTimer = 0;
								switch (longPressButton) {
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
							}
							break;
					}
					pressedButton = GameButtons.None;
				}
			default:
				break;
		}

		if (lTank.getStateChangeCount() > stateChangeCount) {
			stateChanged = true;
			undoCnt++;
			undoList = (LTank[]) PlayUtils.resizeArray(undoList, undoCnt + 1);
			undoList[undoCnt] = lTank.clone();
		}
	}

	private void updatePaused() {
		autoMoveActive = false;
		if (!pausedMenu.isVisible()) {
			fontAlpha = 0.4f;
			pausedMenu.addAction(sequence(visible(true), parallel(new Action() {
				@Override
				public boolean act(final float delta) {
					saveGame();
					return true;
				}

			}, fadeIn(0.2f))));
		}
	}

	private void updateDrowned() {
		autoMoveActive = false;
		if (!drownedMenu.isVisible()) {
			fontAlpha = 0.4f;
			Sounds.drown.play();
			drownedMenu.addAction(sequence(visible(true), fadeIn(0.1f)));
			undoInProgress = true;
			longPressButton = GameButtons.None;
		}
	}

	private void updateDead() {
		autoMoveActive = false;
		if (!deadMenu.isVisible()) {
			fontAlpha = 0.4f;
			Sounds.dead.play();
			deadMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
			undoInProgress = true;
			longPressButton = GameButtons.None;
		}
	}

	private void updateWon() {
		autoMoveActive = false;
		if (!wonMenu.isVisible()) {
			fontAlpha = 0.4f;
			wonMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
			deleteSave();
			saveScores();
			Sounds.won.play();
		}
	}

	private void updateHint() {
		autoMoveActive = false;
		if (!hintMenu.isVisible()) {
			fontAlpha = 0.4f;
			hintMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
			hintUsed = true;
			GameUtils.saveUserScores(currentDclty, currentLevel, 0, 0, hintUsed);
		}
	}

	private void updateFadeOut() {
		if (!fadeOutActive) {
			switch (gameState) {
				case Menu:
				case Quit:
					fadeOutActive = true;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(final float delta) {
							game.setScreen(new MenuScreen(game));
							return false;
						}
					}));
					break;
				case Restart:
					fadeOutActive = true;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(final float delta) {
							game.setScreen(new PlayScreen(game, currentDclty, currentLevel));
							return false;
						}
					}));
					break;
				case NextLevel:
					fadeOutActive = true;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(final float delta) {
							if (curUserSCORE.getMaxPlayedLevel(currentDclty) == (currentLevel + 1)) {
								game.setScreen(new LevelScreen(game, true));
							} else {
								game.setScreen(new LevelScreen(game, false));
							}
							return false;
						}
					}));
					break;
				default:
					break;
			}
		}
	}

	public void drawGameUI() {
		if (!fadeOutActive) {
			batch.begin();
			g.drawString(Integer.toString(currentLevel), 82, 72, Color.GREEN, fontAlpha);
			g.drawStringWrapped(lvlNmeFont, lVl.getLvlName(), 82, 241, Color.GREEN, fontAlpha);
			g.drawString(lvlNmeFont, lVl.getLvlDclty().toString(), 80, 157, Color.GREEN, fontAlpha);
			g.drawString(Integer.toString(lTank.getTankMoves()), 720, 72, Color.GREEN, fontAlpha);
			g.drawString(Integer.toString(lTank.getTankShots()), 720, 158, Color.GREEN, fontAlpha);
			if ((gameState == GameState.Hint) && (hintMenu.getColor().a >= 0.7f)) {
				g.drawStringWrapped(lvlNmeFont, lVl.getLvlHint(), 198, 234, 404, Color.GREEN, 1f);
			}
			// Rect bulletRect = ltank.getCurTankBullet().getCurBulletRect();
			// batch.draw(bullet, bulletRect.left, bulletRect.top);
			batch.end();
		}

		g.setSr(sr);
		sr.begin(ShapeType.Filled);
		g.drawRectWithBorder(lTank.getCurTankBullet().getCurBulletRect(), Color.GREEN);
		g.drawRectWithBorder(lTank.getaTankPrev().getTankBullet().getCurBulletRect(), Color.RED);
		g.drawRectWithBorder(lTank.getaTankCur().getTankBullet().getCurBulletRect(), Color.RED);
		sr.end();
	}

	public void undo() {
		setTouchEnabled(true);
		if (undoCnt > 0) {
			undoCnt--;
			lTank = null;
			lTank = undoList[undoCnt].clone();
			if ((lTank.getCurTankState() == TankState.OnTunnel) || (lTank.getCurTankState() == TankState.OnStream) || (lTank.getCurTankState() == TankState.OnIce)) {
				undoCnt--;
				lTank = null;
				lTank = undoList[undoCnt].clone();
			}
		}

		if (gameState == GameState.Dead) {
			fontAlpha = 1f;
			deadMenu.addAction(sequence(fadeOut(0.2f), visible(false)));
		} else if (gameState == GameState.Drowned) {
			fontAlpha = 1f;
			drownedMenu.addAction(sequence(fadeOut(0.2f), visible(false)));
		}
		gameState = GameState.Running;
		longPressButton = GameButtons.None;
		stateChanged = true;
		undoInProgress = false;
	}

	public void undoInGame() {
		if (undoCnt > 0) {
			undoCnt--;
			lTank = null;
			lTank = undoList[undoCnt].clone();
			if ((lTank.getCurTankState() == TankState.OnTunnel) || (lTank.getCurTankState() == TankState.OnStream) || (lTank.getCurTankState() == TankState.OnIce)) {
				undoCnt--;
				lTank = null;
				lTank = undoList[undoCnt].clone();
			}
		} else {
			longPressButton = GameButtons.None;
		}
	}

	public void fireHero() {
		lTank.fireTank();
		Sounds.shoot.play();
		lTank.incrementTankShots();
	}

	public void moveHero(final Direction drc) {
		if (lTank.moveTank(drc)) {
			lTank.incrementTankMoves();
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

	public void pauseme() {
		if (gameState == GameState.Running) {
			gameState = GameState.Paused;
		}
	}

	public void resumeme() {
		if (gameState == GameState.Paused) {
			if (pausedMenu.isVisible()) {
				fontAlpha = 1f;
				pausedMenu.addAction(sequence(fadeOut(0.2f), visible(false)));
			}
			gameState = GameState.Running;
		} else if (gameState == GameState.Hint) {
			if (hintMenu.isVisible()) {
				fontAlpha = 1f;
				hintMenu.addAction(sequence(fadeOut(0.2f), visible(false)));
			}
			gameState = GameState.Running;
		}
	}

	@Override
	public void pause() {
		fontAlpha = 0.4f;
		pausedMenu.getColor().a = 1f;
		gameState = GameState.Paused;
		pausedMenu.setVisible(true);
		saveGame();
	}

	public boolean initAutoMove(final Point clickPos) {
		final Point dstPoint = PlayUtils.convertPixToPoint(clickPos);
		autoMovePath = pathFinder.findPath(lTank.getLvlPlayField(), lTank.getCurTankDirection(), lTank.getCurTankPos(), dstPoint);
		if (autoMovePath.size() > 0) {
			autoMoveCounter = 0;
			setTouchEnabled(false);
			return true;
		} else {
			return false;
		}
	}

	public Direction getAutoMoveDirection() {
		if (autoMoveCounter < autoMovePath.size()) {
			return autoMovePath.get(autoMoveCounter++);
		} else {
			return null;
		}
	}

	public void saveGame() {
		final GameData gData = new GameData();
		gData.setCurrentDclty(currentDclty);
		gData.setCurrentLevel(currentLevel);
		gData.setGameState(gameState);
		gData.setHintUsed(hintUsed);
		gData.setLtank(lTank);
		gData.setLvl(lVl);
		gData.setStateChanged(stateChanged);
		gData.setUndoCnt(undoCnt);
		gData.setUndoList(undoList);
		final SaveGame sg = new SaveGame(gData);
		sg.start();
	}

	private void deleteSave() {
		curUserOPTS.setGameSaved(false);
		GameUtils.deleteSavedGame(curUserOPTS.getUserName());
	}

	private void saveScores() {
		GameUtils.saveUserScores(currentDclty, currentLevel, lTank.getTankMoves(), lTank.getTankShots(), hintUsed);
		if (currentLevel == curUserSCORE.getMaxPlayedLevel(currentDclty)) {
			if ((currentLevel + 1) <= lvlCntPerDCLTY[currentDclty.ordinal()]) {
				GameUtils.saveUserScores(currentDclty, currentLevel + 1, 0, 0, false);
				final SaveThumbs st2 = new SaveThumbs(g, currentDclty, currentLevel + 1);
				st2.run();
			} else {
				final Difficulty nxtDifficulty = PlayUtils.getDifficultyByIdx(currentDclty.ordinal() + 1);
				if (nxtDifficulty != null) {
					GameUtils.saveUserScores(nxtDifficulty, curUserSCORE.getMaxPlayedLevel(nxtDifficulty), 0, 0, false);
					final SaveThumbs st2 = new SaveThumbs(g, nxtDifficulty, curUserSCORE.getMaxPlayedLevel(nxtDifficulty));
					st2.run();
					curUserOPTS.setlastDclty(nxtDifficulty);
					GameUtils.saveGameOptions();
				}
			}
		}
	}

	private void setTouchEnabled(final boolean isEnabled) {
		if (isEnabled) {
			touchEnabled = true;
			arrowButton_Up.setTouchable(Touchable.enabled);
			arrowButton_Right.setTouchable(Touchable.enabled);
			arrowButton_Down.setTouchable(Touchable.enabled);
			arrowButton_Left.setTouchable(Touchable.enabled);
			hintButton.setTouchable(Touchable.enabled);
			undoButton.setTouchable(Touchable.enabled);
			fireButton.setTouchable(Touchable.enabled);
		} else {
			dragListener.cancel();
			touchEnabled = false;
			arrowButton_Up.setTouchable(Touchable.disabled);
			arrowButton_Right.setTouchable(Touchable.disabled);
			arrowButton_Down.setTouchable(Touchable.disabled);
			arrowButton_Left.setTouchable(Touchable.disabled);
			hintButton.setTouchable(Touchable.disabled);
			undoButton.setTouchable(Touchable.disabled);
			fireButton.setTouchable(Touchable.disabled);
		}
	}

	@Override
	public void dispose() {
		if (playAtlas != null) {
			playAtlas.dispose();
		}
		if (dfltFont != null) {
			dfltFont.dispose();
		}
		if (lvlNmeFont != null) {
			lvlNmeFont.dispose();
		}
		lVl = null;
		pltFrm = null;
		lTank = null;
		undoList = null;
		System.gc();
		super.dispose();
	}

}