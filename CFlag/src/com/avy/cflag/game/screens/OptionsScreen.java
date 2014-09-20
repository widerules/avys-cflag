package com.avy.cflag.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.base.Musics;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.MemStore;
import com.avy.cflag.game.MemStore.Difficulty;
import com.avy.cflag.game.Utils;
import com.avy.cflag.game.utils.GameOpts;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class OptionsScreen extends BackScreen {
	private TextureAtlas optionsAtlas;
	private final BitmapFont font;
	private Image titleStr;

	private Image saveButtonUp;
	private Image saveButtonDown;
	private Image saveStr;
	private Group saveButtonGroup;

	private Image discardButtonUp;
	private Image discardButtonDown;
	private Image discardStr;
	private Group discardButtonGroup;

	private Image enterNameStr;
	private TextField profileName;
	
	private Image profileStr;
	private Image profileLeft, ProfileRight;
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
	
	private GameOpts gOpts;
	
	public OptionsScreen(CFlagGame game) {
		super(game, true,false,false);
		
		optionsAtlas = g.createImageAtlas("options");
		font = g.createFont("salsa", 22);
		
		trackStr = new Image[Musics.values().length];
		
		gOpts = new GameOpts();
		gOpts.setGameOpts(MemStore.gameOPTS);
	}

	@Override
	public void show() {
		super.show();
		
		g.setImageAtlas(commonAtlas);
		g.setFont(font);
		
		int spaceFromMid=20;
		saveButtonUp = new Image(g.getFlipTexRegion("rectbuttonup"));
		saveButtonUp.setPosition(bottomBar.getWidth()/2-saveButtonUp.getWidth()-spaceFromMid, bottomBar.getY() + (bottomBar.getHeight() - saveButtonUp.getHeight()) / 2);
		saveButtonDown = new Image(g.getFlipTexRegion("rectbuttondown"));
		saveButtonDown.setVisible(false);
		saveButtonDown.setPosition(saveButtonUp.getX(),saveButtonUp.getY());

		discardButtonUp = new Image(g.getFlipTexRegion("rectbuttonup"));
		discardButtonUp.setPosition(bottomBar.getWidth()/2+spaceFromMid, bottomBar.getY() + (bottomBar.getHeight() - discardButtonUp.getHeight()) / 2);
		discardButtonDown = new Image(g.getFlipTexRegion("rectbuttondown"));
		discardButtonDown.setPosition(discardButtonUp.getX(),discardButtonUp.getY());
		discardButtonDown.setVisible(false);

		g.setImageAtlas(optionsAtlas);
		
		titleStr = new Image(g.getFlipTexRegion("title"));
		titleStr.setPosition((topBar.getWidth() - titleStr.getWidth()) / 2, (topBar.getHeight() - titleStr.getHeight()) / 2);
		
		saveStr = new Image(g.getFlipTexRegion("save"));
		saveStr.setPosition(saveButtonUp.getX(),saveButtonUp.getY());
		
		discardStr = new Image(g.getFlipTexRegion("discard"));
		discardStr.setPosition(discardButtonUp.getX(),discardButtonUp.getY());

		enterNameStr = new Image(g.getFlipTexRegion("entername"));
		
		enterNameStr.setPosition(game.getSrcWidth(), game.getSrcHeight());
		
		profileStr = new Image(g.getFlipTexRegion("userprofile"));
		profileLeft = new Image(g.getFlipTexRegion("strokeleft"));
		ProfileRight = new Image(g.getFlipTexRegion("strokeright"));

		newButtonUp = new Image(g.getFlipTexRegion("newbuttonup"));
		newButtonDown = new Image(g.getFlipTexRegion("newbuttonDown"));
		newButtonGroup = new Group();

		delButtonUp = new Image(g.getFlipTexRegion("delButtonup"));
		delButtonDown = new Image(g.getFlipTexRegion("delButtonDown"));
		delButtonGroup = new Group();
		
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
			trackStr[trackNo].setVisible(gOpts.getMusicTrack().equals(music));
			trackStr[trackNo].addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					for (int i = 0; i < trackStr.length; i++) {
						if (trackStr[i].isVisible()) {
							trackStr[i].setVisible(false);
							trackStr[(i + 1) % trackStr.length].setVisible(true);
							Musics.valueOf(trackStr[(i + 1) % trackStr.length].getName()).loadAndPlay();
							gOpts.setMusicTrack(Musics.valueOf(trackStr[(i+1)%trackStr.length].getName()));
							break;
						}
					}
					return super.touchDown(event, x, y, pointer, button);
				}
			});
			trackNo++;
		}
	
		
		
		sound = new CheckBox("", g.getCheckBoxStyle());
		sound.setChecked(gOpts.isSoundOn());

		soundVolume = new Slider(0f, 1f, 0.1f, false, g.getSliderStyle());
		soundVolume.setValue(gOpts.getSoundVolume());

		music = new CheckBox("", g.getCheckBoxStyle());
		music.setChecked(gOpts.isMusicOn());

		musicVolume = new Slider(0f, 1f, 0.1f, false, g.getSliderStyle());
		musicVolume.setValue(gOpts.getMusicVolume());
		
		textBox = new TextField("", g.getTextBoxrStyle());
		
		swipeEnabled = new CheckBox("", g.getCheckBoxStyle());
		swipeEnabled.setChecked(gOpts.isSwipeMove());

		int x=80, y=75, yw=60;
		textBox.setPosition(x, y=y+yw);
		
		soundStr.setPosition(x, y=y+yw);
		sound.setPosition(x+160,  y);
		soundVolumeStr.setPosition(x+340, y);
		soundVolume.setPosition(x+495,  y);

		musicStr.setPosition(x, y=y+yw);
		music.setPosition(x+160,  y);
		musicVolumeStr.setPosition(x+340, y);
		musicVolume.setPosition(x+495,  y);

		musicTrackStr.setPosition(x, y=y+yw);
		trackLeft.setPosition(x+157, y);
		for (Image trkStr : trackStr) {
			trkStr.setPosition(x+180, y+2);
		}
		trackRight.setPosition(x+250, y);
		swipeToPlayStr.setPosition(x+340, y);
		swipeEnabled.setPosition(x+500,  y);

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

		textBox.setVisible(true);
		
		stage.addActor(titleStr);
		stage.addActor(saveButtonGroup);
		stage.addActor(discardButtonGroup);
		stage.addActor(textBox);
		stage.addActor(soundStr);
		stage.addActor(soundVolumeStr);
		stage.addActor(musicStr);
		stage.addActor(musicVolumeStr);
		stage.addActor(musicTrackStr);
		stage.addActor(swipeToPlayStr);
		stage.addActor(sound);
		stage.addActor(soundVolume);
		stage.addActor(music);
		stage.addActor(musicVolume);
		stage.addActor(trackGroup);
		stage.addActor(swipeEnabled);
		stage.addActor(argbFull);

		sound.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gOpts.setSoundOn(((CheckBox)actor).isChecked());
				Sounds.setState(gOpts.isSoundOn());
			}
		});
		soundVolume.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gOpts.setSoundVolume(((Slider) actor).getValue());
				Sounds.setVolume(gOpts.getSoundVolume());
			}
		});
		music.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gOpts.setMusicOn(((CheckBox)actor).isChecked());
				Musics.setState(gOpts.isMusicOn());
			}
		});
		musicVolume.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gOpts.setMusicVolume(((Slider) actor).getValue());
				Musics.setVolume(gOpts.getMusicVolume());
			}
		});
		swipeEnabled.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gOpts.setSwipeMove(((CheckBox)actor).isChecked());
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
				MemStore.gameOPTS.setGameOpts(gOpts);
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
				Musics.setState(MemStore.gameOPTS.isMusicOn());
				Musics.setVolume(MemStore.gameOPTS.getMusicVolume());
				MemStore.gameOPTS.getMusicTrack().loadAndPlay();
				
				discardButtonDown.addAction(sequence(fadeOut(0.2f), visible(false)));
				argbFull.addAction(sequence(visible(true), fadeIn(1f), run(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new MenuScreen(game));
					}
				})));
			}
		});

		
		trackLeft.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				for (int i = 0; i < trackStr.length; i++) {
					if(trackStr[i].isVisible()){
						trackStr[i].setVisible(false);
						trackStr[(i-1 + trackStr.length)%trackStr.length].setVisible(true);
						Musics.valueOf(trackStr[(i-1 + trackStr.length)%trackStr.length].getName()).loadAndPlay();
						gOpts.setMusicTrack(Musics.valueOf(trackStr[(i-1 + trackStr.length)%trackStr.length].getName()));
						break;
					}
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		trackRight.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				for (int i = 0; i < trackStr.length;i++) {
					if(trackStr[i].isVisible()){
						trackStr[i].setVisible(false);
						trackStr[(i+1)%trackStr.length].setVisible(true);
						Musics.valueOf(trackStr[(i+1)%trackStr.length].getName()).loadAndPlay();
						gOpts.setMusicTrack(Musics.valueOf(trackStr[(i+1)%trackStr.length].getName()));
						break;
					}
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		stage.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode==Keys.BACK){
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
		if(optionsAtlas!=null) optionsAtlas.dispose();
		if(font!=null) font.dispose();
	}

	private void changeDifficulty(Actor actor){
		Difficulty dclty = Difficulty.valueOf(actor.getName());
		boolean enabled = ((CheckBox)actor).isChecked();
		if(enabled) 
			gOpts.addDifficulty(dclty);
		else
			gOpts.removeDifficulty(dclty);
	}
}
