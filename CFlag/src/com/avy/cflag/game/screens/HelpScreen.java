package com.avy.cflag.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.MemStore;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class HelpScreen extends BackScreen {

	private final TextureAtlas helpAtlas;

	private Image titleStr;

	private Image skipStr;
	private Image playStr;
	private Image backStr;
	private Image midButtonUp;
	private Image midButtonDown;
	private Group midButtonGroup;

	private Image leftButtonUp;
	private Image leftButtonDown;
	private Group leftButtonGroup;

	private Image rightButtonUp;
	private Image rightButtonDown;
	private Group rightButtonGroup;

	private final Image helpPages[];
	private Group helpPageGroup;

	private final int totalHelpPages;
	private int curHelpPage;

	private float dragStartX;
	private float dragEndX;
	private boolean dragInProgress;

	private String context;
	
	public HelpScreen(CFlagGame game, String context) {
		super(game, true, false, false);

		helpAtlas = g.createImageAtlas("help");

		if (MemStore.gameOPTS.isFirstRun()) {
			totalHelpPages = 5;
		} else {
			totalHelpPages = 4;
		}

		helpPages = new Image[totalHelpPages];
		curHelpPage = 0;

		dragStartX = 0;
		dragEndX = 0;
		dragInProgress = false;
		this.context=context;
	}

	@Override
	public void show() {
		super.show();

		g.setImageAtlas(commonAtlas);

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

		g.setImageAtlas(helpAtlas);
		titleStr = new Image(g.getFlipTexRegion("title"));
		titleStr.setPosition((topBar.getWidth() - titleStr.getWidth()) / 2, (topBar.getHeight() - titleStr.getHeight()) / 2);
		backStr = new Image(g.getFlipTexRegion("back"));
		backStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		backStr.setVisible(false);
		skipStr = new Image(g.getFlipTexRegion("skip"));
		skipStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		skipStr.setVisible(false);
		playStr = new Image(g.getFlipTexRegion("play"));
		playStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		playStr.setVisible(false);

		if (context.equalsIgnoreCase("firstplay"))
			skipStr.setVisible(true);
		 else
			backStr.setVisible(true);
		
		midButtonGroup = new Group();
		midButtonGroup.addActor(midButtonUp);
		midButtonGroup.addActor(midButtonDown);
		midButtonGroup.addActor(backStr);
		midButtonGroup.addActor(skipStr);
		midButtonGroup.addActor(playStr);

		leftButtonGroup = new Group();
		leftButtonGroup.addActor(leftButtonUp);
		leftButtonGroup.addActor(leftButtonDown);
		leftButtonGroup.setVisible(false);

		rightButtonGroup = new Group();
		rightButtonGroup.addActor(rightButtonUp);
		rightButtonGroup.addActor(rightButtonDown);
		rightButtonGroup.setVisible(true);

		helpPageGroup = new Group();
		for (int i = 0; i < totalHelpPages; i++) {
			if (MemStore.gameOPTS.isFirstRun()) {
				helpPages[i] = new Image(g.getFlipTexRegion("page" + i));
			} else {
				helpPages[i] = new Image(g.getFlipTexRegion("page" + (i + 1)));
			}
			helpPages[i].setPosition((game.getSrcWidth() - helpPages[i].getWidth()) / 2, (game.getSrcHeight() - helpPages[i].getHeight()) / 2);
			if (i != 0) {
				helpPages[i].setVisible(false);
			}
			helpPageGroup.addActor(helpPages[i]);
		}

		stage.addActor(titleStr);
		stage.addActor(midButtonGroup);
		stage.addActor(leftButtonGroup);
		stage.addActor(rightButtonGroup);
		stage.addActor(helpPageGroup);
		stage.addActor(argbFull);

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
						if(context.equalsIgnoreCase("firstplay"))
							game.setScreen(new PlayScreen(game));
						else
							game.setScreen(new MenuScreen(game));
					}
				})));
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
		if (helpAtlas != null) {
			helpAtlas.dispose();
		}
	}

	public void swipeLeft() {
		if (!dragInProgress && rightButtonGroup.isVisible()) {
			if (curHelpPage < totalHelpPages) {
				helpPages[curHelpPage].addAction(sequence(run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = true;
					}
				}), moveTo(-game.getSrcWidth(), helpPages[curHelpPage].getY(), 1f), visible(false)));

				helpPages[curHelpPage + 1].addAction(sequence(alpha(0), moveTo((game.getSrcWidth() - helpPages[curHelpPage + 1].getWidth()) / 2, helpPages[curHelpPage + 1].getY()), visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = false;
					}
				})));
				curHelpPage++;
			}
			if (curHelpPage >= totalHelpPages - 1) {
				rightButtonGroup.setVisible(false);
				if(context.equalsIgnoreCase("firstplay")) {
					skipStr.setVisible(false);
					playStr.setVisible(true);
				}
			}
			if (curHelpPage > 0) {
				leftButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
			}
		}
	}

	public void swipeRight() {
		if (!dragInProgress && leftButtonGroup.isVisible()) {
			if (curHelpPage > 0) {
				helpPages[curHelpPage].addAction(sequence(run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = true;
					}
				}), moveTo(game.getSrcWidth(), helpPages[curHelpPage].getY(), 1f), visible(false)));

				helpPages[curHelpPage - 1].addAction(sequence(alpha(0), moveTo((game.getSrcWidth() - helpPages[curHelpPage - 1].getWidth()) / 2, helpPages[curHelpPage - 1].getY()), visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						dragInProgress = false;
					}
				})));
				curHelpPage--;
			}
			if (curHelpPage <= 0) {
				leftButtonGroup.setVisible(false);
			}
			if (curHelpPage < totalHelpPages) {
				rightButtonGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				if(context.equalsIgnoreCase("firstplay")) {
					skipStr.setVisible(true);
					playStr.setVisible(false);
				}
			}
		}
	}

}
