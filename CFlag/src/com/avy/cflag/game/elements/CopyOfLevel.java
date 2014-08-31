package com.avy.cflag.game.elements;

import static com.avy.cflag.game.MemStore.lvlDATA;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.avy.cflag.base.Point;
import com.avy.cflag.game.MemStore;
import com.avy.cflag.game.MemStore.Difficulty;
import com.avy.cflag.game.MemStore.PlayImages;
import com.avy.cflag.game.Utils;

public class CopyOfLevel {

	private final byte[] lvlData = MemStore.lvlDATA;
	int totNoviceLevels = 0;
	int totEasyLevels = 0;
	int totMediumLevels = 0;
	int totHardLevels = 0;
	int totDeadlyLevels = 0;

	private final Point lvlFieldLen = MemStore.lvlFieldLEN;
	private final int lvlFieldTotalLen = lvlFieldLen.x * lvlFieldLen.y;
	private final int lvlNameLen = 31;
	private final int lvlHintLen = 256;
	private final int lvlAuthorLen = 31;
	private final int lvlDcltyLen = 2;
	private final int lvlLen = lvlFieldTotalLen + lvlNameLen + lvlHintLen + lvlAuthorLen + lvlDcltyLen;

	private final byte[] noviceLvlData = new byte[378*lvlLen];
	private final byte[] easyLvlData = new byte[816*lvlLen];
	private final byte[] mediumLvlData = new byte[595*lvlLen];
	private final byte[] hardLvlData = new byte[189*lvlLen];
	private final byte[] deadlyLvlData = new byte[52*lvlLen];

	private int lvlNum;
	private PlayImages[][] lvlBaseField;
	private PlayImages[][] lvlPlayField;
	private String lvlName;
	private String lvlHint;
	private String lvlAuthor;
	private Difficulty lvlDclty;

	private Point tankOrigPos;

	public CopyOfLevel() {
		lvlNum = 1;
		lvlBaseField = new PlayImages[lvlFieldLen.x][lvlFieldLen.y];
		lvlPlayField = new PlayImages[lvlFieldLen.x][lvlFieldLen.y];
		lvlDclty = Difficulty.Easy;
	}

	public void loadLevel(int inLvlNum) {

		lvlNum = inLvlNum;
		System.out.println(lvlNum);
		byte tmpLvlData[] = new byte[lvlLen];
		final char[] tmpLvlName = new char[lvlNameLen];
		final char[] tmpLvlAuthor = new char[lvlAuthorLen];
		final char[] tmpLvlHint = new char[lvlHintLen];
		final int[] tmpLvlDclty = new int[lvlDcltyLen];

		int lvlStrt, lvlPlayFieldStrt, lvlPlayFieldEnd, lvlNameStrt, lvlNameEnd, lvlHintStrt, lvlHintEnd, lvlAuthorStrt, lvlAuthorEnd, lvlDcltyStrt, lvlDcltyEnd, lvlEnd;

		lvlStrt = lvlPlayFieldStrt = (lvlNum - 1) * lvlLen;
		lvlPlayFieldEnd = lvlNameStrt = lvlPlayFieldStrt + lvlFieldTotalLen;
		lvlNameEnd = lvlHintStrt = lvlNameStrt + lvlNameLen;
		lvlHintEnd = lvlAuthorStrt = lvlHintStrt + lvlHintLen;
		lvlAuthorEnd = lvlDcltyStrt = lvlAuthorStrt + lvlAuthorLen;
		lvlDcltyEnd = lvlEnd = lvlDcltyStrt + lvlDcltyLen;

		int r = 0, c = 0;

		for (int i = lvlStrt; i < lvlEnd; i++) {
			tmpLvlData[r++]=lvlDATA[i];
			if (i < lvlPlayFieldEnd) {
//				tmpLvlData[r++]=lvlDATA[i];
			} else if (i >= lvlNameStrt && i < lvlNameEnd) {
				tmpLvlName[i - lvlNameStrt] = (char) lvlData[i];
			} else if (i >= lvlHintStrt && i < lvlHintEnd) {
				tmpLvlHint[i - lvlHintStrt] = (char) lvlData[i];
			} else if (i >= lvlAuthorStrt && i < lvlAuthorEnd) {
				tmpLvlAuthor[i - lvlAuthorStrt] = (char) lvlData[i];
			} else if (i >= lvlDcltyStrt && i < lvlDcltyEnd) {
				tmpLvlDclty[i - lvlDcltyStrt] = lvlData[i];
			}
		}

		lvlName = (new String(tmpLvlName)).trim();
		lvlAuthor = (new String(tmpLvlAuthor)).trim();
		lvlHint = (new String(tmpLvlHint)).trim();
		lvlDclty = Utils.getDifficultyByVal(Integer.parseInt(tmpLvlDclty[0] + "" + tmpLvlDclty[1]));

		switch (lvlDclty) {
			case Novice:
				System.arraycopy(tmpLvlData, 0, noviceLvlData, totNoviceLevels*lvlLen, lvlLen);
				totNoviceLevels++;
				break;
			case Easy:
				System.arraycopy(tmpLvlData, 0, easyLvlData, totEasyLevels*lvlLen, lvlLen);
				totEasyLevels++;
				break;
			case Medium:
				System.arraycopy(tmpLvlData, 0, mediumLvlData, totMediumLevels*lvlLen, lvlLen);
				totMediumLevels++;
				break;
			case Hard:
				System.arraycopy(tmpLvlData, 0, hardLvlData, totHardLevels*lvlLen, lvlLen);
				totHardLevels++;
				break;
			case Deadly:
				System.arraycopy(tmpLvlData, 0, deadlyLvlData, totDeadlyLevels*lvlLen, lvlLen);
				totDeadlyLevels++;
				break;
			default:
				break;
		}
	}

