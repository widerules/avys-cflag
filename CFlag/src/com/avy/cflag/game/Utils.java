package com.avy.cflag.game;

import static com.avy.cflag.game.Constants.LVL_FILE_NAME;
import static com.avy.cflag.game.Constants.OPTIONS_PREF_FILE_NAME;
import static com.avy.cflag.game.Constants.OPTIONS_PREF_TAG_NAME;
import static com.avy.cflag.game.Constants.SAVE_GAME_FILE_NAME;
import static com.avy.cflag.game.Constants.SAVE_GAME_TAG_NAME;
import static com.avy.cflag.game.Constants.SCORE_PREF_FILE_NAME;
import static com.avy.cflag.game.Constants.SCORE_PREF_TAG_NAME;
import static com.avy.cflag.game.MemStore.gameOPTS;
import static com.avy.cflag.game.MemStore.lvlDATA;
import static com.avy.cflag.game.MemStore.lvlFieldLEN;
import static com.avy.cflag.game.MemStore.playImageScaledLEN;
import static com.avy.cflag.game.MemStore.pltfrmStartPOS;
import static com.avy.cflag.game.MemStore.userSCORE;

import java.io.InputStream;

import com.avy.cflag.base.Musics;
import com.avy.cflag.base.Point;
import com.avy.cflag.base.Sounds;
import com.avy.cflag.game.MemStore.Difficulty;
import com.avy.cflag.game.MemStore.Direction;
import com.avy.cflag.game.MemStore.PlayImages;
import com.avy.cflag.game.utils.GameData;
import com.avy.cflag.game.utils.GameOpts;
import com.avy.cflag.game.utils.LevelScore;
import com.avy.cflag.game.utils.UserScore;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

public class Utils {

	public static void loadLevelData() {
		try {
			final InputStream fi = Gdx.files.internal(LVL_FILE_NAME).read();
			lvlDATA = new byte[fi.available()];
			fi.read(lvlDATA);
			fi.close();
		} catch (final Exception e) {
			throw new RuntimeException("Error Reading Level Data File");
		}
	}

	public static void loadUserScores() {
		final Preferences pr = Gdx.app.getPreferences(SCORE_PREF_FILE_NAME);
		final String jsonStr = pr.getString(SCORE_PREF_TAG_NAME);
		final Json jsn = new Json();
		userSCORE = new UserScore();
		if (jsonStr != "") {
			userSCORE = jsn.fromJson(UserScore.class, jsonStr);
		}
	}

	public static void saveUserScores(int levelNo, int movesPlayed, int shotsTriggered, boolean hintUsed) {
		final LevelScore currentScore = new LevelScore(levelNo, movesPlayed, shotsTriggered, hintUsed);
		if (levelNo <= userSCORE.getMaxPlayedLevel()) {
			userSCORE.setCurrentLevel(levelNo);
			userSCORE.updateScores(currentScore);
		} else {
			userSCORE.setMaxPlayedLevel(levelNo);
			userSCORE.setCurrentLevel(levelNo);
			userSCORE.addScores(currentScore);
		}
		final Json jsn = new Json();
		final Preferences pr = Gdx.app.getPreferences(SCORE_PREF_FILE_NAME);
		pr.putString(SCORE_PREF_TAG_NAME, jsn.toJson(userSCORE));
		pr.flush();
	}
	
	public static GameData loadGame(){
		final Preferences pr = Gdx.app.getPreferences(SAVE_GAME_FILE_NAME);
		final String jsonStr = pr.getString(SAVE_GAME_FILE_NAME);
		final Json jsn = new Json();
		if (jsonStr != "") {
			return(jsn.fromJson(GameData.class, jsonStr));
		}
		return null;
	}

	public static void saveGame(GameData gData){
		final Json jsn = new Json();
		final Preferences pr = Gdx.app.getPreferences(SAVE_GAME_FILE_NAME);
		pr.putString(SAVE_GAME_TAG_NAME, jsn.toJson(gData));
		pr.flush();
		
	}
	
	public static void loadGameOptions() {
		final Preferences pr = Gdx.app.getPreferences(OPTIONS_PREF_FILE_NAME);
		final String jsonStr = pr.getString(OPTIONS_PREF_TAG_NAME);
		final Json jsn = new Json();
		gameOPTS = new GameOpts();
		if (jsonStr != "") {
			gameOPTS = jsn.fromJson(GameOpts.class, jsonStr);
		}
	}

	public static void saveGameOptions() {
		final Json jsn = new Json();
		final Preferences pr = Gdx.app.getPreferences(OPTIONS_PREF_FILE_NAME);
		pr.putString(OPTIONS_PREF_TAG_NAME, jsn.toJson(gameOPTS));
		pr.flush();
	}

	public static void loadGameAudio() {
		Sounds.setState(gameOPTS.isSoundOn());
		Sounds.setVolume(gameOPTS.getSoundVolume());
		Sounds.loadAll();

		Musics.setState(gameOPTS.isMusicOn());
		Musics.setVolume(gameOPTS.getMusicVolume());
		gameOPTS.getMusicTrack().loadAndPlay();
	}

	public static Difficulty getDifficulty(int inDcltyValue) {
		Difficulty outDclty = null;

		for (final Difficulty curDclty : Difficulty.values()) {
			if (curDclty.dcltyValue == inDcltyValue) {
				outDclty = curDclty;
			}
		}
		return outDclty;
	}

