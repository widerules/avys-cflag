package com.avy.cflag.game;

import static com.avy.cflag.game.Constants.LVL_FILE_NAME;
import static com.avy.cflag.game.Constants.SAVE_GAME_FILE_NAME;
import static com.avy.cflag.game.Constants.SAVE_GAME_TAG_NAME;
import static com.avy.cflag.game.Constants.SCORE_DATA_FILE_NAME;
import static com.avy.cflag.game.Constants.SCORE_DATA_TAG_NAME;
import static com.avy.cflag.game.Constants.USERS_DATA_FILE_NAME;
import static com.avy.cflag.game.Constants.USERS_DATA_TAG_NAME;
import static com.avy.cflag.game.MemStore.acraMAP;
import static com.avy.cflag.game.MemStore.certSIGN;
import static com.avy.cflag.game.MemStore.curUserOPTS;
import static com.avy.cflag.game.MemStore.curUserSCORE;
import static com.avy.cflag.game.MemStore.encodedCERT;
import static com.avy.cflag.game.MemStore.lvlAuthorLEN;
import static com.avy.cflag.game.MemStore.lvlCntPerDCLTY;
import static com.avy.cflag.game.MemStore.lvlDataPerDCLTY;
import static com.avy.cflag.game.MemStore.lvlFieldDataLEN;
import static com.avy.cflag.game.MemStore.lvlHintLEN;
import static com.avy.cflag.game.MemStore.lvlLEN;
import static com.avy.cflag.game.MemStore.lvlNameLEN;
import static com.avy.cflag.game.MemStore.savedGame;
import static com.avy.cflag.game.MemStore.userLIST;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.avy.cflag.base.Musics;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.game.EnumStore.Difficulty;
import com.avy.cflag.game.utils.GameData;
import com.avy.cflag.game.utils.LevelScore;
import com.avy.cflag.game.utils.SaveThumbs;
import com.avy.cflag.game.utils.UserList;
import com.avy.cflag.game.utils.UserOptions;
import com.avy.cflag.game.utils.UserScore;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

public class GameUtils {

	public static void loadLevelData() {
		try {
			final InputStream fi = Gdx.files.internal(LVL_FILE_NAME).read();
			final byte fileData[] = new byte[fi.available()];
			fi.read(fileData);
			fi.close();

			final byte inData[] = decryptDate(fileData);

			final String lvlDataPerDclty[] = new String[Difficulty.length()];

			final int totLvls = inData.length / lvlLEN;
			final int dcltyPos = lvlFieldDataLEN + lvlNameLEN + lvlHintLEN + lvlAuthorLEN;
			for (int i = 0; i < totLvls; i++) {
				final byte[] curLvlData = new byte[lvlLEN];
				final Difficulty curDclty = PlayUtils.getDifficultyByVal(Integer.parseInt(new Integer(inData[(i * lvlLEN) + dcltyPos]) + "" + new Integer(inData[(i * lvlLEN) + dcltyPos + 1])));
				lvlCntPerDCLTY[curDclty.ordinal()]++;
				System.arraycopy(inData, i * lvlLEN, curLvlData, 0, lvlLEN);
				if (lvlDataPerDclty[curDclty.ordinal()] == null) {
					lvlDataPerDclty[curDclty.ordinal()] = "";
				}
				lvlDataPerDclty[curDclty.ordinal()] = lvlDataPerDclty[curDclty.ordinal()] + new String(curLvlData);
			}

			for (int i = 0; i < Difficulty.length(); i++) {
				lvlDataPerDCLTY[i] = new byte[lvlDataPerDclty[i].length()];
				lvlDataPerDCLTY[i] = lvlDataPerDclty[i].getBytes();
			}

		} catch (final Exception e) {
			final StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			acraMAP.put("encodedCert", encodedCERT);
			acraMAP.put("certSignature", certSIGN);
			acraMAP.put("HandledException", sw.toString());
			throw new RuntimeException("Error Decrypting/Reading Level Data File");
		}
	}

	public static byte[] decryptDate(final byte[] inputData) throws Exception {
		final IvParameterSpec ivSpec = new IvParameterSpec(certSIGN);
		final SecretKeySpec key = new SecretKeySpec(encodedCERT, "AES");
		final Cipher deCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		deCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		return deCipher.doFinal(inputData);
	}

	public static void loadGameAudio() {
		Sounds.setState(curUserOPTS.isSoundOn());
		Sounds.setVolume(curUserOPTS.getSoundVolume());
		Sounds.loadAll();

		Musics.setState(curUserOPTS.isMusicOn());
		Musics.setVolume(curUserOPTS.getMusicVolume());
		curUserOPTS.getMusicTrack().loadAndPlay();
	}

