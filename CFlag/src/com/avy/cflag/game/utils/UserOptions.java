package com.avy.cflag.game.utils;

import com.avy.cflag.base.Musics;

public class UserOptions {
	private String userName;
	private boolean firstRun;
	private boolean gameSaved;
	private boolean soundOn;
	private boolean musicOn;
	private float musicVolume;
	private float soundVolume;
	private Musics musicTrack;
	private boolean swipeMove;

	public UserOptions() {
		userName = "Default";
		firstRun=true;
		gameSaved=false;
		soundOn = true;
		musicOn = true;
		soundVolume = 0.3f;
		musicVolume = 0.3f;
		musicTrack = Musics.track1;
		swipeMove = false;
	}

	public UserOptions(String inUserName) {
		userName = inUserName;
		firstRun=true;
		gameSaved=false;
		soundOn = true;
		musicOn = true;
		soundVolume = 0.3f;
		musicVolume = 0.3f;
		musicTrack = Musics.track1;
		swipeMove = false;
	}
	
	public void setGameOpts(UserOptions gOpts){
		userName = gOpts.userName;
		firstRun=gOpts.firstRun;
		gameSaved=gOpts.gameSaved;
		soundOn=gOpts.soundOn;
		musicOn=gOpts.musicOn;
		musicVolume=gOpts.musicVolume;
		soundVolume=gOpts.soundVolume;
		musicTrack=gOpts.musicTrack;
		swipeMove=gOpts.swipeMove;
	}

	public UserOptions clone() {
		UserOptions uo = new UserOptions();
		uo.setGameOpts(this);
		return uo;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean isFirstRun() {
		return firstRun;
	}
	
	public void setFirstRun(boolean firstRun) {
		this.firstRun = firstRun;
	}

	public boolean isGameSaved() {
		return gameSaved;
	}
	
	public void setGameSaved(boolean gameSaved) {
		this.gameSaved = gameSaved;
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

}
