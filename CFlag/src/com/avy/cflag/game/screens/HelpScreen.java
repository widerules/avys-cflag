package com.avy.cflag.game.screens;

import static com.avy.cflag.game.MemStore.curUserOPTS;
import static com.avy.cflag.game.MemStore.curUserSCORE;
import static com.avy.cflag.game.MemStore.userLIST;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.base.TouchListener;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.EnumStore.Difficulty;
import com.avy.cflag.game.GameUtils;
import com.avy.cflag.game.MemStore;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class HelpScreen extends BackScreen {

	private final TextureAtlas helpAtlas;

	private Image title1Str;
	private Image enterNameStr;
	private TextField nameField;
	private Image okButtonUp, okButtonDown;
	private Group okButtonGroup;
	private Label newnameResult;
	private Group enterNameGroup;

	private Image title2Str;
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

	private final String context;

	public HelpScreen(final CFlagGame game, final String context) {
		super(game, true, false, false);

		helpAtlas = g.createImageAtlas("help");

		if (MemStore.curUserOPTS.isFirstRun()) {
			totalHelpPages = 5;
		} else {
			totalHelpPages = 4;
		}

		helpPages = new Image[totalHelpPages];
		curHelpPage = 0;

		dragStartX = 0;
		dragEndX = 0;
		dragInProgress = false;
		this.context = context;
	}

	@Override
	public void show() {
		super.show();

		g.setImageAtlas(commonAtlas);

		midButtonUp = new Image(g.getFlipTexRegion("rectbuttonup"));
		midButtonUp.setPosition((bottomBar.getWidth() - midButtonUp.getWidth()) / 2, bottomBar.getY() + ((bottomBar.getHeight() - midButtonUp.getHeight()) / 2));
		midButtonDown = new Image(g.getFlipTexRegion("rectbuttondown"));
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

		g.setImageAtlas(helpAtlas);
		title2Str = new Image(g.getFlipTexRegion("title"));
		title2Str.setPosition((topBar.getWidth() - title2Str.getWidth()) / 2, (topBar.getHeight() - title2Str.getHeight()) / 2);
		backStr = new Image(g.getFlipTexRegion("back"));
		backStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		backStr.setVisible(false);
		skipStr = new Image(g.getFlipTexRegion("skip"));
		skipStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		skipStr.setVisible(false);
		playStr = new Image(g.getFlipTexRegion("play"));
		playStr.setPosition(midButtonUp.getX(), midButtonUp.getY());
		playStr.setVisible(false);

		if (context.equalsIgnoreCase("newgame") || context.equalsIgnoreCase("levelselect")) {
			skipStr.setVisible(true);
		} else {
			backStr.setVisible(true);
		}

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
			if (MemStore.curUserOPTS.isFirstRun()) {
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

		helpPageGroup.addActor(title2Str);
		helpPageGroup.addActor(midButtonGroup);
		helpPageGroup.addActor(leftButtonGroup);
		helpPageGroup.addActor(rightButtonGroup);

		title1Str = new Image(g.getFlipTexRegion("ctf"));
		enterNameStr = new Image(g.getFlipTexRegion("entername"));
		nameField = new TextField("", g.getTextBoxStyle("salsa", 23));
		okButtonUp = new Image(g.getFlipTexRegion("okbuttonup"));
		okButtonDown = new Image(g.getFlipTexRegion("okbuttondown"));
		newnameResult = new Label("", g.getLabelStyle("salsa", 12));

		title1Str.setPosition((topBar.getWidth() - title1Str.getWidth()) / 2, (topBar.getHeight() - title1Str.getHeight()) / 2);
		enterNameStr.setPosition((game.getSrcWidth() - (enterNameStr.getWidth() + nameField.getWidth() + okButtonUp.getWidth() + 20)) / 2, ((game.getSrcHeight() - enterNameStr.getHeight()) / 2) - 80);
		nameField.setMaxLength(10);
		nameField.setPosition(enterNameStr.getX() + enterNameStr.getWidth(), ((game.getSrcHeight() - nameField.getHeight()) / 2) - 80);
		okButtonUp.setPosition(enterNameStr.getX() + enterNameStr.getWidth() + nameField.getWidth() + 20, ((game.getSrcHeight() - okButtonUp.getHeight()) / 2) - 80);
		okButtonDown.setPosition(okButtonUp.getX(), okButtonUp.getY());
		okButtonDown.setVisible(false);
		newnameResult.setWidth(200);
		newnameResult.setPosition((game.getSrcWidth() - newnameResult.getWidth()) / 2, enterNameStr.getY() + 60);
		newnameResult.setAlignment(Align.center);

		okButtonGroup = new Group();
		okButtonGroup.addActor(okButtonUp);
		okButtonGroup.addActor(okButtonDown);

		enterNameGroup = new Group();
		enterNameGroup.addActor(title1Str);
		enterNameGroup.addActor(enterNameStr);
		enterNameGroup.addActor(nameField);
		enterNameGroup.addActor(okButtonGroup);
		enterNameGroup.addActor(newnameResult);

		if (userLIST.getCurrentUser() < 0) {
			enterNameGroup.setVisible(true);
			helpPageGroup.setVisible(false);
		} else {
			enterNameGroup.setVisible(false);
			helpPageGroup.setVisible(true);
		}

		stage.addActor(helpPageGroup);
		stage.addActor(enterNameGroup);
		stage.addActor(argbFull);

		leftButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				leftButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				leftButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				swipeRight();
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
				swipeLeft();
			}
		});

		midButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				midButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				midButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						if (context.equalsIgnoreCase("newgame")) {
							if (curUserOPTS.isFirstRun()) {
								game.setScreen(new PlayScreen(game, Difficulty.Novice, 1));
							} else {
								game.setScreen(new PlayScreen(game, curUserOPTS.getLastDifficulty(), curUserSCORE.getMaxPlayedLevel(curUserOPTS.getLastDifficulty())));
							}
						} else if (context.equalsIgnoreCase("levelselect")) {
							game.setScreen(new LevelScreen(game, false));
						} else {
							game.setScreen(new MenuScreen(game));
						}
					}
				})));
			}
		});

		okButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				okButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				okButtonDown.addAction(sequence(fadeOut(0.1f), visible(false)));
				final String userName = nameField.getText();
				if (userName.length() == 0) {
					newnameResult.setText("Please enter a Name");
				} else {
					if (userLIST.isUserExists(userName)) {
						newnameResult.setText("User already exists");
					} else {
						curUserOPTS = userLIST.addUser(userName);
						GameUtils.saveGameOptions();
						helpPageGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
						enterNameGroup.addAction(sequence(fadeOut(0.1f), visible(false)));
					}
				}
			}
		});

		stage.addListener(new DragListener() {
			@Override
			public void dragStart(final InputEvent event, final float x, final float y, final int pointer) {
				if (MemStore.curUserOPTS.isSwipeMove()) {
					dragStartX = x;
					super.dragStart(event, x, y, pointer);
				}
			}

			@Override
			public void dragStop(final InputEvent event, final float x, final float y, final int pointer) {
				if (MemStore.curUserOPTS.isSwipeMove()) {
					super.dragStop(event, x, y, pointer);
					dragEndX = x;
					if (dragStartX > dragEndX) {
						swipeLeft();
					} else if (dragStartX < dragEndX) {
						swipeRight();
					}
				}
			}

			@Override
			public boolean keyDown(final InputEvent event, final int keycode) {
				if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
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

	@Override
	public void dispose() {
		if (helpAtlas != null) {
			helpAtlas.dispose();
		}
		super.dispose();
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

				helpPages[curHelpPage + 1].addAction(sequence(alpha(0), moveTo((game.getSrcWidth() - helpPages[curHelpPage + 1].getWidth()) / 2, helpPages[curHelpPage + 1].getY()), visible(true),
						fadeIn(1f), run(new Runnable() {
							@Override
							public void run() {
								dragInProgress = false;
							}
						})));
				curHelpPage++;
			}
			if (curHelpPage >= (totalHelpPages - 1)) {
				rightButtonGroup.setVisible(false);
				if (context.equalsIgnoreCase("newgame")) {
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

				helpPages[curHelpPage - 1].addAction(sequence(alpha(0), moveTo((game.getSrcWidth() - helpPages[curHelpPage - 1].getWidth()) / 2, helpPages[curHelpPage - 1].getY()), visible(true),
						fadeIn(1f), run(new Runnable() {
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
				if (context.equalsIgnoreCase("newgame")) {
					skipStr.setVisible(true);
					playStr.setVisible(false);
				}
			}
		}
	}
}
