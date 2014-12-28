package com.avy.cflag.game.utils;

import com.avy.cflag.base.Musics;
import com.avy.cflag.game.EnumStore.Difficulty;

public class UserOptions {
	private String userName;
	private boolean firstRun;
	private boolean gameSaved;
	private Difficulty lastDclty;
	private boolean soundOn;
	private boolean musicOn;
	private float musicVolume;
	private float soundVolume;
	private Musics musicTrack;
	private boolean swipeMove;

	public UserOptions() {
		userName = "No Profile";
		firstRun = true;
		gameSaved = false;
		lastDclty = Difficulty.Novice;
		soundOn = true;
		musicOn = true;
		soundVolume = 0.3f;
		musicVolume = 0.3f;
		musicTrack = Musics.track1;
		swipeMove = false;
	}

	public UserOptions(final String inUserName) {
		userName = inUserName;
		firstRun = true;
		gameSaved = false;
		lastDclty = Difficulty.Novice;
		soundOn = true;
		musicOn = true;
		soundVolume = 0.3f;
		musicVolume = 0.3f;
		musicTrack = Musics.track1;
		swipeMove = false;
	}

	public void setGameOpts(final UserOptions inOpts) {
		userName = inOpts.userName;
		firstRun = inOpts.firstRun;
		gameSaved = inOpts.gameSaved;
		lastDclty = inOpts.lastDclty;
		soundOn = inOpts.soundOn;
		musicOn = inOpts.musicOn;
		musicVolume = inOpts.musicVolume;
		soundVolume = inOpts.soundVolume;
		musicTrack = inOpts.musicTrack;
		swipeMove = inOpts.swipeMove;
	}

	@Override
	public UserOptions clone() {
		final UserOptions uo = new UserOptions();
		uo.setGameOpts(this);
		return uo;
	}

	public String getUserName() {
		return userName;
	}

	public boolean isFirstRun() {
		return firstRun;
	}

	public void setFirstRun(final boolean firstRun) {
		this.firstRun = firstRun;
	}

	public boolean isGameSaved() {
		return gameSaved;
	}

	public void setGameSaved(final boolean gameSaved) {
		this.gameSaved = gameSaved;
	}

	public void setlastDclty(final Difficulty lastDclty) {
		this.lastDclty = lastDclty;
	}

	public Difficulty getLastDifficulty() {
		return lastDclty;
	}

	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(final boolean soundOn) {
		this.soundOn = soundOn;
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public void setMusicOn(final boolean musicOn) {
		this.musicOn = musicOn;
	}

	public float getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(final float musicVolume) {
		this.musicVolume = musicVolume;
	}

	public float getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(final float soundVolume) {
		this.soundVolume = soundVolume;
	}

	public Musics getMusicTrack() {
		return musicTrack;
	}

	public void setMusicTrack(final Musics musicTrack) {
		this.musicTrack = musicTrack;
	}

	public boolean isSwipeMove() {
		return swipeMove;
	}

	public void setSwipeMove(final boolean swipeMove) {
		this.swipeMove = swipeMove;
	}

}
