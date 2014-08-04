package com.avy.cflag.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.MemStore;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class LevelScreen extends BackScreen {

	private final int THUMBS_PER_ROW = 5;
	private final int THUMBS_PER_COL = 2;
	private final int thumbsPerPage = THUMBS_PER_ROW * THUMBS_PER_COL;

	private final TextureAtlas levelAtlas;
	private final BitmapFont font;

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
	private Image noLevelStr;

	private final Image thumbs[];
	private final Group thumbsGroup[];

	private final int totalThumbs;
	private int selectedThumb;
	private final int totalPages;
	private int curPage;

	private float dragStartX;
	private float dragEndX;
	private boolean dragInProgress;
	private boolean printText;

	public LevelScreen(CFlagGame game) {
		super(game, true, true, false);

		levelAtlas = g.createImageAtlas("levelselect");
		font = g.createFont("salsa", 13);
		curPage = 1;
		totalThumbs = MemStore.userSCORE.getMaxPlayedLevel();
		selectedThumb = MemStore.userSCORE.getCurrentLevel()-1;
		// totalThumbs = 24;
		// selectedThumb = 24;
		totalPages = (int) Math.floor(totalThumbs / thumbsPerPage) + 1;
		thumbs = new Image[totalThumbs];
		thumbsGroup = new Group[totalPages];
		for (int i = 0; i < thumbsGroup.length; i++) {
			thumbsGroup[i] = new Group();
		}
		curPage = totalPages - 1;

		printText = true;
		dragStartX = 0;
		dragEndX = 0;
		dragInProgress = false;
	}

	@Override
	public void show() {
		super.show();

		g.setImageAtlas(commonAtlas);
		g.setFont(font);

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

		titleStr = new Image(g.getFlipTexRegion("title"));
		titleStr.setPosition((topBar.getWidth() - titleStr.getWidth()) / 2, (topBar.getHeight() - titleStr.getHeight()) / 2);
		playStr = new Image(g.getFlipTexRegion("play"));
		playStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		playAgainStr = new Image(g.getFlipTexRegion("playagain"));
		playAgainStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		backStr = new Image(g.getFlipTexRegion("back"));
		backStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		backStr.setVisible(false);
		noLevelStr = new Image(g.getFlipTexRegion("nolevels"));
		noLevelStr.setPosition((game.getSrcWidth() - noLevelStr.getWidth()) / 2, (game.getSrcHeight() - noLevelStr.getHeight()) / 2);
		noLevelStr.setVisible(false);

		midButtonGroup = new Group();
		midButtonGroup.addActor(midButtonUp);
		midButtonGroup.addActor(midButtonDown);
		midButtonGroup.addActor(playStr);
		midButtonGroup.addActor(playAgainStr);
		midButtonGroup.addActor(backStr);
		midButtonGroup.setVisible(false);

		leftButtonGroup = new Group();
		leftButtonGroup.addActor(leftButtonUp);
		leftButtonGroup.addActor(leftButtonDown);
		leftButtonGroup.setVisible(false);

		rightButtonGroup = new Group();
		rightButtonGroup.addActor(rightButtonUp);
		rightButtonGroup.addActor(rightButtonDown);
		rightButtonGroup.setVisible(true);

		if (totalThumbs > 0) {
			int row = 0;
			int col = 0;
			int grp = 0;
			final int leftMargin = 10;
			final int topMargin = 10;
			final int rowSpace = 50;
			final int colSpace = 40;

			for (int i = 1; i <= totalThumbs; i++) {

				thumbs[i - 1] = new Image(g.getThumbTexRegion(i));
				thumbs[i - 1].setName("" + (i - 1));
				thumbs[i - 1].setPosition(leftMargin + rowSpace * (col + 1) + col * thumbs[i - 1].getWidth(), topBar.getHeight() + topMargin + colSpace * (row + 1) + row * thumbs[i - 1].getHeight());
				thumbs[i - 1].setOrigin(thumbs[i - 1].getWidth() / 2, thumbs[i - 1].getHeight() / 2);
				if (i == totalThumbs) {
					thumbs[i - 1].addAction(sequence(new Action() {
						@Override
						public boolean act(float delta) {
							playStr.addAction(visible(true));
							playAgainStr.addAction(visible(false));
							midButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(1f)));
							return true;
						}
					}, forever(rotateBy(1f))));
				}

				thumbs[i - 1].addListener(new InputListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						if (selectedThumb >= 0) {
							thumbs[selectedThumb].clearActions();
							thumbs[selectedThumb].addAction(rotateTo(0));
						}
						event.getListenerActor().addAction(forever(rotateBy(1f)));
						printText = true;
						selectedThumb = Integer.parseInt(event.getListenerActor().getName());
						if (selectedThumb == totalThumbs - 1) {
							playStr.addAction(visible(true));
							playAgainStr.addAction(visible(false));
							midButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(1f)));

						} else {
							playStr.addAction(visible(false));
							playAgainStr.addAction(visible(true));
							midButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(1f)));
						}
						return true;
					}
				});

				thumbsGroup[grp].addActor(thumbs[i - 1]);
				col++;
				if (col >= THUMBS_PER_ROW) {
					col = 0;
					row++;
				}

				if (row >= THUMBS_PER_COL) {
					row = 0;
					col = 0;
					grp++;
					leftButtonGroup.setVisible(true);
				}
			}
		} else {
			noLevelStr.setVisible(true);
			backStr.setVisible(true);
			playAgainStr.setVisible(false);
			playStr.setVisible(false);
			rightButtonGroup.setVisible(false);
			midButtonGroup.setVisible(true);
		}

		stage.addActor(titleStr);
		stage.addActor(noLevelStr);
		stage.addActor(midButtonGroup);
		stage.addActor(leftButtonGroup);
		stage.addActor(rightButtonGroup);
		for (int i = 0; i < thumbsGroup.length; i++) {
			if (i != thumbsGroup.length - 1) {
				thumbsGroup[i].setVisible(false);
			}
			stage.addActor(thumbsGroup[i]);
		}
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
				argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						if (totalThumbs > 0) {
							MemStore.userSCORE.setCurrentLevel(selectedThumb);
							game.setScreen(new PlayScreen(game));
						} else {
							game.setScreen(new MenuScreen(game));
						}
					}
				})));
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

		if (MemStore.gameOPTS.isSwipeMove()) {
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

				@Override
				public boolean keyDown(InputEvent event, int keycode) {
					if (keycode == Keys.BACK) {
						argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
							@Override
							public void run() {
								game.setScreen(new MenuScreen(game));
							}
						})));
					}
					return true;
				}
			});
		}
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (selectedThumb >= 0 && printText && totalThumbs > 0) {
			final LevelScore gScore = MemStore.userSCORE.getScores(selectedThumb);
			final Level lvl = new Level();
			lvl.loadLevel(selectedThumb + 1);
			batch.begin();
			final String lvlDetails = "Level No      : " + (selectedThumb+1) + "\nLevel Name : " + lvl.getLvlName() + "\nDifficulty    : " + lvl.getLvlDclty().name() + "\n";
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
		if (levelAtlas != null) {
			levelAtlas.dispose();
		}
		if (font != null) {
			font.dispose();
		}
	}

	public void swipeLeft() {
		if (!dragInProgress && rightButtonGroup.isVisible()) {
			if (curPage < totalPages) {
				thumbsGroup[curPage].addAction(sequence(run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = true;
					}
				}), moveTo(-game.getSrcWidth(), thumbsGroup[curPage].getY(), 1f), visible(false)));

				thumbsGroup[curPage + 1].addAction(sequence(alpha(0), moveTo(0, thumbsGroup[curPage + 1].getY()), visible(true), fadeIn(1f), run(new Runnable() {

					@Override
					public void run() {
						dragInProgress = false;
					}
				})));
				curPage++;
			}
			if (curPage >= totalPages - 1) {
				rightButtonGroup.setVisible(false);
			}
			if (curPage > 0) {
				leftButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
			}
			if (curPage != Math.floor(selectedThumb / (THUMBS_PER_COL * THUMBS_PER_ROW))) {
				printText = false;
				midButtonGroup.addAction(sequence(fadeOut(1f), visible(false)));
			} else {
				printText = true;
				midButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(1f)));
			}
		}
	}

	public void swipeRight() {
		if (!dragInProgress && leftButtonGroup.isVisible()) {
			if (curPage > 0) {
				thumbsGroup[curPage].addAction(sequence(run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = true;
					}
				}), moveTo(game.getSrcWidth(), thumbsGroup[curPage].getY(), 1f), visible(false)));

				thumbsGroup[curPage - 1].addAction(sequence(alpha(0), moveTo(0, thumbsGroup[curPage - 1].getY()), visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = false;
					}
				})));
				curPage--;
			}
			if (curPage <= 0) {
				leftButtonGroup.setVisible(false);
			}
			if (curPage < totalPages) {
				rightButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
			}
			if (curPage != Math.floor(selectedThumb / (THUMBS_PER_COL * THUMBS_PER_ROW))) {
				printText = false;
				midButtonGroup.addAction(sequence(fadeOut(1f), visible(false)));
			} else {
				printText = true;
				midButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(1f)));
			}
		}
	}
}