	public static void loadUserOptions() {
		final Preferences pr = Gdx.app.getPreferences(USERS_DATA_FILE_NAME);
		final String jsonStr = pr.getString(USERS_DATA_TAG_NAME);
		final Json jsn = new Json();
		userLIST = new UserList();
		curUserOPTS = new UserOptions();
		if ((jsonStr != "") && !jsonStr.equals("{}")) {
			userLIST = jsn.fromJson(UserList.class, jsonStr);
			curUserOPTS = userLIST.getCurrentUserOptions();
		}
	}

	public static void loadUserScores() {
		final Preferences pr = Gdx.app.getPreferences(curUserOPTS.getUserName() + "\\" + SCORE_DATA_FILE_NAME);
		final String jsonStr = pr.getString(SCORE_DATA_TAG_NAME);
		final Json jsn = new Json();
		curUserSCORE = new UserScore();
		if ((jsonStr != "") && !jsonStr.equals("{}")) {
			curUserSCORE = jsn.fromJson(UserScore.class, jsonStr);
		}
	}

	public static void loadAcraMap() {
		acraMAP.put(USERS_DATA_TAG_NAME, userLIST);
		acraMAP.put(SCORE_DATA_TAG_NAME, curUserSCORE);
	}

	public static void loadThumbs() {
		final SaveThumbs st = new SaveThumbs(Difficulty.Novice, 1);
		st.setPlayAtlas();
		for (final Difficulty dclty : Difficulty.values()) {
			st.setLevelDclty(dclty);
			st.createThumb();
		}
		st.disposePlayAtlas();
	}

	public static void loadGame() {
		final Preferences pr = Gdx.app.getPreferences(curUserOPTS.getUserName() + "\\" + SAVE_GAME_FILE_NAME);
		final String jsonStr = pr.getString(SAVE_GAME_TAG_NAME);
		final Json jsn = new Json();
		if (jsonStr != "") {
			savedGame = jsn.fromJson(GameData.class, jsonStr);
		} else {
			savedGame=null;	
		}
	}

	public static void saveGameOptions() {
		userLIST.updateUser(curUserOPTS);
		final Json jsn = new Json();
		final Preferences pr = Gdx.app.getPreferences(USERS_DATA_FILE_NAME);
		pr.putString(USERS_DATA_TAG_NAME, jsn.toJson(userLIST));
		pr.flush();
	}

	public static void saveUserScores(final Difficulty dclty, final int levelNo, final int movesPlayed, final int shotsTriggered, final boolean hintUsed) {
		final LevelScore currentScore = new LevelScore(levelNo, movesPlayed, shotsTriggered, hintUsed);
		if (levelNo <= curUserSCORE.getMaxPlayedLevel(dclty)) {
			curUserSCORE.updateScores(dclty, currentScore);
		} else {
			curUserSCORE.setMaxPlayedLevel(dclty, levelNo);
			curUserSCORE.addScores(dclty, currentScore);
		}
		final Json jsn = new Json();
		final Preferences pr = Gdx.app.getPreferences(curUserOPTS.getUserName() + "\\" + SCORE_DATA_FILE_NAME);
		pr.putString(SCORE_DATA_TAG_NAME, jsn.toJson(curUserSCORE));
		pr.flush();
	}

	public static void deleteUserScores(final ArrayList<String> deletedUsers) {
		for (final String userName : deletedUsers) {
			final Preferences pr1 = Gdx.app.getPreferences(userName + "\\" + SCORE_DATA_FILE_NAME);
			pr1.putString(SCORE_DATA_TAG_NAME, "");
			pr1.flush();
			deleteSavedGame(userName);
			curUserOPTS.setGameSaved(false);
		}
	}

	public static void deleteSavedGame(final String userName) {
		final Preferences pr2 = Gdx.app.getPreferences(userName + "\\" + SAVE_GAME_FILE_NAME);
		pr2.putString(SAVE_GAME_TAG_NAME, "");
		pr2.flush();
	}

	public static void saveGame(final GameData gData) {
		final Json jsn = new Json();
		final Preferences pr = Gdx.app.getPreferences(curUserOPTS.getUserName() + "\\" + SAVE_GAME_FILE_NAME);
		pr.putString(SAVE_GAME_TAG_NAME, jsn.toJson(gData));
		pr.flush();
		curUserOPTS.setGameSaved(true);
	}
}
