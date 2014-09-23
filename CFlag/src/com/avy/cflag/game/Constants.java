package com.avy.cflag.game;

public class Constants {

	private Constants() {
		throw new AssertionError();
	}

	public static final String ATLAS_FLDR = "atlas";

	public static final String DATA_FLDR = "data";
	public static final String LVL_FILE_NAME = DATA_FLDR + "/output.lvl";

	public static final String SCORE_PREF_FILE_NAME = "scores";
	public static final String SAVE_GAME_FILE_NAME = "savegame";
	public static final String SCORE_PREF_TAG_NAME = "data";
	public static final String SAVE_GAME_TAG_NAME = "savegame";

	public static final String OPTIONS_PREF_FILE_NAME = "options";
	public static final String OPTIONS_PREF_TAG_NAME = "data";

	public static final String SOUNDS_FLDR = "sounds";
	public static final String MUSICS_FLDR = "music";
}
