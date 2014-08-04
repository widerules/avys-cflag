package com.avy.cflag.game.utils;

import java.util.ArrayList;

import com.avy.cflag.base.Musics;
import com.avy.cflag.game.MemStore;
import com.avy.cflag.game.MemStore.Difficulty;

public class GameOpts {
	private boolean firstRun;
	private boolean soundOn;
	private boolean musicOn;
	private float musicVolume;
	private float soundVolume;
	private Musics musicTrack;
	private boolean swipeMove;
	private ArrayList<Difficulty> selectedDclties;

	public GameOpts() {
		firstRun=true;
		soundOn = true;
		musicOn = true;
		soundVolume = 0.3f;
		musicVolume = 0.3f;
		musicTrack = Musics.track1;
		swipeMove = false;
		selectedDclties = new ArrayList<MemStore.Difficulty>();
		for (final MemStore.Difficulty curDclty : MemStore.Difficulty.values()) {
			selectedDclties.add(curDclty);
		}
	}
	
	public void setGameOpts(GameOpts gOpts){
		firstRun=gOpts.firstRun;
		soundOn=gOpts.soundOn;
		musicOn=gOpts.musicOn;
		musicVolume=gOpts.musicVolume;
		soundVolume=gOpts.soundVolume;
		musicTrack=gOpts.musicTrack;
		swipeMove=gOpts.swipeMove;
		selectedDclties=new ArrayList<MemStore.Difficulty>();
		for (Difficulty dclty : gOpts.selectedDclties) {
			selectedDclties.add(dclty);
		}
	}
	
	public boolean isFirstRun() {
		return firstRun;
	}
	
	public void setFirstRun(boolean firstRun) {
		this.firstRun = firstRun;
	}
	
	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public void setMusicOn(boolean musicOn) {
		this.musicOn = musicOn;
	}

	public float getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(float musicVolume) {
		this.musicVolume = musicVolume;
	}

	public float getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(float soundVolume) {
		this.soundVolume = soundVolume;
	}

	public Musics getMusicTrack() {
		return musicTrack;
	}

	public void setMusicTrack(Musics musicTrack) {
		this.musicTrack = musicTrack;
	}

	public boolean isSwipeMove() {
		return swipeMove;
	}

	public void setSwipeMove(boolean swipeMove) {
		this.swipeMove = swipeMove;
	}

	public void addDifficulty(Difficulty dclty) {
		selectedDclties.add(dclty);
	}

	public void removeDifficulty(Difficulty dclty) {
		selectedDclties.remove(dclty);
	}

	public boolean difficultyExists(Difficulty dclty) {
		return selectedDclties.contains(dclty);
	}

	public ArrayList<Difficulty> getSelectedDclties() {
		return selectedDclties;
	}
}