	public static PlayImages getPlayImage(int inFieldId) {
		PlayImages outPlayObject = null;

		for (final PlayImages curPlayObject : PlayImages.values()) {
			if (curPlayObject.id == inFieldId) {
				outPlayObject = curPlayObject;
			}
		}
		return outPlayObject;
	}

	public static Point getCenterPixPos(Point inPos) {
		final Point outPoint = new Point();
		outPoint.x = pltfrmStartPOS.x + inPos.x * playImageScaledLEN.x + playImageScaledLEN.x / 2;
		outPoint.y = pltfrmStartPOS.y + inPos.y * playImageScaledLEN.y + playImageScaledLEN.y / 2;
		return outPoint;
	}

	public static PlayImages turnTank(Direction inDirection) {
		PlayImages outTank = PlayImages.Hero_U;
		switch (inDirection) {
			case Up:
				outTank = PlayImages.Hero_U;
				break;
			case Right:
				outTank = PlayImages.Hero_R;
				break;
			case Down:
				outTank = PlayImages.Hero_D;
				break;
			case Left:
				outTank = PlayImages.Hero_L;
				break;
		}
		return outTank;
	}

	public static PlayImages destroyTank(PlayImages curTank) {
		PlayImages outTank = PlayImages.Villain_U;
		switch (getDirection(curTank)) {
			case Up:
				outTank = PlayImages.DVillain_U;
				break;
			case Right:
				outTank = PlayImages.DVillain_R;
				break;
			case Down:
				outTank = PlayImages.DVillain_D;
				break;
			case Left:
				outTank = PlayImages.DVillain_L;
				break;
		}
		return outTank;
	}

	public static Direction getDirection(PlayImages curObj) {
		Direction outDirection = Direction.Up;
		final String objName = curObj.name();
		if (objName.contains("_U")) {
			outDirection = Direction.Up;
		}
		if (objName.contains("_R")) {
			outDirection = Direction.Right;
		}
		if (objName.contains("_D")) {
			outDirection = Direction.Down;
		}
		if (objName.contains("_L")) {
			outDirection = Direction.Left;
		}
		return outDirection;
	}

	public static Point getNextPosition(Point curPos, Direction moveDirection) {
		final Point nxtPos = new Point();

		switch (moveDirection) {
			case Up:
				nxtPos.x = curPos.x;
				nxtPos.y = curPos.y - 1;
				break;
			case Right:
				nxtPos.x = curPos.x + 1;
				nxtPos.y = curPos.y;
				break;
			case Down:
				nxtPos.x = curPos.x;
				nxtPos.y = curPos.y + 1;
				break;
			case Left:
				nxtPos.x = curPos.x - 1;
				nxtPos.y = curPos.y;
				break;
		}

		return nxtPos;
	}

	public static boolean positionWithinBounds(Point curPoint) {
		return curPoint.x >= 0 && curPoint.x < lvlFieldLEN.x && curPoint.y >= 0 && curPoint.y < lvlFieldLEN.y;
	}

	public static boolean inBoundsRect(Point event, int x, int y, int width, int height) {
		return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
	}

	public static boolean inBoundsRect(Point event, Point posOnScreen, Point imgLen) {
		return event.x > posOnScreen.x && event.x < posOnScreen.x + imgLen.x - 1 && event.y > posOnScreen.y && event.y < posOnScreen.y + imgLen.y - 1;
	}

	public static boolean inBoundsPolygon(Point p, Point[] polygon) {
		double minX = polygon[0].x;
		double maxX = polygon[0].x;
		double minY = polygon[0].y;
		double maxY = polygon[0].y;
		for (int i = 1; i < polygon.length; i++) {
			final Point q = polygon[i];
			minX = Math.min(q.x, minX);
			maxX = Math.max(q.x, maxX);
			minY = Math.min(q.y, minY);
			maxY = Math.max(q.y, maxY);
		}

		if (p.x < minX || p.x > maxX || p.y < minY || p.y > maxY) {
			return false;
		}

		boolean inside = false;
		for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
			if (polygon[i].y > p.y != polygon[j].y > p.y && p.x < (polygon[j].x - polygon[i].x) * (p.y - polygon[i].y) / (polygon[j].y - polygon[i].y) + polygon[i].x) {
				inside = !inside;
			}
		}
		return inside;
	}

	public static boolean inBoundsRect(Point event, int topLeftX, int topLeftY, int topRightX, int topRightY, int bottomLeftX, int bottomLeftY, int bottomRightX, int bottomRightY) {
		final Point polygon[] = new Point[4];
		polygon[0] = new Point(topLeftX, topLeftY);
		polygon[1] = new Point(topRightX, topRightY);
		polygon[2] = new Point(bottomLeftX, bottomLeftY);
		polygon[3] = new Point(bottomRightX, bottomRightY);
		return inBoundsPolygon(new Point(event.x, event.y), polygon);
	}

	@SuppressWarnings("rawtypes")
	public static Object resizeArray(Object oldArray, int newSize) {
		final int oldSize = java.lang.reflect.Array.getLength(oldArray);
		final Class elementType = oldArray.getClass().getComponentType();
		final Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
		final int preserveLength = Math.min(oldSize, newSize);

		if (preserveLength > 0) {
			System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
		}

		return newArray;
	}
}
