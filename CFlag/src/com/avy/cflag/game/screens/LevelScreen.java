package com.avy.cflag.game.screens;

import static com.avy.cflag.game.MemStore.curUserOPTS;
import static com.avy.cflag.game.MemStore.curUserSCORE;
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

import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.MemStore;
import com.avy.cflag.game.MemStore.Difficulty;
import com.avy.cflag.game.Utils;
import com.avy.cflag.game.elements.Level;
import com.avy.cflag.game.utils.LevelScore;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

	private Image dcltyButtonUp[] = new Image[totalDcltys];
	private Image dcltyButtonDown[] = new Image[totalDcltys];
	private Image dcltyStr[] = new Image[totalDcltys];
	private Group dcltyButtonPageGroup[] = new Group[totalDcltys];
	private Image dcltyPageFrame[] = new Image[totalDcltys];
	private Group dcltyNumPageGroup[][] = new Group[totalDcltys][];
	private Group dcltyNumGroup[] = new Group[totalDcltys];
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

	private Difficulty selectedDclty;
	private int curPage;
	private int selectedLevel;

	private float dragStartX;
	private float dragEndX;
	private boolean dragInProgress;

	private boolean printLevelData;

	private Image thumbnail;
	private Image clickedNumButton;
	private boolean unlockAnimate;

	public LevelScreen(CFlagGame game, boolean unlockAnimate) {
		super(game, true, true, false);

		for (int i = 0; i < dcltyGrpCnt.length; i++) {
			dcltyGrpCnt[i] = lvlCntPerDCLTY[i] / perPageLvlCnt + (lvlCntPerDCLTY[i] % perPageLvlCnt > 0 ? 1 : 0);
			dcltyNumPageGroup[i] = new Group[dcltyGrpCnt[i]];
		}

		levelAtlas = g.createImageAtlas("levelselect");
		scoreFont = g.createFont("salsa", 13, true);

		selectedDclty = curUserOPTS.getLastDifficulty();
		curPage = curUserSCORE.getMaxPlayedLevel(selectedDclty) / perPageLvlCnt;

		printLevelData = false;
		dragStartX = 0;
		dragEndX = 0;
		dragInProgress = false;
		this.unlockAnimate = unlockAnimate;
	}

	@Override
	public void show() {
		super.show();

		g.setImageAtlas(commonAtlas);
		g.setFont(scoreFont);

		midButtonUp = new Image(g.getFlipTexRegion("rectbuttonup"));
		midButtonUp.setPosition((bottomBar.getWidth() - midButtonUp.getWidth()) / 2, bottomBar.getY() + (bottomBar.getHeight() - midButtonUp.getHeight()) / 2);
		midButtonDown = new Image(g.getFlipTexRegion("rectbuttondown"));
		midButtonDown.setPosition(midButtonUp.getX(), midButtonUp.getY());
		midButtonDown.setVisible(false);

		final int sideButtonMargin = 18;
		leftButtonUp = new Image(g.getFlipTexRegion("lefttributtonup"));
		leftButtonUp.setPosition(sideButtonMargin, bottomBar.getY() + (bottomBar.getHeight() - leftButtonUp.getHeight()) / 2);
		leftButtonDown = new Image(g.getFlipTexRegion("lefttributtondown"));
		leftButtonDown.setPosition(leftButtonUp.getX(), leftButtonUp.getY());
		leftButtonDown.setVisible(false);

		rightButtonUp = new Image(g.getFlipTexRegion("righttributtonup"));
		rightButtonUp.setPosition(bottomBar.getWidth() - rightButtonUp.getWidth() - sideButtonMargin, bottomBar.getY() + (bottomBar.getHeight() - rightButtonUp.getHeight()) / 2);
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
		unlockMsg.setPosition((game.getSrcWidth()-unlockMsg.getWidth())/2, (game.getSrcHeight()-unlockMsg.getHeight())/2);
		unlockMsg.setVisible(false);
		unlockMsg.getColor().a=0f;
		
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
		if (curPage > 0)
			leftButtonGroup.setVisible(true);
		else
			leftButtonGroup.setVisible(false);

		rightButtonGroup = new Group();
		rightButtonGroup.addActor(rightButtonUp);
		rightButtonGroup.addActor(rightButtonDown);
		if (curPage < dcltyGrpCnt[selectedDclty.ordinal()] - 1)
			rightButtonGroup.setVisible(true);
		else
			rightButtonGroup.setVisible(false);
		stage.addActor(titleStr);
		stage.addActor(midButtonGroup);
		stage.addActor(leftButtonGroup);
		stage.addActor(rightButtonGroup);

		dcltyButtonGroup = new Group();

		int idx = 0;
		for (int dcltyNo = 0; dcltyNo < totalDcltys; dcltyNo++) {

			Difficulty dclty = Utils.getDifficultyByIdx(idx);
			if (lvlCntPerDCLTY[dcltyNo] > 0) {
				dcltyButtonUp[idx] = new Image(g.getFlipTexRegion("buttonup"));

				if (idx == 0)
					dcltyButtonUp[idx].setPosition((topBar.getWidth() - (dcltyButtonUp[idx].getWidth() * 5)) / 2, topBar.getHeight() + 20);
				else
					dcltyButtonUp[idx].setPosition(dcltyButtonUp[idx - 1].getX() + dcltyButtonUp[idx - 1].getWidth() - 1, dcltyButtonUp[idx - 1].getY());

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

				dcltyButtonPageGroup[idx].addListener(new InputListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						Group tmp = (Group) event.getListenerActor();
						int tmpDcltyNo = Integer.parseInt(tmp.getName());

						if (!dcltyButtonDown[tmpDcltyNo].isVisible()) {
							for (int i = 0; i < totalDcltys; i++) {
								if (i == tmpDcltyNo) {
									dcltyButtonDown[i].addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
									dcltyPageFrame[i].addAction(sequence(alpha(0), visible(true), alpha(0.5f)));
									curPage = curUserSCORE.getMaxPlayedLevel(selectedDclty) / perPageLvlCnt;
									selectedDclty = Utils.getDifficultyByIdx(i);
									dcltyNumPageGroup[i][curPage].setVisible(true);
									dcltyNumGroup[i].addAction(sequence(alpha(0), visible(true), alpha(1f)));
									if (curPage > 0)
										leftButtonGroup.setVisible(true);
									else
										leftButtonGroup.setVisible(false);

									if (curPage < dcltyGrpCnt[selectedDclty.ordinal()] - 1)
										rightButtonGroup.setVisible(true);
									else
										rightButtonGroup.setVisible(false);
								} else {
									dcltyButtonDown[i].addAction(sequence(fadeOut(0.2f), visible(false)));
									dcltyPageFrame[i].addAction(sequence(fadeOut(0.2f), visible(false)));
									dcltyNumGroup[i].addAction(sequence(fadeOut(0.2f), visible(false)));
								}
							}
						}
						return true;
					}
				});

				colGap = (dcltyPageFrame[idx].getWidth() - colCnt * numButWidth) / (colCnt + 1);
				rowGap = (dcltyPageFrame[idx].getHeight() - rowCnt * numButHeight) / (rowCnt + 1);
				dcltyNumGroup[idx] = new Group();
				int l = 1;
				for (int i = 0; i < dcltyNumPageGroup[idx].length; i++) {
					dcltyNumPageGroup[idx][i] = new Group();
					for (int j = 0; j < rowCnt; j++) {
						for (int k = 0; k < colCnt; k++) {
							if (l <= lvlCntPerDCLTY[idx]) {
								Group numButtonGroup = new Group();
								Image numButtonUp = new Image(g.getFlipTexRegion("numbuttonup"));
								numButtonUp.setPosition(dcltyPageFrame[idx].getX() + colGap + k * (numButtonUp.getWidth() + colGap), dcltyPageFrame[idx].getY() + rowGap + j * (numButtonUp.getHeight() + rowGap));
								numButtonUp.setName("buttonUp");

								Image numButtonDown = new Image(g.getFlipTexRegion("numbuttondown"));
								numButtonDown.setPosition(numButtonUp.getX(), numButtonUp.getY());
								numButtonDown.setVisible(false);
								numButtonDown.setName("buttonDown");

								numButtonGroup.addActor(numButtonUp);
								numButtonGroup.addActor(numButtonDown);
								numButtonGroup.setName(Integer.toString(l));

								char numStr[] = Integer.toString(l).toCharArray();

								Image numStr1 = null, numStr2 = null, numStr3 = null;
								Group numStrGroup = new Group();
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
									numStr1.setPosition(numButtonUp.getX() + (numButtonUp.getWidth() - numStrLen) / 2, numButtonUp.getY() + (numButtonUp.getHeight() - numStr1.getHeight()) / 2);
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

								Image lockStr = new Image(g.getFlipTexRegion("lock"));
								lockStr.setPosition(numButtonUp.getX(), numButtonUp.getY());
								numButtonGroup.addActor(lockStr);

								if (l <= curUserSCORE.getMaxPlayedLevel(dclty)) {
									if(unlockAnimate && l==curUserSCORE.getMaxPlayedLevel(dclty)){
										numStrGroup.setVisible(false);
										lockStr.setVisible(true);
										lockStr.setOrigin(lockStr.getWidth()/2, lockStr.getHeight()/2);
										lockStr.addAction(delay(1f,sequence(scaleBy(0.1f, 0.1f),repeat(4,rotateBy(360f,0.1f)),parallel(repeat(4,rotateBy(360f,0.1f)),fadeOut(1f)),visible(false))));
										numStrGroup.addAction(delay(2f,sequence(visible(true),fadeIn(1f))));
									} else {
										numStrGroup.setVisible(true);
										lockStr.setVisible(false);
									}
								} else {
									numStrGroup.setVisible(false);
									lockStr.setVisible(true);
									numButtonGroup.getColor().a=0.7f;
								}

								dcltyNumPageGroup[idx][i].addActor(numButtonGroup);
								numButtonGroup.addListener(new InputListener() {
									@Override
									public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
										Group tmp = (Group) event.getListenerActor();
										Image buttonDown = (Image) tmp.findActor("buttonDown");
										buttonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
										return true;
									}

									@Override
									public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
										Group tmp = (Group) event.getListenerActor();
										clickedNumButton = (Image) tmp.findActor("buttonDown");
										clickedNumButton.addAction(sequence(fadeOut(0.2f), visible(false)));
										selectedLevel = Integer.parseInt(tmp.getName());
										if (selectedLevel <= curUserSCORE.getMaxPlayedLevel(selectedDclty)) {
											swingOutThumbnail();
										} else {
											if(!unlockMsg.isVisible()){
												dcltyNumGroup[selectedDclty.ordinal()].addAction(alpha(0.4f));
												unlockMsg.addAction(sequence(visible(true),fadeIn(0.5f)));
											}
										}
									}
								});
							}
							l++;
						}
					}

					if (idx == selectedDclty.ordinal() && i == curPage)
						dcltyNumPageGroup[idx][i].setVisible(true);
					else
						dcltyNumPageGroup[idx][i].setVisible(false);

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
		stage.addActor(argbFull);

		midButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				midButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				midButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				if(backStr.isVisible()){
					argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
						@Override
						public void run() {
							game.setScreen(new MenuScreen(game));
						}
					})));
				} else {
					argbFull.addAction(sequence(visible(true), new Action() {
						@Override
						public boolean act(float delta) {
							thumbnail.addAction(fadeOut(0.1f));
							return true;
						}
					},fadeIn(1f), run(new Runnable() {
						@Override
						public void run() {
							printLevelData = false;
							thumbnail.clearActions();
							thumbnail.remove();
							game.setScreen(new PlayScreen(game, selectedDclty, selectedLevel));
						}
					})));
				}
			}
		});

		leftButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				leftButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				leftButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				swipeRight();
			}
		});

		rightButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				rightButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				rightButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				swipeLeft();
			}
		});

		if (MemStore.curUserOPTS.isSwipeMove()) {
			stage.addListener(new DragListener() {
				@Override
				public void dragStart(InputEvent event, float x, float y, int pointer) {
					dragStartX = x;
					super.dragStart(event, x, y, pointer);
				}

				@Override
				public void dragStop(InputEvent event, float x, float y, int pointer) {
					super.dragStop(event, x, y, pointer);
					dragEndX = x;
					if (dragStartX > dragEndX) {
						swipeLeft();
					} else if (dragStartX < dragEndX) {
						swipeRight();
					}
				}
			});
		}

		stage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(unlockMsg.isVisible()){
					unlockMsg.addAction(sequence(fadeOut(0.5f),visible(false)));
					dcltyNumGroup[selectedDclty.ordinal()].addAction(alpha(1f));
				}
				if (printLevelData) {
					if (!(event.getTarget().getName() != null && (event.getTarget().getName().equalsIgnoreCase("thumbnail") || event.getTarget().getName().equalsIgnoreCase("midbutton"))))
						swingInThumbnail();
				}
				return true;
			}

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					if (!printLevelData) {
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
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (printLevelData) {
			final LevelScore gScore = MemStore.curUserSCORE.getScores(selectedDclty, selectedLevel);
			final Level lvl = new Level();
			lvl.loadLevel(selectedDclty, selectedLevel);
			batch.begin();
			final String lvlDetails = "Level No      : " + (selectedLevel) + "\nLevel Name : " + lvl.getLvlName() + "\nDifficulty    : " + lvl.getLvlDclty().name() + "\n";
			String scoreDetails = "";
			if (gScore != null) {
				scoreDetails = "Moves Played : " + gScore.getMoves() + "\nShots Fired    : " + gScore.getShots() + "\nHint Used       : " + (gScore.isHintUsed() ? "Yes" : "No");
			} else {
				scoreDetails = "Moves Played : 0\nShots Fired    : 0 \nHint Used       : No";
			}
			g.drawMultiLineString(lvlDetails, 200, 433, Color.BLACK);
			g.drawMultiLineString(scoreDetails, 600, 433, Color.BLACK);
			batch.end();
		}
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
		int selectedDcltyIdx = selectedDclty.ordinal();
		if (!dragInProgress && rightButtonGroup.isVisible()) {
			if (curPage < dcltyGrpCnt[selectedDcltyIdx] - 1) {
				dcltyNumPageGroup[selectedDcltyIdx][curPage].addAction(sequence());
				dcltyNumPageGroup[selectedDcltyIdx][curPage].addAction(sequence(run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = true;
					}
				}), moveTo(-game.getSrcWidth(), dcltyNumPageGroup[selectedDcltyIdx][curPage].getY(), 1f), visible(false)));

				dcltyNumPageGroup[selectedDclty.ordinal()][curPage + 1].addAction(sequence(alpha(0), moveTo(0, dcltyNumPageGroup[selectedDcltyIdx][curPage + 1].getY()), visible(true), fadeIn(1f), run(new Runnable() {

					@Override
					public void run() {
						dragInProgress = false;
					}
				})));
				curPage++;
			}
		}
		if (curPage >= dcltyGrpCnt[selectedDcltyIdx] - 1) {
			rightButtonGroup.setVisible(false);
		}
		if (curPage > 0) {
			leftButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
		}
	}

	public void swipeRight() {
		int selectedDcltyIdx = selectedDclty.ordinal();
		if (!dragInProgress && leftButtonGroup.isVisible()) {
			if (curPage > 0) {
				dcltyNumPageGroup[selectedDcltyIdx][curPage].addAction(sequence());
				dcltyNumPageGroup[selectedDcltyIdx][curPage].addAction(sequence(run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = true;
					}
				}), moveTo(game.getSrcWidth(), dcltyNumPageGroup[selectedDcltyIdx][curPage].getY(), 1f), visible(false)));

				dcltyNumPageGroup[selectedDcltyIdx][curPage - 1].addAction(sequence(alpha(0), moveTo(0, dcltyNumPageGroup[selectedDcltyIdx][curPage - 1].getY()), visible(true), fadeIn(1f), run(new Runnable() {

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
		if (curPage < dcltyGrpCnt[selectedDcltyIdx] - 1) {
			rightButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
		}
	}

	public void swingOutThumbnail() {
		thumbnail = new Image(g.getThumbTexRegion(selectedDclty, selectedLevel));
		thumbnail.setPosition(clickedNumButton.getX(), clickedNumButton.getY());
		thumbnail.setOrigin(thumbnail.getWidth() / 2, thumbnail.getHeight() / 2);
		thumbnail.setName("thumbnail");
		float tempWidth = thumbnail.getWidth();
		float tempHeight = thumbnail.getHeight();
		thumbnail.setSize(clickedNumButton.getWidth(), clickedNumButton.getHeight());
		stage.addActor(thumbnail);
		thumbnail.clearActions();
		thumbnail.addAction(parallel(forever(rotateBy(1f)), sizeTo(tempWidth, tempHeight, 1f), moveTo((topBar.getWidth() - tempWidth) / 2, (480 - tempHeight) / 2, 1f, swingOut)));
		dcltyNumGroup[selectedDclty.ordinal()].addAction(alpha(0.1f));
		leftButtonGroup.addAction(alpha(0f));
		rightButtonGroup.addAction(alpha(0f));

		dcltyNumGroup[selectedDclty.ordinal()].setTouchable(Touchable.disabled);
		dcltyButtonGroup.setTouchable(Touchable.disabled);
		leftButtonGroup.setTouchable(Touchable.disabled);
		rightButtonGroup.setTouchable(Touchable.disabled);

		if (selectedLevel < curUserSCORE.getMaxPlayedLevel(selectedDclty)) {
			backStr.addAction(visible(false));
			playStr.addAction(visible(false));
			playAgainStr.addAction(visible(true));
		} else {
			backStr.addAction(visible(false));
			playStr.addAction(visible(true));
			playAgainStr.addAction(visible(false));
		}
		printLevelData = true;
	}

	public void swingInThumbnail() {
		thumbnail.setOrigin(clickedNumButton.getWidth() / 2, clickedNumButton.getWidth() / 2);
		thumbnail.clearActions();
		thumbnail.addAction(sequence(parallel(repeat(40, rotateBy(-1f)), sizeTo(clickedNumButton.getWidth(), clickedNumButton.getHeight(), 1f), moveTo(clickedNumButton.getX(), clickedNumButton.getY(), 1f, swingIn)), fadeOut(0.2f), visible(false),
				run(new Runnable() {
					@Override
					public void run() {
						thumbnail.clearActions();
						thumbnail.remove();
					}
				})));
		dcltyNumGroup[selectedDclty.ordinal()].addAction(alpha(1f));
		leftButtonGroup.addAction(alpha(1f));
		rightButtonGroup.addAction(alpha(1f));

		dcltyNumGroup[selectedDclty.ordinal()].setTouchable(Touchable.enabled);
		dcltyButtonGroup.setTouchable(Touchable.enabled);
		leftButtonGroup.setTouchable(Touchable.enabled);
		rightButtonGroup.setTouchable(Touchable.enabled);

		playStr.addAction(visible(false));
		playAgainStr.addAction(visible(false));
		backStr.addAction(visible(true));
		printLevelData = false;
	}
}
