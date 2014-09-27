package com.avy.cflag.game.screens;

import static com.avy.cflag.game.MemStore.curUserOPTS;
import static com.avy.cflag.game.MemStore.curUserSCORE;
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
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
	private Level lvl;
	private Platform pltfrm;
	private LTank ltank;
	private LTank undoList[];
	private int undoCnt;

	boolean hintUsed;
	boolean stateChanged;
	boolean fadeOutActive;

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
	
	private GameButtons longPressButton = GameButtons.None;
	private int longPressReflexDelay=2;
	private int longPressTimer=0;
	private float dragStartX=0, dragStartY=0;
	private boolean updateInProgress = false;

	public PlayScreen(CFlagGame game) {
		super(game, true, true, true);

		playAtlas = g.createImageAtlas("play");
		g.setImageAtlas(playAtlas);
		PlayImages.load(g);
		pressedButton = GameButtons.None;

		dfltFont = g.createFont("ceaser", 20, true);
		g.setFont(dfltFont);

		lvlNmeFont = g.createFont("ceaser", 13, true);
		fadeOutActive=false;
		
		GameData savedGame = Utils.loadGame();

		if(savedGame==null){
			init(curUserOPTS.getLastDifficulty(), curUserSCORE.getMaxPlayedLevel(curUserOPTS.getLastDifficulty()));
		} else {
			currentDclty = savedGame.getCurrentDclty();
			currentLevel = savedGame.getCurrentLevel();
			gameState = GameState.Ready;
			lvl = savedGame.getLvl();
			ltank = savedGame.getLtank();
			undoCnt = savedGame.getUndoCnt();
			undoList = savedGame.getUndoList();
			hintUsed = savedGame.isHintUsed();
			stateChanged = savedGame.isStateChanged();
		}
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
		fadeOutActive=false;
		
		init(selectedDclty,selectedLevel);
	}

	private void init(Difficulty selectedDclty, int selectedLevel){
		currentDclty = selectedDclty;
		currentLevel = selectedLevel;
		gameState = GameState.Ready;

		curUserOPTS.setLastDifficulty(selectedDclty);
		curUserOPTS.setFirstRun(false);
		Utils.saveGameOptions();
		
		if(currentLevel==1) {
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

		pltfrm = new Platform(midPanel);
		pltfrm.paintPlatform(ltank);

		argbFull.addAction(sequence(fadeOut(1f), visible(false)));
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

		arrowButton_Up.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Up_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.ArrowUp;
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Up_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton = GameButtons.None;
			};
		});
		
		arrowButton_Up.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f){
			@Override
			public boolean longPress(Actor actor, float x, float y) {
				longPressButton=GameButtons.ArrowUp;
				return super.longPress(actor, x, y);
			}
		});
		
		arrowButton_Right.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Right_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.ArrowRight;
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Right_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton = GameButtons.None;
			};
		});
		
		arrowButton_Right.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f){
			@Override
			public boolean longPress(Actor actor, float x, float y) {
				longPressButton=GameButtons.ArrowRight;
				return super.longPress(actor, x, y);
			}
		});
		
		arrowButton_Down.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Down_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.ArrowDown;
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Down_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton = GameButtons.None;
			};
		});
		
		arrowButton_Down.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f){
			@Override
			public boolean longPress(Actor actor, float x, float y) {
				longPressButton=GameButtons.ArrowDown;
				return super.longPress(actor, x, y);
			}
		});
		
		arrowButton_Left.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Left_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.ArrowLeft;
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				arrowButton_Left_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton = GameButtons.None;
			};
		});
		
		arrowButton_Left.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f){
			@Override
			public boolean longPress(Actor actor, float x, float y) {
				longPressButton=GameButtons.ArrowLeft;
				return super.longPress(actor, x, y);
			}
		});
		
		hintButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				hintButton_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				gameState = GameState.Hint;
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				hintButton_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
			};
		});
		
		undoButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				undoButton_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.UnDo;
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				undoButton_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton=GameButtons.None;
			};
		});
		
		undoButton.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f){
			@Override
			public boolean longPress(Actor actor, float x, float y) {
				if(undoCnt>0)
					longPressButton=GameButtons.UnDo;
				else
					longPressButton=GameButtons.None;
				return super.longPress(actor, x, y);
			}
		});
		
		fireButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				fireButton_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				pressedButton = GameButtons.Fire;
				return true;
			};

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				fireButton_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				longPressButton=GameButtons.None;
			};
		});
		
		fireButton.addListener(new ActorGestureListener(20, 0.4f, 0.3f, 0.15f){
			@Override
			public boolean longPress(Actor actor, float x, float y) {
				longPressButton=GameButtons.Fire;
				return super.longPress(actor, x, y);
			}
		});
		
		stage.addListener(new ActorGestureListener(){
			@Override
			public void fling(InputEvent event, float velocityX, float velocityY, int button) {
				if(curUserOPTS.isSwipeMove()){
					if(Math.abs(velocityX)>Math.abs(velocityY))
						if(velocityX>0)
							pressedButton = GameButtons.ArrowRight;
						else
							pressedButton = GameButtons.ArrowLeft;
					else 
						if(velocityY>0)
							pressedButton = GameButtons.ArrowDown;
						else
							pressedButton = GameButtons.ArrowUp;
					}
				super.fling(event, velocityX, velocityY, button);
			}
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
		
		stage.addListener(new DragListener() {

			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				if (curUserOPTS.isSwipeMove()) {
					float velocityX = x - dragStartX;
					float velocityY = y - dragStartY;
					if (Math.abs(velocityX) > 100 || Math.abs(velocityY) > 100) {
						if (Math.abs(velocityX) > Math.abs(velocityY)) {
							if (velocityX > 0)
								longPressButton = GameButtons.ArrowRight;
							else
								longPressButton = GameButtons.ArrowLeft;
						} else if (Math.abs(velocityX) < Math.abs(velocityY)) {
							if (velocityY > 0)
								longPressButton = GameButtons.ArrowDown;
							else
								longPressButton = GameButtons.ArrowUp;
						}
					}
				}
				super.drag(event, x, y, pointer);
			}

			@Override
			public void dragStart(InputEvent event, float x, float y, int pointer) {
				dragStartX = x;
				dragStartY = y;
				super.dragStart(event, x, y, pointer);
			}

			@Override
			public void dragStop(InputEvent event, float x, float y, int pointer) {
				dragStartX = 0;
				dragStartY = 0;
				super.dragStop(event, x, y, pointer);
			}
		});		
		
		ClickListener hackListener = new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(inTapSquare(780,460) && getTapCount()==3){
					gameState=GameState.Won;
				}
				super.clicked(event, x, y);
			}
		};
		hackListener.setTapSquareSize(20);
		stage.addListener(hackListener);
		
		setTouchEnabled(false);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if(!updateInProgress)
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
		updateInProgress=true;
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
		updateInProgress=false;
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
//						System.out.println("undop");
						undoInGame();
						stateChanged = true;
						break;
					case Fire:
						fireHero();
						break;
					default:
						if(longPressTimer<longPressReflexDelay){
							longPressTimer++;
						} else {
							longPressTimer=0;
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
//									System.out.println("undol");
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
		if(!pausedMenu.isVisible())
			pausedMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
	}

	private void updateDrowned() {
		if(!drownedMenu.isVisible())
			drownedMenu.addAction(sequence(visible(true), fadeIn(0.1f)));
	}

	private void updateDead() {
		if(!deadMenu.isVisible())
			deadMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
	}

	private void updateWon() {
		if(!wonMenu.isVisible())
			wonMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
	}

	private void updateHint() {
		if(!hintMenu.isVisible())
			hintMenu.addAction(sequence(visible(true), fadeIn(0.2f)));
	}

	private void updateFadeOut(float delta) {
		if(!fadeOutActive){
			switch (gameState) {
				case Menu:
				case Quit:
					fadeOutActive=true;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
							game.setScreen(new MenuScreen(game));
							return false;
						}
					}));
					break;
				case Restart:
					fadeOutActive=true;
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
							game.setScreen(new PlayScreen(game, currentDclty, currentLevel));
							return false;
						}
					}));
					break;
				case NextLevel:
					fadeOutActive=true;
					saveScores();
					argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
						@Override
						public boolean act(float delta) {
							if(currentLevel==curUserSCORE.getMaxPlayedLevel(currentDclty)) {
								game.setScreen(new LevelScreen(game,true));
							} else {
								game.setScreen(new LevelScreen(game,false));
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
		longPressButton = GameButtons.None;
		stateChanged = true;
	}

	public void undoInGame() {
		if (undoCnt > 0) {
			undoCnt--;
			ltank = null;
			ltank = undoList[undoCnt].clone();
		} else {
			longPressButton=GameButtons.None;
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
			if(pausedMenu.isVisible())
				pausedMenu.addAction(sequence(fadeOut(0.2f), visible(false)));
			gameState = GameState.Running;
		} else if (gameState == GameState.Hint) {
			if(hintMenu.isVisible())
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
		mainmenu();
	}

	@Override
	public void dispose() {
		if (playAtlas != null)
			playAtlas.dispose();
		if (dfltFont != null)
			dfltFont.dispose();
		if(lvlNmeFont != null)
			lvlNmeFont.dispose();
		lvl = null;
		pltfrm = null;
		ltank = null;
		undoList = null;
		System.gc();
		super.dispose();
	}

	private void saveScores() {
		Utils.saveUserScores(currentDclty, currentLevel, ltank.getTankMoves(), ltank.getTankShots(), hintUsed);
		if(currentLevel==curUserSCORE.getMaxPlayedLevel(currentDclty)) {
			Utils.saveUserScores(currentDclty, currentLevel+1, 0, 0, false);
			SaveThumbs st2 = new SaveThumbs(g, currentDclty, currentLevel);
			st2.run();
		}
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