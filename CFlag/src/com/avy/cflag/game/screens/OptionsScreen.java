package com.avy.cflag.game.screens;

import static com.avy.cflag.game.MemStore.curUserOPTS;
import static com.avy.cflag.game.MemStore.userLIST;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.base.Musics;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.Utils;
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
	private TextureAtlas optionsAtlas;
	private Image titleStr;

	private Image enterNameStr;
	private TextField nameField;
	private Image okButtonUp, okButtonDown;
	private Group okButtonGroup;
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
	private Image[] trackStr;
	private Group trackGroup;

	private Image swipeToPlayStr;
	private CheckBox swipeEnabled;

	private Image saveButtonUp;
	private Image saveButtonDown;
	private Image saveStr;
	private Group saveButtonGroup;

	private Image discardButtonUp;
	private Image discardButtonDown;
	private Image discardStr;
	private Group discardButtonGroup;
	private Group optionsGroup;

	public OptionsScreen(CFlagGame game) {
		super(game, true, false, false);

		optionsAtlas = g.createImageAtlas("options");
		trackStr = new Image[Musics.values().length];
	}

	@Override
	public void show() {
		super.show();

		g.setImageAtlas(commonAtlas);

		int spaceFromMid = 20;
		saveButtonUp = new Image(g.getFlipTexRegion("rectbuttonup"));
		discardButtonUp = new Image(g.getFlipTexRegion("rectbuttonup"));
		saveButtonDown = new Image(g.getFlipTexRegion("rectbuttondown"));
		discardButtonDown = new Image(g.getFlipTexRegion("rectbuttondown"));

		saveButtonUp.setPosition(bottomBar.getWidth() / 2 - saveButtonUp.getWidth() - spaceFromMid, bottomBar.getY() + (bottomBar.getHeight() - saveButtonUp.getHeight()) / 2);
		saveButtonDown.setPosition(saveButtonUp.getX(), saveButtonUp.getY());
		discardButtonUp.setPosition(bottomBar.getWidth() / 2 + spaceFromMid, bottomBar.getY() + (bottomBar.getHeight() - discardButtonUp.getHeight()) / 2);
		discardButtonDown.setPosition(discardButtonUp.getX(), discardButtonUp.getY());

		saveButtonDown.setVisible(false);
		discardButtonDown.setVisible(false);

		g.setImageAtlas(optionsAtlas);

		titleStr = new Image(g.getFlipTexRegion("title"));
		saveStr = new Image(g.getFlipTexRegion("save"));
		discardStr = new Image(g.getFlipTexRegion("discard"));

		titleStr.setPosition((topBar.getWidth() - titleStr.getWidth()) / 2, (topBar.getHeight() - titleStr.getHeight()) / 2);
		saveStr.setPosition(saveButtonUp.getX(), saveButtonUp.getY());
		discardStr.setPosition(discardButtonUp.getX(), discardButtonUp.getY());

		enterNameStr = new Image(g.getFlipTexRegion("entername"));
		nameField = new TextField("", g.getTextBoxStyle("salsa", 23));
		okButtonUp = new Image(g.getFlipTexRegion("okbuttonup"));
		okButtonDown = new Image(g.getFlipTexRegion("okbuttondown"));
		newnameResult = new Label("", g.getLabelStyle("salsa", 12));

		enterNameStr.setPosition((game.getSrcWidth() - (enterNameStr.getWidth() + nameField.getWidth() + okButtonUp.getWidth() + 20)) / 2, (game.getSrcHeight() - enterNameStr.getHeight()) / 2);
		nameField.setMaxLength(10);
		nameField.setPosition(enterNameStr.getX() + enterNameStr.getWidth(), (game.getSrcHeight() - nameField.getHeight()) / 2);
		okButtonUp.setPosition(enterNameStr.getX() + enterNameStr.getWidth() + nameField.getWidth() + 20, (game.getSrcHeight() - okButtonUp.getHeight()) / 2);
		okButtonDown.setPosition(okButtonUp.getX(), okButtonUp.getY());
		okButtonDown.setVisible(false);
		newnameResult.setWidth(200);
		newnameResult.setPosition((game.getSrcWidth() - newnameResult.getWidth()) / 2, enterNameStr.getY() + 60);
		newnameResult.setAlignment(Align.center);

		okButtonGroup = new Group();
		okButtonGroup.addActor(okButtonUp);
		okButtonGroup.addActor(okButtonDown);

		enterNameGroup = new Group();
		enterNameGroup.addActor(enterNameStr);
		enterNameGroup.addActor(nameField);
		enterNameGroup.addActor(okButtonGroup);
		enterNameGroup.addActor(newnameResult);
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
		swipeToPlayStr = new Image(g.getFlipTexRegion("swipetoplay"));

		int trackNo = 0;
		for (Musics music : Musics.values()) {
			trackStr[trackNo] = new Image(g.getFlipTexRegion(music.name()));
			trackStr[trackNo].setName(music.name());
			trackStr[trackNo].setVisible(curUserOPTS.getMusicTrack().equals(music));
			trackStr[trackNo].addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
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

		swipeEnabled = new CheckBox("", g.getCheckBoxStyle("salsa", 18));
		swipeEnabled.setChecked(curUserOPTS.isSwipeMove());

		int x = 80, y = 75, yw = 60;
		profileStr.setPosition(x, y = y + yw);
		profileLeft.setPosition(profileStr.getX() + profileStr.getWidth(), y);
		profileName.setBounds(profileLeft.getX() + profileLeft.getWidth(), y + (profileLeft.getHeight() - 10) / 2, 90, 10);
		profileRight.setPosition(profileName.getX() + profileName.getWidth(), y);

		newButtonUp.setPosition(profileRight.getX() + profileRight.getWidth() + 20, (y + profileStr.getHeight() / 2) - (newButtonUp.getHeight() / 2));
		newButtonDown.setPosition(newButtonUp.getX(), newButtonUp.getY());

		delButtonUp.setPosition(newButtonUp.getX() + newButtonUp.getWidth() + 10, newButtonUp.getY());
		delButtonDown.setPosition(delButtonUp.getX(), delButtonUp.getY());

		soundStr.setPosition(x, y = y + yw);
		sound.setPosition(x + 160, y);
		soundVolumeStr.setPosition(x + 340, y);
		soundVolume.setPosition(x + 495, y);

		musicStr.setPosition(x, y = y + yw);
		music.setPosition(x + 160, y);
		musicVolumeStr.setPosition(x + 340, y);
		musicVolume.setPosition(x + 495, y);

		musicTrackStr.setPosition(x, y = y + yw);
		trackLeft.setPosition(x + 157, y);
		for (Image trkStr : trackStr) {
			trkStr.setPosition(x + 180, y + 2);
		}
		trackRight.setPosition(x + 250, y);
		swipeToPlayStr.setPosition(x + 340, y);
		swipeEnabled.setPosition(x + 500, y);

		newButtonGroup = new Group();
		newButtonGroup.addActor(newButtonUp);
		newButtonGroup.addActor(newButtonDown);

		delButtonGroup = new Group();
		delButtonGroup.addActor(delButtonUp);
		delButtonGroup.addActor(delButtonDown);
		delButtonGroup.setVisible(userLIST.getUserCount() > 0 ? true : false);

		trackGroup = new Group();
		trackGroup.addActor(trackLeft);
		for (Image trkStr : trackStr) {
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
		optionsGroup.addActor(swipeToPlayStr);
		optionsGroup.addActor(sound);
		optionsGroup.addActor(soundVolume);
		optionsGroup.addActor(music);
		optionsGroup.addActor(musicVolume);
		optionsGroup.addActor(trackGroup);
		optionsGroup.addActor(swipeEnabled);
		optionsGroup.addActor(saveButtonGroup);
		optionsGroup.addActor(discardButtonGroup);
		optionsGroup.setVisible(true);

		stage.addActor(titleStr);
		stage.addActor(optionsGroup);
		stage.addActor(enterNameGroup);
		stage.addActor(argbFull);

		sound.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				curUserOPTS.setSoundOn(((CheckBox) actor).isChecked());
				Sounds.setState(curUserOPTS.isSoundOn());
			}
		});
		soundVolume.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				curUserOPTS.setSoundVolume(((Slider) actor).getValue());
				Sounds.setVolume(curUserOPTS.getSoundVolume());
			}
		});
		music.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				curUserOPTS.setMusicOn(((CheckBox) actor).isChecked());
				Musics.setState(curUserOPTS.isMusicOn());
			}
		});
		musicVolume.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				curUserOPTS.setMusicVolume(((Slider) actor).getValue());
				Musics.setVolume(curUserOPTS.getMusicVolume());
			}
		});
		swipeEnabled.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				curUserOPTS.setSwipeMove(((CheckBox) actor).isChecked());
			}
		});

		saveButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				saveButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				saveButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				userLIST.updateUser(curUserOPTS);
				Utils.saveGameOptions();
				argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new MenuScreen(game));
					}
				})));
			}
		});

		discardButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				discardButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Utils.loadGameOptions();
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
			public boolean acceptChar(TextField textField, char c) {
				if (Character.isAlphabetic(c) || Character.isDigit(c))
					return true;
				else
					return false;
			}
		});

		nameField.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				newnameResult.setText("");
			}
		});

		okButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				okButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				okButtonDown.addAction(sequence(fadeOut(0.1f), visible(false)));
				String userName = nameField.getText();
				if (userName.length() == 0) {
					newnameResult.setText("Please enter a name");
				} else {
					if (userLIST.isUserExists(userName))
						newnameResult.setText("User already exists");
					else {
						curUserOPTS = userLIST.addUser(userName);
						applyUserOptions();
						optionsGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
						enterNameGroup.addAction(sequence(fadeOut(0.1f), visible(false)));
					}
				}
			}
		});

		newButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				newButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				newButtonDown.addAction(sequence(fadeOut(0.1f), visible(false)));
				userLIST.updateUser(curUserOPTS);
				nameField.setText("");
				delButtonGroup.setVisible(true);
				enterNameGroup.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
				optionsGroup.addAction(sequence(fadeOut(0.1f), visible(false)));
			}
		});

		delButtonGroup.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				delButtonDown.addAction(sequence(alpha(0), visible(true), fadeIn(0.1f)));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				delButtonDown.addAction(sequence(fadeOut(0.1f), visible(false)));
				int idx = userLIST.getUserIndex(curUserOPTS.getUserName());
				userLIST.deleteUser(idx);
				if (userLIST.getUserCount() > 0) {
					curUserOPTS.setGameOpts(userLIST.getUserOptionsByIdx((idx + 1 + userLIST.getUserCount()) % userLIST.getUserCount()));
				} else if (userLIST.getUserCount() == 0) {
					curUserOPTS = new UserOptions();
					delButtonGroup.setVisible(false);
				}
				applyUserOptions();
			}
		});

		profileLeft.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				userLIST.updateUser(curUserOPTS);
				int idx = userLIST.getUserIndex(curUserOPTS.getUserName());
				if (userLIST.getUserCount() > 1) {
					curUserOPTS.setGameOpts(userLIST.getUserOptionsByIdx((idx - 1 + userLIST.getUserCount()) % userLIST.getUserCount()));
					applyUserOptions();
				}
				return true;
			}
		});

		profileRight.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				userLIST.updateUser(curUserOPTS);
				int idx = userLIST.getUserIndex(curUserOPTS.getUserName());
				if (userLIST.getUserCount() > 1) {
					curUserOPTS.setGameOpts(userLIST.getUserOptionsByIdx((idx + 1 + userLIST.getUserCount()) % userLIST.getUserCount()));
					applyUserOptions();
				}
				return true;
			}
		});

		profileName.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				userLIST.updateUser(curUserOPTS);
				int idx = userLIST.getUserIndex(curUserOPTS.getUserName());
				if (userLIST.getUserCount() > 1) {
					curUserOPTS.setGameOpts(userLIST.getUserOptionsByIdx((idx + 1 + userLIST.getUserCount()) % userLIST.getUserCount()));
					applyUserOptions();
				}
				return true;
			}
		});

		trackLeft.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				for (int i = 0; i < trackStr.length; i++) {
					if (trackStr[i].isVisible()) {
						trackStr[i].setVisible(false);
						trackStr[(i - 1 + trackStr.length) % trackStr.length].setVisible(true);
						Musics.valueOf(trackStr[(i - 1 + trackStr.length) % trackStr.length].getName()).loadAndPlay();
						curUserOPTS.setMusicTrack(Musics.valueOf(trackStr[(i - 1 + trackStr.length) % trackStr.length].getName()));
						break;
					}
				}
				return true;
			}
		});

		trackRight.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				for (int i = 0; i < trackStr.length; i++) {
					if (trackStr[i].isVisible()) {
						trackStr[i].setVisible(false);
						trackStr[(i + 1) % trackStr.length].setVisible(true);
						Musics.valueOf(trackStr[(i + 1) % trackStr.length].getName()).loadAndPlay();
						curUserOPTS.setMusicTrack(Musics.valueOf(trackStr[(i + 1) % trackStr.length].getName()));
						break;
					}
				}
				return true;
			}
		});

		stage.addListener(new InputListener() {
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
		if (optionsAtlas != null)
			optionsAtlas.dispose();
	}

	public void applyUserOptions() {

		profileName.setText(curUserOPTS.getUserName());

		music.setChecked(curUserOPTS.isMusicOn());
		Musics.setState(curUserOPTS.isMusicOn());

		for (int i = 0; i < trackStr.length; i++) {
			if (curUserOPTS.getMusicTrack().name() == trackStr[i].getName()) {
				trackStr[i].setVisible(true);
				Musics.valueOf(trackStr[i].getName()).loadAndPlay();
			} else {
				trackStr[i].setVisible(false);
			}
		}

		musicVolume.setValue(curUserOPTS.getMusicVolume());
		Musics.setVolume(curUserOPTS.getMusicVolume());

		sound.setChecked(curUserOPTS.isSoundOn());
		Sounds.setState(curUserOPTS.isSoundOn());

		soundVolume.setValue(curUserOPTS.getSoundVolume());
		Sounds.setVolume(curUserOPTS.getSoundVolume());
		swipeEnabled.setChecked(curUserOPTS.isSwipeMove());
	}

}
