package com.avy.cflag.game.screens;

import static com.avy.cflag.game.MemStore.curUserOPTS;
import static com.avy.cflag.game.MemStore.userLIST;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import java.util.ArrayList;

import com.avy.cflag.base.Musics;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.base.TouchListener;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.GameUtils;
import com.avy.cflag.game.utils.UserOptions;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class OptionsScreen extends BackScreen {
	private final TextureAtlas optionsAtlas;
	private Image titleStr;

	private Image enterNameStr;
	private TextField nameField;
	private Label newnameResult;
	private Group enterNameGroup;

	private Image profileStr;
	private Image profileLeft, profileRight;
	private Label profileName;
	private Image newButtonUp, newButtonDown;
	private Image delButtonUp, delButtonDown;
	private Group newButtonGroup, delButtonGroup;

	private Image soundStr;
	private CheckBox sound;

	private Image soundVolumeStr;
	private Slider soundVolume;

	private Image musicStr;
	private CheckBox music;

	private Image musicVolumeStr;
	private Slider musicVolume;

	private Image musicTrackStr;
	private Image trackLeft, trackRight;
	private final Image[] trackStr;
	private Group trackGroup;

	private Image swipeSensitivityStr;
	private Slider swipeSensitivity;

	private Image saveButtonUp;
	private Image saveButtonDown;
	private Image saveStr;
	private Group saveButtonGroup;

	private Image discardButtonUp;
	private Image discardButtonDown;
	private Image discardStr;
	private Group discardButtonGroup;

	private Image okButtonUp;
	private Image okButtonDown;
	private Image okStr;
	private Group okButtonGroup;
	
	private Group optionsGroup;
	ArrayList<String> deletedUsers;

	public OptionsScreen(final CFlagGame game) {
		super(game, true, false, false);

		optionsAtlas = g.createImageAtlas("options");
		trackStr = new Image[Musics.values().length];
		deletedUsers = new ArrayList<String>();
	}

	@Override
	public void show() {
		super.show();

		g.setImageAtlas(commonAtlas);
		
		okButtonUp = new Image(g.getFlipTexRegion("0_rectbuttonup"));
		okButtonUp.setPosition((bottomBar.getWidth() - okButtonUp.getWidth()) / 2, bottomBar.getY() + ((bottomBar.getHeight() - okButtonUp.getHeight()) / 2));
		okButtonDown = new Image(g.getFlipTexRegion("0_rectbuttondown"));
		okButtonDown.setPosition(okButtonUp.getX(), okButtonUp.getY());
		okButtonDown.setVisible(false);

		final int spaceFromMid = 20;
		saveButtonUp = new Image(g.getFlipTexRegion("0_rectbuttonup"));
		discardButtonUp = new Image(g.getFlipTexRegion("1_rectbuttonup"));
		saveButtonDown = new Image(g.getFlipTexRegion("0_rectbuttondown"));
		discardButtonDown = new Image(g.getFlipTexRegion("1_rectbuttondown"));

		saveButtonUp.setPosition((bottomBar.getWidth() / 2) - saveButtonUp.getWidth() - spaceFromMid, bottomBar.getY() + ((bottomBar.getHeight() - saveButtonUp.getHeight()) / 2));
		saveButtonDown.setPosition(saveButtonUp.getX(), saveButtonUp.getY());
		discardButtonUp.setPosition((bottomBar.getWidth() / 2) + spaceFromMid, bottomBar.getY() + ((bottomBar.getHeight() - discardButtonUp.getHeight()) / 2));
		discardButtonDown.setPosition(discardButtonUp.getX(), discardButtonUp.getY());

		saveButtonDown.setVisible(false);
		discardButtonDown.setVisible(false);

		g.setImageAtlas(optionsAtlas);

		titleStr = new Image(g.getFlipTexRegion("title"));
		saveStr = new Image(g.getFlipTexRegion("save"));
		discardStr = new Image(g.getFlipTexRegion("discard"));
		okStr = new Image(g.getFlipTexRegion("ok"));

		titleStr.setPosition((topBar.getWidth() - titleStr.getWidth()) / 2, (topBar.getHeight() - titleStr.getHeight()) / 2);
		saveStr.setPosition(saveButtonUp.getX(), saveButtonUp.getY());
		discardStr.setPosition(discardButtonUp.getX(), discardButtonUp.getY());
		okStr.setPosition(okButtonUp.getX(), okButtonUp.getY());

		enterNameStr = new Image(g.getFlipTexRegion("entername"));
		nameField = new TextField("", g.getTextBoxStyle("salsa", 23));
		newnameResult = new Label("", g.getLabelStyle("salsa", 12));

		enterNameStr.setPosition((game.getSrcWidth() - (enterNameStr.getWidth() + nameField.getWidth() + 20)) / 2, ((game.getSrcHeight() - enterNameStr.getHeight()) / 2) - 80);
		nameField.setMaxLength(10);
		nameField.setPosition(enterNameStr.getX() + enterNameStr.getWidth(), ((game.getSrcHeight() - nameField.getHeight()) / 2) - 80);
		newnameResult.setWidth(200);
		newnameResult.setPosition((game.getSrcWidth() - newnameResult.getWidth()) / 2, enterNameStr.getY() + 60);
		newnameResult.setAlignment(Align.center);

		okButtonGroup = new Group();
		okButtonGroup.addActor(okButtonDown);
		okButtonGroup.addActor(okButtonUp);
		okButtonGroup.addActor(okStr);
		
		enterNameGroup = new Group();
		enterNameGroup.addActor(enterNameStr);
		enterNameGroup.addActor(nameField);
		enterNameGroup.addActor(newnameResult);
		enterNameGroup.addActor(okButtonGroup);
		enterNameGroup.setVisible(false);

		profileStr = new Image(g.getFlipTexRegion("userprofile"));
		profileLeft = new Image(g.getFlipTexRegion("strokeleft"));
		profileName = new Label(curUserOPTS.getUserName(), g.getLabelStyle("salsa", 18));
		profileRight = new Image(g.getFlipTexRegion("strokeright"));

		newButtonUp = new Image(g.getFlipTexRegion("newbuttonup"));
		newButtonDown = new Image(g.getFlipTexRegion("newbuttondown"));
		newButtonDown.setVisible(false);

		delButtonUp = new Image(g.getFlipTexRegion("delbuttonup"));
		delButtonDown = new Image(g.getFlipTexRegion("delbuttondown"));
		delButtonDown.setVisible(false);

		soundStr = new Image(g.getFlipTexRegion("sound"));
		soundVolumeStr = new Image(g.getFlipTexRegion("soundvolume"));
		musicStr = new Image(g.getFlipTexRegion("music"));
		musicVolumeStr = new Image(g.getFlipTexRegion("musicvolume"));
		musicTrackStr = new Image(g.getFlipTexRegion("musictrack"));
		trackLeft = new Image(g.getFlipTexRegion("strokeleft"));
		trackRight = new Image(g.getFlipTexRegion("strokeright"));
		swipeSensitivityStr = new Image(g.getFlipTexRegion("swipesensitivity"));

		int trackNo = 0;
		for (final Musics music : Musics.values()) {
			trackStr[trackNo] = new Image(g.getFlipTexRegion(music.name()));
			trackStr[trackNo].setName(music.name());
			trackStr[trackNo].setVisible(curUserOPTS.getMusicTrack().equals(music));
			trackStr[trackNo].addListener(new TouchListener() {
				@Override
				public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
					for (int i = 0; i < trackStr.length; i++) {
						if (trackStr[i].isVisible()) {
							trackStr[i].setVisible(false);
							trackStr[(i + 1) % trackStr.length].setVisible(true);
							Musics.valueOf(trackStr[(i + 1) % trackStr.length].getName()).loadAndPlay();
							curUserOPTS.setMusicTrack(Musics.valueOf(trackStr[(i + 1) % trackStr.length].getName()));
							break;
						}
					}
					return super.touchDown(event, x, y, pointer, button);
				}
			});
			trackNo++;
		}

		sound = new CheckBox("", g.getCheckBoxStyle("salsa", 18));
		sound.setChecked(curUserOPTS.isSoundOn());

		soundVolume = new Slider(0f, 1f, 0.1f, false, g.getSliderStyle());
		soundVolume.setValue(curUserOPTS.getSoundVolume());

		music = new CheckBox("", g.getCheckBoxStyle("salsa", 18));
		music.setChecked(curUserOPTS.isMusicOn());

		musicVolume = new Slider(0f, 1f, 0.1f, false, g.getSliderStyle());
		musicVolume.setValue(curUserOPTS.getMusicVolume());

		swipeSensitivity = new Slider(10f, 110f, 10f, false, g.getSliderStyle());
		swipeSensitivity.setValue(curUserOPTS.getSwipeSensitivity());

		final int x = 60;
		int y = 75;
		final int yw = 60;
		profileStr.setPosition(x, y = y + yw);
		profileLeft.setPosition(profileStr.getX() + profileStr.getWidth()-2, y);
		profileName.setBounds(profileLeft.getX() + profileLeft.getWidth(), y + ((profileLeft.getHeight() - 10) / 2), 90, 10);
		profileRight.setPosition(profileName.getX() + profileName.getWidth(), y);

		newButtonUp.setPosition(profileRight.getX() + profileRight.getWidth() + 20, (y + (profileStr.getHeight() / 2)) - (newButtonUp.getHeight() / 2));
		newButtonDown.setPosition(newButtonUp.getX(), newButtonUp.getY());

		delButtonUp.setPosition(newButtonUp.getX() + newButtonUp.getWidth() + 10, newButtonUp.getY());
		delButtonDown.setPosition(delButtonUp.getX(), delButtonUp.getY());

		soundStr.setPosition(profileStr.getX(), y = y + yw);
		sound.setPosition(profileLeft.getX(), y);
		soundVolumeStr.setPosition(newButtonUp.getX(), y);
		soundVolume.setPosition(soundVolumeStr.getX() + soundVolumeStr.getWidth(), y);

		musicStr.setPosition(profileStr.getX(), y = y + yw);
		music.setPosition(profileLeft.getX(), y);
		musicVolumeStr.setPosition(newButtonUp.getX(), y);
		musicVolume.setPosition(musicVolumeStr.getX() + musicVolumeStr.getWidth(), y);

		musicTrackStr.setPosition(profileStr.getX(), y = y + yw);
		trackLeft.setPosition(profileLeft.getX()-2, y);
		for (final Image trkStr : trackStr) {
			trkStr.setPosition(trackLeft.getX()+trackLeft.getWidth()-13, y + 2);
		}
		trackRight.setPosition(trackStr[0].getX() + trackStr[0].getWidth()-14, y);
		swipeSensitivityStr.setPosition(newButtonUp.getX(), y);
		swipeSensitivity.setPosition(musicVolumeStr.getX() + musicVolumeStr.getWidth(), y);

		newButtonGroup = new Group();
		newButtonGroup.addActor(newButtonUp);
		newButtonGroup.addActor(newButtonDown);

		delButtonGroup = new Group();
		delButtonGroup.addActor(delButtonUp);
		delButtonGroup.addActor(delButtonDown);
		delButtonGroup.setVisible(userLIST.getUserCount() > 0 ? true : false);

		trackGroup = new Group();
		trackGroup.addActor(trackLeft);
		for (final Image trkStr : trackStr) {
			trackGroup.addActor(trkStr);
		}
		trackGroup.addActor(trackRight);

		saveButtonGroup = new Group();
		saveButtonGroup.addActor(saveButtonUp);
		saveButtonGroup.addActor(saveButtonDown);
		saveButtonGroup.addActor(saveStr);

		discardButtonGroup = new Group();
		discardButtonGroup.addActor(discardButtonUp);
		discardButtonGroup.addActor(discardButtonDown);
		discardButtonGroup.addActor(discardStr);

		optionsGroup = new Group();
		optionsGroup.addActor(profileStr);
		optionsGroup.addActor(profileLeft);
		optionsGroup.addActor(profileName);
		optionsGroup.addActor(profileRight);
		optionsGroup.addActor(newButtonGroup);
		optionsGroup.addActor(delButtonGroup);
		optionsGroup.addActor(soundStr);
		optionsGroup.addActor(soundVolumeStr);
		optionsGroup.addActor(musicStr);
		optionsGroup.addActor(musicVolumeStr);
		optionsGroup.addActor(musicTrackStr);
		optionsGroup.addActor(swipeSensitivityStr);
		optionsGroup.addActor(sound);
		optionsGroup.addActor(soundVolume);
		optionsGroup.addActor(music);
		optionsGroup.addActor(musicVolume);
		optionsGroup.addActor(trackGroup);
		optionsGroup.addActor(swipeSensitivity);
		optionsGroup.addActor(saveButtonGroup);
		optionsGroup.addActor(discardButtonGroup);
		optionsGroup.setVisible(true);

		stage.addActor(titleStr);
		stage.addActor(optionsGroup);
		stage.addActor(enterNameGroup);
		stage.addActor(argbFull);

		sound.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				curUserOPTS.setSoundOn(((CheckBox) actor).isChecked());
				Sounds.setState(curUserOPTS.isSoundOn());
			}
		});
		soundVolume.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				curUserOPTS.setSoundVolume(((Slider) actor).getValue());
				Sounds.setVolume(curUserOPTS.getSoundVolume());
			}
		});
		music.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				curUserOPTS.setMusicOn(((CheckBox) actor).isChecked());
				Musics.setState(curUserOPTS.isMusicOn());
			}
		});
		musicVolume.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				curUserOPTS.setMusicVolume(((Slider) actor).getValue());
				Musics.setVolume(curUserOPTS.getMusicVolume());
			}
		});
		
		swipeSensitivity.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				curUserOPTS.setSwipeSensitivity(((Slider) actor).getValue());
			}
		});

		saveButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				saveButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				saveButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				userLIST.updateUser(curUserOPTS);
				GameUtils.deleteUserScores(deletedUsers);
				GameUtils.saveGameOptions();
				GameUtils.loadUserScores();
				argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new MenuScreen(game));
					}
				})));
			}
		});

		discardButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				discardButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				GameUtils.loadUserOptions();
				applyUserOptions();
				discardButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new MenuScreen(game));
					}
				})));
			}
		});

		nameField.setTextFieldFilter(new TextFieldFilter() {
			@Override
			public boolean acceptChar(final TextField textField, final char c) {
				if (Character.isAlphabetic(c) || Character.isDigit(c)) {
					return true;
				} else {
					return false;
				}
			}
		});

		nameField.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(final TextField textField, final char c) {
				newnameResult.setText("");
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
					newnameResult.setText("Please enter a name");
				} else {
					if (userLIST.isUserExists(userName)) {
						newnameResult.setText("User already exists");
					} else {
						curUserOPTS = userLIST.addUser(userName);
						applyUserOptions();
						optionsGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
						enterNameGroup.addAction(sequence(fadeOut(0.1f), visible(false)));
					}
				}
			}
		});

		newButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				newButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				newButtonDown.addAction(sequence(fadeOut(0.1f), visible(false)));
				userLIST.updateUser(curUserOPTS);
				nameField.setText("");
				delButtonGroup.setVisible(true);
				enterNameGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
				optionsGroup.addAction(sequence(fadeOut(0.1f), visible(false)));
			}
		});

		delButtonGroup.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				delButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				delButtonDown.addAction(sequence(fadeOut(0.1f), visible(false)));
				final int idx = userLIST.getUserIndex(curUserOPTS.getUserName());
				userLIST.deleteUser(idx);
				deletedUsers.add(curUserOPTS.getUserName());
				if (userLIST.getUserCount() > 0) {
					curUserOPTS.setGameOpts(userLIST.getUserOptionsByIdx((idx + 1 + userLIST.getUserCount()) % userLIST.getUserCount()));
				} else if (userLIST.getUserCount() == 0) {
					curUserOPTS = new UserOptions();
					delButtonGroup.setVisible(false);
				}
				applyUserOptions();
			}
		});

		profileLeft.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				userLIST.updateUser(curUserOPTS);
				final int idx = userLIST.getUserIndex(curUserOPTS.getUserName());
				if (userLIST.getUserCount() > 1) {
					curUserOPTS.setGameOpts(userLIST.getUserOptionsByIdx(((idx - 1) + userLIST.getUserCount()) % userLIST.getUserCount()));
					applyUserOptions();
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		profileRight.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				userLIST.updateUser(curUserOPTS);
				final int idx = userLIST.getUserIndex(curUserOPTS.getUserName());
				if (userLIST.getUserCount() > 1) {
					curUserOPTS.setGameOpts(userLIST.getUserOptionsByIdx((idx + 1 + userLIST.getUserCount()) % userLIST.getUserCount()));
					applyUserOptions();
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		profileName.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				userLIST.updateUser(curUserOPTS);
				final int idx = userLIST.getUserIndex(curUserOPTS.getUserName());
				if (userLIST.getUserCount() > 1) {
					curUserOPTS.setGameOpts(userLIST.getUserOptionsByIdx((idx + 1 + userLIST.getUserCount()) % userLIST.getUserCount()));
					applyUserOptions();
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		trackLeft.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				for (int i = 0; i < trackStr.length; i++) {
					if (trackStr[i].isVisible()) {
						trackStr[i].setVisible(false);
						trackStr[((i - 1) + trackStr.length) % trackStr.length].setVisible(true);
						Musics.valueOf(trackStr[((i - 1) + trackStr.length) % trackStr.length].getName()).loadAndPlay();
						curUserOPTS.setMusicTrack(Musics.valueOf(trackStr[((i - 1) + trackStr.length) % trackStr.length].getName()));
						break;
					}
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		trackRight.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				for (int i = 0; i < trackStr.length; i++) {
					if (trackStr[i].isVisible()) {
						trackStr[i].setVisible(false);
						trackStr[(i + 1) % trackStr.length].setVisible(true);
						Musics.valueOf(trackStr[(i + 1) % trackStr.length].getName()).loadAndPlay();
						curUserOPTS.setMusicTrack(Musics.valueOf(trackStr[(i + 1) % trackStr.length].getName()));
						break;
					}
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		stage.addListener(new InputListener() {
			@Override
			public boolean keyDown(final InputEvent event, final int keycode) {
				if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
					if (enterNameGroup.isVisible()) {
						optionsGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
						enterNameGroup.addAction(sequence(fadeOut(0.1f), visible(false)));
					} else {
						argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
							@Override
							public void run() {
								game.setScreen(new MenuScreen(game));
							}
						})));
					}
					if (enterNameGroup.isVisible()) {
						if (keycode == Keys.ENTER) {
							final String userName = nameField.getText();
							if (userName.length() == 0) {
								newnameResult.setText("Please enter a Name");
							} else if (userLIST.isUserExists(userName)) {
								newnameResult.setText("User already exists");
							}
						} else {
							newnameResult.setText("");
						}
					}
				}
				return true;
			}
		});
	}

	@Override
	public void dispose() {
		if (optionsAtlas != null) {
			optionsAtlas.dispose();
		}
		super.dispose();
	}

	public void applyUserOptions() {
		profileName.setText(curUserOPTS.getUserName());
		music.setChecked(curUserOPTS.isMusicOn());
		Musics.setState(curUserOPTS.isMusicOn());
		for (final Image element : trackStr) {
			if (curUserOPTS.getMusicTrack().name() == element.getName()) {
				element.setVisible(true);
				Musics.valueOf(element.getName()).loadAndPlay();
			} else {
				element.setVisible(false);
			}
		}
		musicVolume.setValue(curUserOPTS.getMusicVolume());
		Musics.setVolume(curUserOPTS.getMusicVolume());
		sound.setChecked(curUserOPTS.isSoundOn());
		Sounds.setState(curUserOPTS.isSoundOn());
		soundVolume.setValue(curUserOPTS.getSoundVolume());
		Sounds.setVolume(curUserOPTS.getSoundVolume());
		swipeSensitivity.setValue(curUserOPTS.getSwipeSensitivity());
	}
}
