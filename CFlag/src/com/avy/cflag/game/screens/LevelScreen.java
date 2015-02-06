package com.avy.cflag.game.screens;

import static com.avy.cflag.game.Constants.PLAYSTORE_PRO_LINK;
import static com.avy.cflag.game.MemStore.curUserOPTS;
import static com.avy.cflag.game.MemStore.curUserSCORE;
import static com.avy.cflag.game.MemStore.dummyLvlCntPerDCLTY;
import static com.avy.cflag.game.MemStore.lvlCntPerDCLTY;
import static com.badlogic.gdx.math.Interpolation.swingIn;
import static com.badlogic.gdx.math.Interpolation.swingOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.base.ImageString;
import com.avy.cflag.base.ImageString.PrintFormat;
import com.avy.cflag.base.TouchListener;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.EnumStore.Difficulty;
import com.avy.cflag.game.GameUtils;
import com.avy.cflag.game.MemStore;
import com.avy.cflag.game.PlayUtils;
import com.avy.cflag.game.elements.Level;
import com.avy.cflag.game.utils.LevelScore;
import com.avy.cflag.game.utils.SaveThumbs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class LevelScreen extends BackScreen {

	private final int colCnt = 10;
	private final int rowCnt = 4;
	private final int numButWidth = 40;
	private final int numButHeight = 40;

	private final int perPageLvlCnt = colCnt * rowCnt;
	private float rowGap = 0;
	private float colGap = 0;

	private final int totalDcltys = Difficulty.length();
	private final int dcltyGrpCnt[] = new int[totalDcltys];

	private final Image dcltyButtonUp[] = new Image[totalDcltys];
	private final Image dcltyButtonDown[] = new Image[totalDcltys];
	private final Image dcltyStr[] = new Image[totalDcltys];
	private final Group dcltyButtonPageGroup[] = new Group[totalDcltys];
	private final Image dcltyPageFrame[] = new Image[totalDcltys];
	private final Group dcltyNumPageGroup[][] = new Group[totalDcltys][];
	private final Group dcltyNumGroup[] = new Group[totalDcltys];
	private Group dcltyButtonGroup;

	private final TextureAtlas levelAtlas;
	private final BitmapFont scoreFont;

	private Image midButtonUp;
	private Image midButtonDown;
	private Group midButtonGroup;

	private Image leftButtonUp;
	private Image leftButtonDown;
	private Group leftButtonGroup;

	private Image rightButtonUp;
	private Image rightButtonDown;
	private Group rightButtonGroup;

	private Image titleStr;
	private Image playAgainStr;
	private Image playStr;
	private Image backStr;

	private Image unlockMsg;
	private Image buyMsg;

	private Difficulty selectedDclty;
	private int curPage;
	private int selectedLevel;

	private boolean isDragged;
	private float dragStartX;
	private float dragEndX;
	private boolean dragInProgress;

	private boolean touchEnabled;

	private Image thumbnail;
	private Image clickedNumButton;
	private final boolean unlockAnimate;
	
	private Group printData;
	
	private ImageString levelNoStr;
	private ImageString levelNameStr;
	private ImageString levelDcltyStr;
	private ImageString movesPlayedStr;
	private ImageString shotsFiredStr;
	private ImageString hintsUsedStr;

	private ImageString levelNoData;
	private ImageString levelNameData;
	private ImageString levelDcltyData;
	private ImageString movesPlayedData;
	private ImageString shotsFiredData;
	private ImageString hintsUsedData;


	public LevelScreen(final CFlagGame game, final boolean unlockAnimate) {
		super(game, true, true, false);

		for (int i = 0; i < dcltyGrpCnt.length; i++) {
			dcltyGrpCnt[i] = (dummyLvlCntPerDCLTY[i] / perPageLvlCnt) + ((dummyLvlCntPerDCLTY[i] % perPageLvlCnt) > 0 ? 1 : 0);
			dcltyNumPageGroup[i] = new Group[dcltyGrpCnt[i]];
		}

		levelAtlas = g.createImageAtlas("levelselect");
		scoreFont = g.createFont("salsa", 13, true);

		selectedDclty = curUserOPTS.getLastDifficulty();
		curPage = curUserSCORE.getMaxPlayedLevel(selectedDclty) / perPageLvlCnt;

		isDragged = false;
		dragStartX = 0;
		dragEndX = 0;
		dragInProgress = false;
		touchEnabled = true;
		this.unlockAnimate = unlockAnimate;
	}

	@Override
	public void show() {
		super.show();

		g.setImageAtlas(commonAtlas);

		midButtonUp = new Image(g.getFlipTexRegion("1_rectbuttonup"));
		midButtonUp.setPosition((bottomBar.getWidth() - midButtonUp.getWidth()) / 2, bottomBar.getY() + ((bottomBar.getHeight() - midButtonUp.getHeight()) / 2));
		midButtonDown = new Image(g.getFlipTexRegion("1_rectbuttondown"));
		midButtonDown.setPosition(midButtonUp.getX(), midButtonUp.getY());
		midButtonDown.setVisible(false);

		final int sideButtonMargin = 18;
		leftButtonUp = new Image(g.getFlipTexRegion("lefttributtonup"));
		leftButtonUp.setPosition(sideButtonMargin, bottomBar.getY() + ((bottomBar.getHeight() - leftButtonUp.getHeight()) / 2));
		leftButtonDown = new Image(g.getFlipTexRegion("lefttributtondown"));
		leftButtonDown.setPosition(leftButtonUp.getX(), leftButtonUp.getY());
		leftButtonDown.setVisible(false);

		rightButtonUp = new Image(g.getFlipTexRegion("righttributtonup"));
		rightButtonUp.setPosition(bottomBar.getWidth() - rightButtonUp.getWidth() - sideButtonMargin, bottomBar.getY() + ((bottomBar.getHeight() - rightButtonUp.getHeight()) / 2));
		rightButtonDown = new Image(g.getFlipTexRegion("righttributtondown"));
		rightButtonDown.setPosition(rightButtonUp.getX(), rightButtonUp.getY());
		rightButtonDown.setVisible(false);

		g.setImageAtlas(levelAtlas);

		titleStr = new Image(g.getFlipTexRegion("titlestr"));
		titleStr.setPosition((topBar.getWidth() - titleStr.getWidth()) / 2, (topBar.getHeight() - titleStr.getHeight()) / 2);
		playStr = new Image(g.getFlipTexRegion("playstr"));
		playStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		playAgainStr = new Image(g.getFlipTexRegion("playagainstr"));
		playAgainStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		backStr = new Image(g.getFlipTexRegion("backstr"));
		backStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		playStr.setVisible(false);
		playAgainStr.setVisible(false);
		backStr.setVisible(true);

		unlockMsg = new Image(g.getFlipTexRegion("unlockmsg"));
		unlockMsg.setPosition((game.getSrcWidth() - unlockMsg.getWidth()) / 2, (game.getSrcHeight() - unlockMsg.getHeight()) / 2);
		unlockMsg.setVisible(false);
		unlockMsg.getColor().a = 0f;

		buyMsg = new Image(g.getFlipTexRegion("buymsg"));
		buyMsg.setPosition((game.getSrcWidth() - buyMsg.getWidth()) / 2, (game.getSrcHeight() - buyMsg.getHeight()) / 2);
		buyMsg.setVisible(false);
		buyMsg.getColor().a = 0f;

		midButtonGroup = new Group();
		midButtonGroup.addActor(midButtonUp);
		midButtonGroup.addActor(midButtonDown);
		midButtonGroup.addActor(playStr);
		midButtonGroup.addActor(playAgainStr);
		midButtonGroup.addActor(backStr);

		midButtonUp.setName("midbutton");
		midButtonDown.setName("midbutton");
		playStr.setName("midbutton");
		playAgainStr.setName("midbutton");
		backStr.setName("midbutton");
		midButtonGroup.setName("midbutton");

		leftButtonGroup = new Group();
		leftButtonGroup.addActor(leftButtonUp);
		leftButtonGroup.addActor(leftButtonDown);
		if (curPage > 0) {
			leftButtonGroup.setVisible(true);
		} else {
			leftButtonGroup.setVisible(false);
		}

		rightButtonGroup = new Group();
		rightButtonGroup.addActor(rightButtonUp);
		rightButtonGroup.addActor(rightButtonDown);
		if (curPage < (dcltyGrpCnt[selectedDclty.ordinal()] - 1)) {
			rightButtonGroup.setVisible(true);
		} else {
			rightButtonGroup.setVisible(false);
		}
		stage.addActor(titleStr);
		stage.addActor(midButtonGroup);
		stage.addActor(leftButtonGroup);
		stage.addActor(rightButtonGroup);
		
		levelNoStr = new ImageString    ("Level No      : ", scoreFont, Color.YELLOW, PrintFormat.Normal_Left);
		levelNameStr = new ImageString  ("Level Name : ", scoreFont, Color.YELLOW, PrintFormat.Normal_Left);
		levelDcltyStr = new ImageString ("Difficulty    : ", scoreFont, Color.YELLOW, PrintFormat.Normal_Left);
		levelNoData = new ImageString("1", scoreFont, Color.WHITE, PrintFormat.Normal_Left);
		levelNameData = new ImageString("1", scoreFont, Color.WHITE, PrintFormat.Normal_Left);
		levelDcltyData = new ImageString("1", scoreFont, Color.WHITE, PrintFormat.Normal_Left);
		
		movesPlayedStr = new ImageString("Moves Played : ", scoreFont, Color.YELLOW, PrintFormat.Normal_Left);
		shotsFiredStr = new ImageString ("Shots Fired    : ", scoreFont, Color.YELLOW, PrintFormat.Normal_Left);
		hintsUsedStr = new ImageString  ("Hint Used       : ", scoreFont, Color.YELLOW, PrintFormat.Normal_Left);
		movesPlayedData = new ImageString("1", scoreFont, Color.WHITE, PrintFormat.Normal_Left);
		shotsFiredData = new ImageString("1", scoreFont, Color.WHITE, PrintFormat.Normal_Left);
		hintsUsedData = new ImageString("1", scoreFont, Color.WHITE, PrintFormat.Normal_Left);
		
		levelNoStr.setPosition(60,420);
		levelNameStr.setPosition(60,420+15); 
		levelDcltyStr.setPosition(60,420+30); 
		levelNoData.setPosition(60+90,420); 
		levelNameData.setPosition(60+90,420+15); 
		levelDcltyData.setPosition(60+90,420+30); 
		
		movesPlayedStr.setPosition(560,420); 
		shotsFiredStr.setPosition(560,420+15); 
		hintsUsedStr.setPosition(560,420+30); 
		movesPlayedData.setPosition(560+100,420); 
		shotsFiredData.setPosition(560+100,420+15); 
		hintsUsedData.setPosition(560+100,420+30); 
		
		printData = new Group();
		printData.addActor(levelNoStr);
		printData.addActor(levelNameStr);
		printData.addActor(levelDcltyStr);
		printData.addActor(levelNoData);
		printData.addActor(levelNameData);
		printData.addActor(levelDcltyData);
		printData.addActor(movesPlayedStr);
		printData.addActor(shotsFiredStr);
		printData.addActor(hintsUsedStr);
		printData.addActor(movesPlayedData);
		printData.addActor(shotsFiredData);
		printData.addActor(hintsUsedData);
		printData.setVisible(false);
		
		stage.addActor(printData);
		
		dcltyButtonGroup = new Group();

		int idx = 0;
		for (int dcltyNo = 0; dcltyNo < totalDcltys; dcltyNo++) {

			final Difficulty dclty = PlayUtils.getDifficultyByIdx(idx);
			if (dummyLvlCntPerDCLTY[dcltyNo] > 0) {
				dcltyButtonUp[idx] = new Image(g.getFlipTexRegion("buttonup"));

				if (idx == 0) {
					dcltyButtonUp[idx].setPosition((topBar.getWidth() - (dcltyButtonUp[idx].getWidth() * 5)) / 2, topBar.getHeight() + 20);
				} else {
					dcltyButtonUp[idx].setPosition((dcltyButtonUp[idx - 1].getX() + dcltyButtonUp[idx - 1].getWidth()) - 1, dcltyButtonUp[idx - 1].getY());
				}

				dcltyButtonDown[idx] = new Image(g.getFlipTexRegion(dclty.name().toLowerCase() + "buttondown"));
				dcltyButtonDown[idx].setPosition(dcltyButtonUp[idx].getX(), dcltyButtonUp[idx].getY());
				dcltyButtonDown[idx].setVisible(false);

				dcltyStr[idx] = new Image(g.getFlipTexRegion(dclty.name().toLowerCase() + "str"));
				dcltyStr[idx].setPosition(dcltyButtonUp[idx].getX(), dcltyButtonUp[idx].getY());
				dcltyPageFrame[idx] = new Image(g.getFlipTexRegion(dclty.name().toLowerCase() + "frame"));
				dcltyPageFrame[idx].setPosition(20, dcltyButtonUp[idx].getY() + dcltyButtonUp[idx].getHeight());
				dcltyPageFrame[idx].setSize(topBar.getWidth() - 40, bottomBar.getY() - dcltyPageFrame[idx].getY() - 20);
				dcltyPageFrame[idx].getColor().a = 0.5f;

				dcltyButtonPageGroup[idx] = new Group();
				dcltyButtonPageGroup[idx].addActor(dcltyButtonUp[idx]);
				dcltyButtonPageGroup[idx].addActor(dcltyButtonDown[idx]);
				dcltyButtonPageGroup[idx].addActor(dcltyStr[idx]);
				dcltyButtonPageGroup[idx].setVisible(true);
				dcltyButtonPageGroup[idx].setName(Integer.toString(idx));

				dcltyButtonPageGroup[idx].addListener(new TouchListener() {
					@Override
					public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
						final Group tmp = (Group) event.getListenerActor();
						final int tmpDcltyNo = Integer.parseInt(tmp.getName());

						if (!dcltyButtonDown[tmpDcltyNo].isVisible()) {
							for (int i = 0; i < totalDcltys; i++) {
								if (i == tmpDcltyNo) {
									dcltyButtonDown[i].addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
									dcltyPageFrame[i].addAction(sequence(alpha(0), visible(true), alpha(0.5f)));
									curPage = curUserSCORE.getMaxPlayedLevel(selectedDclty) / perPageLvlCnt;
									selectedDclty = PlayUtils.getDifficultyByIdx(i);
									dcltyNumPageGroup[i][curPage].setVisible(true);
									dcltyNumGroup[i].addAction(sequence(alpha(0), visible(true), alpha(1f)));
									if (curPage > 0) {
										leftButtonGroup.setVisible(true);
									} else {
										leftButtonGroup.setVisible(false);
									}

									if (curPage < (dcltyGrpCnt[selectedDclty.ordinal()] - 1)) {
										rightButtonGroup.setVisible(true);
									} else {
										rightButtonGroup.setVisible(false);
									}
								} else {
									dcltyButtonDown[i].addAction(sequence(fadeOut(0.2f), visible(false)));
									dcltyPageFrame[i].addAction(sequence(fadeOut(0.2f), visible(false)));
									dcltyNumGroup[i].addAction(sequence(fadeOut(0.2f), visible(false)));
								}
							}
						}
						return super.touchDown(event, x, y, pointer, button);
					}
				});

				colGap = (dcltyPageFrame[idx].getWidth() - (colCnt * numButWidth)) / (colCnt + 1);
				rowGap = (dcltyPageFrame[idx].getHeight() - (rowCnt * numButHeight)) / (rowCnt + 1);
				dcltyNumGroup[idx] = new Group();
				int l = 1;
				for (int i = 0; i < dcltyNumPageGroup[idx].length; i++) {
					dcltyNumPageGroup[idx][i] = new Group();
					for (int j = 0; j < rowCnt; j++) {
						for (int k = 0; k < colCnt; k++) {
							if (l <= dummyLvlCntPerDCLTY[idx]) {
								final Group numButtonGroup = new Group();
								final Image numButtonUp = new Image(g.getFlipTexRegion("numbuttonup"));
								numButtonUp.setPosition(dcltyPageFrame[idx].getX() + colGap + (k * (numButtonUp.getWidth() + colGap)),
										dcltyPageFrame[idx].getY() + rowGap + (j * (numButtonUp.getHeight() + rowGap)));
								numButtonUp.setName("buttonUp");

								final Image numButtonDown = new Image(g.getFlipTexRegion("numbuttondown"));
								numButtonDown.setPosition(numButtonUp.getX(), numButtonUp.getY());
								numButtonDown.setVisible(false);
								numButtonDown.setName("buttonDown");

								numButtonGroup.addActor(numButtonUp);
								numButtonGroup.addActor(numButtonDown);
								numButtonGroup.setName(Integer.toString(l));

								final char numStr[] = Integer.toString(l).toCharArray();

								Image numStr1 = null, numStr2 = null, numStr3 = null;
								final Group numStrGroup = new Group();
								float numStrLen = 0;

								if (numStr.length > 0) {
									numStr1 = new Image(g.getFlipTexRegion("" + numStr[0]));
									numStrLen = numStr1.getWidth();
								}
								if (numStr.length > 1) {
									numStr2 = new Image(g.getFlipTexRegion("" + numStr[1]));
									numStrLen = numStrLen + numStr2.getWidth();
								}
								if (numStr.length > 2) {
									numStr3 = new Image(g.getFlipTexRegion("" + numStr[2]));
									numStrLen = numStrLen + numStr3.getWidth();
								}

								if (numStr.length > 0) {
									numStr1.setPosition(numButtonUp.getX() + ((numButtonUp.getWidth() - numStrLen) / 2), numButtonUp.getY() + ((numButtonUp.getHeight() - numStr1.getHeight()) / 2));
									numStrGroup.addActor(numStr1);
								}
								if (numStr.length > 1) {
									numStr2.setPosition(numStr1.getX() + numStr1.getWidth(), numStr1.getY());
									numStrGroup.addActor(numStr2);
								}
								if (numStr.length > 2) {
									numStr3.setPosition(numStr2.getX() + numStr2.getWidth(), numStr2.getY());
									numStrGroup.addActor(numStr3);
								}
								numButtonGroup.addActor(numStrGroup);

								final Image lockStr = new Image(g.getFlipTexRegion("lock"));
								lockStr.setPosition(numButtonUp.getX(), numButtonUp.getY());
								numButtonGroup.addActor(lockStr);

								if (l <= curUserSCORE.getMaxPlayedLevel(dclty)) {
									if (unlockAnimate && (l == curUserSCORE.getMaxPlayedLevel(dclty))) {
										numStrGroup.setVisible(false);
										lockStr.setVisible(true);
										lockStr.setOrigin(lockStr.getWidth() / 2, lockStr.getHeight() / 2);
										lockStr.addAction(delay(1f,
												sequence(scaleBy(0.1f, 0.1f), repeat(4, rotateBy(360f, 0.1f)), parallel(repeat(4, rotateBy(360f, 0.1f)), fadeOut(1f)), visible(false))));
										numStrGroup.addAction(delay(2f, sequence(visible(true), fadeIn(1f))));
									} else {
										numStrGroup.setVisible(true);
										lockStr.setVisible(false);
									}
								} else {
									numStrGroup.setVisible(false);
									lockStr.setVisible(true);
									numButtonGroup.getColor().a = 0.7f;
								}

								dcltyNumPageGroup[idx][i].addActor(numButtonGroup);
								numButtonGroup.addListener(new TouchListener() {
									@Override
									public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
										final Group tmp = (Group) event.getListenerActor();
										final Image buttonDown = (Image) tmp.findActor("buttonDown");
										buttonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
										return super.touchDown(event, x, y, pointer, button);
									}

									@Override
									public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
										final Group tmp = (Group) event.getListenerActor();
										clickedNumButton = (Image) tmp.findActor("buttonDown");
										clickedNumButton.addAction(sequence(fadeOut(0.2f), visible(false)));
										if (!isDragged) {
											selectedLevel = Integer.parseInt(tmp.getName());
											if (selectedLevel <= curUserSCORE.getMaxPlayedLevel(selectedDclty)) {
												swingOutThumbnail();
											} else if (selectedLevel <= lvlCntPerDCLTY[selectedDclty.ordinal()]) {
												if (!unlockMsg.isVisible()) {
													dcltyNumGroup[selectedDclty.ordinal()].addAction(alpha(0.4f));
													unlockMsg.addAction(sequence(visible(true), fadeIn(0.5f)));
												}
											} else {
												if (!unlockMsg.isVisible() && !buyMsg.isVisible()) {
													dcltyNumGroup[selectedDclty.ordinal()].addAction(alpha(0.4f));
													buyMsg.addAction(sequence(visible(true), fadeIn(0.5f)));
												}
											}
										}
									}
								});
							}
							l++;
						}
					}

					if ((idx == selectedDclty.ordinal()) && (i == curPage)) {
						dcltyNumPageGroup[idx][i].setVisible(true);
					} else {
						dcltyNumPageGroup[idx][i].setVisible(false);
					}

					dcltyNumGroup[idx].addActor(dcltyNumPageGroup[idx][i]);

					if (idx == selectedDclty.ordinal()) {
						dcltyButtonDown[idx].setVisible(true);
						dcltyPageFrame[idx].setVisible(true);
						dcltyNumGroup[idx].setVisible(true);
					} else {
						dcltyNumGroup[idx].setVisible(false);
						dcltyPageFrame[idx].setVisible(false);
					}
				}
				dcltyButtonGroup.addActor(dcltyButtonPageGroup[idx]);
				stage.addActor(dcltyPageFrame[idx]);
				stage.addActor(dcltyNumGroup[idx]);
				idx++;
			}
		}
		stage.addActor(dcltyButtonGroup);
		stage.addActor(unlockMsg);
		stage.addActor(buyMsg);
		stage.addActor(argbFull);

		midButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				midButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				midButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				if (backStr.isVisible()) {
					argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
						@Override
						public void run() {
							game.setScreen(new MenuScreen(game));
						}
					})));
				} else {
					argbFull.addAction(sequence(visible(true), new Action() {
						@Override
						public boolean act(final float delta) {
							thumbnail.addAction(fadeOut(0.1f));
							return true;
						}
					}, new Action() {
						@Override
						public boolean act(final float delta) {
							thumbnail.clearActions();
							thumbnail.remove();
							return true;
						}
					}, fadeIn(1f), run(new Runnable() {
						@Override
						public void run() {
							game.setScreen(new PlayScreen(game, selectedDclty, selectedLevel));
						}
					})));
				}
			}
		});

		leftButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				leftButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				leftButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				if (!isDragged) {
					swipeRight();
				}
			}
		});

		rightButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				rightButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				rightButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				if (!isDragged) {
					swipeLeft();
				}
			}
		});

		buyMsg.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				Gdx.net.openURI(PLAYSTORE_PRO_LINK);
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		stage.addListener(new DragListener() {
			@Override
			public void dragStart(final InputEvent event, final float x, final float y, final int pointer) {
				dragStartX = x;
				isDragged = true;
				super.dragStart(event, x, y, pointer);
			}

			@Override
			public void dragStop(final InputEvent event, final float x, final float y, final int pointer) {
				super.dragStop(event, x, y, pointer);
				dragEndX = x;
				if (dragStartX > dragEndX) {
					swipeLeft();
				} else if (dragStartX < dragEndX) {
					swipeRight();
				}
				isDragged = false;
			}
		});

		stage.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				if (unlockMsg.isVisible()) {
					unlockMsg.addAction(sequence(fadeOut(0.5f), visible(false)));
					dcltyNumGroup[selectedDclty.ordinal()].addAction(alpha(1f));
				}
				if (buyMsg.isVisible()) {
					buyMsg.addAction(sequence(fadeOut(0.5f), visible(false)));
					dcltyNumGroup[selectedDclty.ordinal()].addAction(alpha(1f));
				}
				if (printData.isVisible()) {
					if (!((event.getTarget().getName() != null) && (event.getTarget().getName().equalsIgnoreCase("thumbnail") || event.getTarget().getName().equalsIgnoreCase("midbutton")))) {
						swingInThumbnail();
					}
				}
				return true;
			}

			@Override
			public boolean keyDown(final InputEvent event, final int keycode) {
				if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
					if (!printData.isVisible()) {
						argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
							@Override
							public void run() {
								game.setScreen(new MenuScreen(game));
							}
						})));
					} else {
						swingInThumbnail();
					}
				}
				return true;
			}
		});
		final ClickListener hackListener = new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(final InputEvent event, final float x, final float y) {
				if (inTapSquare(780, 20) && (getTapCount() == 3)) {
					final int maxUnlockedLevel = curUserSCORE.getMaxPlayedLevel(selectedDclty);
					if ((maxUnlockedLevel + 1) <= lvlCntPerDCLTY[selectedDclty.ordinal()]) {
						curUserOPTS.setlastDclty(selectedDclty);
						GameUtils.saveGameOptions();
						GameUtils.saveUserScores(selectedDclty, maxUnlockedLevel + 1, 0, 0, 0);
						final SaveThumbs st2 = new SaveThumbs(g, selectedDclty, maxUnlockedLevel + 1);
						st2.setPlayAtlas();
						st2.run();
						argbFull.addAction(sequence(visible(true), fadeIn(1f), new Action() {
							@Override
							public boolean act(final float delta) {
								game.setScreen(new LevelScreen(game, true));
								return false;
							}
						}));

					}
				}
				super.clicked(event, x, y);
			}
		};
		hackListener.setTapSquareSize(20);
		stage.addListener(hackListener);
	}

	@Override
	public void render(final float delta) {
		super.render(delta);
//		if (printLevelData) {
//			final LevelScore gScore = MemStore.curUserSCORE.getScores(selectedDclty, selectedLevel);
//			batch.begin();
//			final String lvlDetailsLeft = "Level No      : \nLevel Name : \nDifficulty    : ";
//			final String lvlDetailsRight = selectedLevel + "\n" + selectedLevelName + "\n" + selectedDclty.name();
//			String scoreDetailsLeft = "Moves Played : \nShots Fired    : \nHint Used       : ";
//			String scoreDetailsRight = "0\n0\nNo";
//			if (gScore != null) {
//				scoreDetailsLeft = "Moves Played : \nShots Fired    : \nHint Used       : ";
//				scoreDetailsRight = gScore.getMoves() + "\n" + gScore.getShots() + "\n" + gScore.getHintsUsed();
//			}
//			g.drawMultiLineString(lvlDetailsLeft, 100, 433, Color.YELLOW);
//			g.drawMultiLineString(lvlDetailsRight, 200, 433, Color.WHITE);
//
//			g.drawMultiLineString(scoreDetailsLeft, 600, 433, Color.YELLOW);
//			g.drawMultiLineString(scoreDetailsRight, 670, 433, Color.WHITE);
//
//			batch.end();
//		}
	}

	@Override
	public void dispose() {
		if (levelAtlas != null) {
			levelAtlas.dispose();
		}
		if (scoreFont != null) {
			scoreFont.dispose();
		}
		super.dispose();
	}

	public void swipeLeft() {
		final int selectedDcltyIdx = selectedDclty.ordinal();
		if (touchEnabled && !dragInProgress && rightButtonGroup.isVisible()) {
			if (curPage < (dcltyGrpCnt[selectedDcltyIdx] - 1)) {
				dcltyNumPageGroup[selectedDcltyIdx][curPage].addAction(sequence());
				dcltyNumPageGroup[selectedDcltyIdx][curPage].addAction(sequence(run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = true;
					}
				}), moveTo(-game.getSrcWidth(), dcltyNumPageGroup[selectedDcltyIdx][curPage].getY(), 1f), visible(false)));

				dcltyNumPageGroup[selectedDclty.ordinal()][curPage + 1].addAction(sequence(alpha(0), moveTo(0, dcltyNumPageGroup[selectedDcltyIdx][curPage + 1].getY()), visible(true), fadeIn(1f),
						run(new Runnable() {

							@Override
							public void run() {
								dragInProgress = false;
							}
						})));
				curPage++;
			}
		}
		if (curPage >= (dcltyGrpCnt[selectedDcltyIdx] - 1)) {
			rightButtonGroup.setVisible(false);
		}
		if (curPage > 0) {
			leftButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
		}
	}

	public void swipeRight() {
		final int selectedDcltyIdx = selectedDclty.ordinal();
		if (touchEnabled && !dragInProgress && leftButtonGroup.isVisible()) {
			if (curPage > 0) {
				dcltyNumPageGroup[selectedDcltyIdx][curPage].addAction(sequence());
				dcltyNumPageGroup[selectedDcltyIdx][curPage].addAction(sequence(run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = true;
					}
				}), moveTo(game.getSrcWidth(), dcltyNumPageGroup[selectedDcltyIdx][curPage].getY(), 1f), visible(false)));

				dcltyNumPageGroup[selectedDcltyIdx][curPage - 1].addAction(sequence(alpha(0), moveTo(0, dcltyNumPageGroup[selectedDcltyIdx][curPage - 1].getY()), visible(true), fadeIn(1f),
						run(new Runnable() {

							@Override
							public void run() {
								dragInProgress = false;
							}
						})));
				curPage--;
			}
		}
		if (curPage <= 0) {
			leftButtonGroup.setVisible(false);
		}
		if (curPage < (dcltyGrpCnt[selectedDcltyIdx] - 1)) {
			rightButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
		}
	}

	public void swingOutThumbnail() {
		setTouchEnabled(false);

		final Level lvl = new Level();
		lvl.loadLevel(selectedDclty, selectedLevel);
		thumbnail = new Image(g.getThumbTexRegion(selectedDclty, selectedLevel));
		thumbnail.setPosition(clickedNumButton.getX(), clickedNumButton.getY());
		thumbnail.setOrigin(thumbnail.getWidth() / 2, thumbnail.getHeight() / 2);
		thumbnail.setName("thumbnail");
		final float tempWidth = thumbnail.getWidth();
		final float tempHeight = thumbnail.getHeight();
		thumbnail.setSize(clickedNumButton.getWidth(), clickedNumButton.getHeight());
		stage.addActor(thumbnail);
		thumbnail.clearActions();
		thumbnail.addAction(parallel(forever(rotateBy(1f)), sizeTo(tempWidth, tempHeight, 1f), moveTo((topBar.getWidth() - tempWidth) / 2, (480 - tempHeight) / 2, 1f, swingOut)));

		thumbnail.addListener(new TouchListener() {
			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				midButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				if (backStr.isVisible()) {
					argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
						@Override
						public void run() {
							game.setScreen(new MenuScreen(game));
						}
					})));
				} else {
					argbFull.addAction(sequence(visible(true), new Action() {
						@Override
						public boolean act(final float delta) {
							thumbnail.addAction(fadeOut(0.1f));
							return true;
						}
					}, new Action() {
						@Override
						public boolean act(final float delta) {
							thumbnail.clearActions();
							thumbnail.remove();
							return true;
						}
					}, fadeIn(1f), run(new Runnable() {
						@Override
						public void run() {
							game.setScreen(new PlayScreen(game, selectedDclty, selectedLevel));
						}
					})));
				}
			}
		});

		dcltyNumGroup[selectedDclty.ordinal()].addAction(alpha(0.1f));
		leftButtonGroup.addAction(alpha(0f));
		rightButtonGroup.addAction(alpha(0f));

		if (selectedLevel < curUserSCORE.getMaxPlayedLevel(selectedDclty)) {
			backStr.addAction(visible(false));
			playStr.addAction(visible(false));
			playAgainStr.addAction(visible(true));
		} else {
			backStr.addAction(visible(false));
			playStr.addAction(visible(true));
			playAgainStr.addAction(visible(false));
		}
		printLevelData(selectedDclty, selectedLevel, lvl.getLvlName());
	}

	public void swingInThumbnail() {
		thumbnail.setOrigin(clickedNumButton.getWidth() / 2, clickedNumButton.getWidth() / 2);
		thumbnail.clearActions();
		thumbnail.addAction(sequence(
				parallel(repeat(40, rotateBy(-1f)), sizeTo(clickedNumButton.getWidth(), clickedNumButton.getHeight(), 1f), moveTo(clickedNumButton.getX(), clickedNumButton.getY(), 1f, swingIn)),
				fadeOut(0.2f), visible(false), run(new Runnable() {
					@Override
					public void run() {
						thumbnail.clearActions();
						thumbnail.remove();
						setTouchEnabled(true);
					}
				})));
		dcltyNumGroup[selectedDclty.ordinal()].addAction(alpha(1f));
		leftButtonGroup.addAction(alpha(1f));
		rightButtonGroup.addAction(alpha(1f));

		playStr.addAction(visible(false));
		playAgainStr.addAction(visible(false));
		backStr.addAction(visible(true));
		printData.setVisible(false);
	}

	private void setTouchEnabled(final boolean isEnabled) {
		if (isEnabled) {
			touchEnabled = true;
			dcltyNumGroup[selectedDclty.ordinal()].setTouchable(Touchable.enabled);
			dcltyButtonGroup.setTouchable(Touchable.enabled);
			leftButtonGroup.setTouchable(Touchable.enabled);
			rightButtonGroup.setTouchable(Touchable.enabled);

		} else {
			touchEnabled = false;
			dcltyNumGroup[selectedDclty.ordinal()].setTouchable(Touchable.disabled);
			dcltyButtonGroup.setTouchable(Touchable.disabled);
			leftButtonGroup.setTouchable(Touchable.disabled);
			rightButtonGroup.setTouchable(Touchable.disabled);
		}
	}
	
	private void printLevelData(Difficulty selectedDclty, int selectedLevel, String levelName){
		LevelScore gScore = MemStore.curUserSCORE.getScores(selectedDclty, selectedLevel);
		levelNoData.setPrintStr(""+selectedLevel);
		levelNameData.setPrintStr(levelName);
		levelDcltyData.setPrintStr(selectedDclty.name());
		movesPlayedData.setPrintStr("" + gScore.getMoves());
		shotsFiredData.setPrintStr("" + gScore.getShots());
		hintsUsedData.setPrintStr("" + gScore.getHintsUsed());
		printData.setVisible(true);
	}
}
