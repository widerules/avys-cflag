package com.avy.cflag.game;

import java.util.HashMap;

import com.avy.cflag.base.AcraMap;
import com.avy.cflag.base.Point;
import com.avy.cflag.game.EnumStore.Difficulty;
import com.avy.cflag.game.utils.GameData;
import com.avy.cflag.game.utils.HelpMsg;
import com.avy.cflag.game.utils.UserList;
import com.avy.cflag.game.utils.UserOptions;
import com.avy.cflag.game.utils.UserScore;
import com.badlogic.gdx.utils.Array;

public class MemStore {

	public static AcraMap acraMAP;
	public static final Point lvlFieldLEN = new Point(16, 16);
	public static final int lvlFieldDataLEN = lvlFieldLEN.x * lvlFieldLEN.y;
	public static final int lvlNameLEN = 31;
	public static final int lvlHintLEN = 256;
	public static final int lvlAuthorLEN = 31;
	public static final int lvlDcltyLEN = 2;
	public static final int lvlLEN = lvlFieldDataLEN + lvlNameLEN + lvlHintLEN + lvlAuthorLEN + lvlDcltyLEN;

	public static byte[][] lvlDataPerDCLTY = new byte[Difficulty.length()][];
	public static int lvlCntPerDCLTY[] = new int[Difficulty.length()];
	public static int dummyLvlCntPerDCLTY[] = { 100, 100, 100, 50, 50 };

	public static UserList userLIST = null;
	public static UserScore curUserSCORE = null;
	public static UserOptions curUserOPTS = null;
	public static GameData savedGame = null;
	public static HashMap<String, Array<HelpMsg>> helpDATA = null;
	
	public static Point pltfrmStartPOS = null;
	public static Point pltfrmLEN = null;
	public static final Point playImageOrigLEN = new Point(30, 30);
	public static Point playImageScaledLEN = null;
	public static float streamStateTime[] = { 0f, 0f, 0f, 0f };

	public static byte[] encodedCERT;
	public static byte[] certSIGN;

}