	public void increaseLevel() {
		if (lvlNum < 100) {
			lvlNum++;
		} else {
			lvlNum = 1;
		}
		loadLevel(lvlNum);
	}

	public void decreaseLevel() {
		if (lvlNum > 1) {
			lvlNum--;
		} else {
			lvlNum = 1;
		}
		loadLevel(lvlNum);
	}

	public PlayImages[][] getLvlBaseField() {
		return lvlBaseField;
	}

	public void setLvlBaseField(PlayImages[][] lvlBaseField) {
		this.lvlBaseField = lvlBaseField;
	}

	public PlayImages[][] getLvlPlayField() {
		return lvlPlayField;
	}

	public void setLvlPlayField(PlayImages[][] lvlPlayField) {
		this.lvlPlayField = lvlPlayField;
	}

	public String getLvlName() {
		return lvlName;
	}

	public void setLvlName(String lvlName) {
		this.lvlName = lvlName;
	}

	public String getLvlHint() {
		return lvlHint;
	}

	public void setLvlHint(String lvlHint) {
		this.lvlHint = lvlHint;
	}

	public String getLvlAuthor() {
		return lvlAuthor;
	}

	public void setLvlAuthor(String lvlAuthor) {
		this.lvlAuthor = lvlAuthor;
	}

	public Difficulty getLvlDclty() {
		return lvlDclty;
	}

	public void setLvlDclty(Difficulty lvlDclty) {
		this.lvlDclty = lvlDclty;
	}

	public Point getTankOrigPos() {
		return tankOrigPos;
	}

	public void setTankOrigPos(Point tankOrigPos) {
		this.tankOrigPos = tankOrigPos;
	}

	public static void main(String[] args) {
		try {
			
			final InputStream fi = new FileInputStream("data\\LaserTank.lvl");
			lvlDATA = new byte[fi.available()];
			fi.read(lvlDATA);
			fi.close();
			
			CopyOfLevel col = new CopyOfLevel();
			int totalLevels=lvlDATA.length/col.lvlLen;
			
			for (int i = 1; i <= totalLevels; i++) {
				col.loadLevel(i);
			}
			
			System.out.println(col.totNoviceLevels);
			System.out.println(col.totEasyLevels);
			System.out.println(col.totMediumLevels);
			System.out.println(col.totHardLevels);
			System.out.println(col.totDeadlyLevels);
			
			byte[] outputLvlData =  new byte[col.lvlLen*400];
			System.arraycopy(col.noviceLvlData, 0, outputLvlData, 0, col.lvlLen*100);
			System.arraycopy(col.easyLvlData, 0, outputLvlData, col.lvlLen*100, col.lvlLen*100);
			System.arraycopy(col.mediumLvlData, 0, outputLvlData, col.lvlLen*200, col.lvlLen*100);
			System.arraycopy(col.hardLvlData, 0, outputLvlData, col.lvlLen*300, col.lvlLen*50);
			System.arraycopy(col.deadlyLvlData, 0, outputLvlData, col.lvlLen*350, col.lvlLen*50);

			
			FileOutputStream fo = new FileOutputStream("data\\output.lvl");
//			fo.write(col.noviceLvlData);
//			fo.write(col.easyLvlData);
//			fo.write(col.mediumLvlData);
//			fo.write(col.hardLvlData);
//			fo.write(col.deadlyLvlData);
			fo.write(outputLvlData);
			fo.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
