package com.avy.cflag.game.elements;

import static com.avy.cflag.game.MemStore.lvlAuthorLEN;
import static com.avy.cflag.game.MemStore.lvlDataPerDCLTY;
import static com.avy.cflag.game.MemStore.lvlDcltyLEN;
import static com.avy.cflag.game.MemStore.lvlFieldDataLEN;
import static com.avy.cflag.game.MemStore.lvlFieldLEN;
import static com.avy.cflag.game.MemStore.lvlHintLEN;
import static com.avy.cflag.game.MemStore.lvlLEN;
import static com.avy.cflag.game.MemStore.lvlNameLEN;

import com.avy.cflag.base.Point;
import com.avy.cflag.game.MemStore.Difficulty;
import com.avy.cflag.game.MemStore.PlayImages;
import com.avy.cflag.game.Utils;

public class Level {

	private byte[] lvlData;

	private int lvlNum;
	private PlayImages[][] lvlBaseField;
	private PlayImages[][] lvlPlayField;
	private String lvlName;
	private String lvlHint;
	private String lvlAuthor;
	private Difficulty lvlDclty;

	private Point tankOrigPos;

	public Level() {
		lvlNum = 1;
		lvlData = new byte[lvlLEN];
		lvlBaseField = new PlayImages[lvlFieldLEN.x][lvlFieldLEN.y];
		lvlPlayField = new PlayImages[lvlFieldLEN.x][lvlFieldLEN.y];
		lvlDclty = Difficulty.Easy;
	}

	public void loadLevel(Difficulty inLvlDclty, int inLvlNum) {

		lvlNum = inLvlNum;
		lvlDclty = inLvlDclty;
		lvlData = lvlDataPerDCLTY[inLvlDclty.ordinal()];

		final char[] tmpLvlName = new char[lvlNameLEN];
		final char[] tmpLvlAuthor = new char[lvlAuthorLEN];
		final char[] tmpLvlHint = new char[lvlHintLEN];
		final int[] tmpLvlDclty = new int[lvlDcltyLEN];

		int lvlStrt, lvlPlayFieldStrt, lvlPlayFieldEnd, lvlNameStrt, lvlNameEnd, lvlHintStrt, lvlHintEnd, lvlAuthorStrt, lvlAuthorEnd, lvlDcltyStrt, lvlDcltyEnd, lvlEnd;

		lvlStrt = lvlPlayFieldStrt = (lvlNum - 1) * lvlLEN;
		lvlPlayFieldEnd = lvlNameStrt = lvlPlayFieldStrt + lvlFieldDataLEN;
		lvlNameEnd = lvlHintStrt = lvlNameStrt + lvlNameLEN;
		lvlHintEnd = lvlAuthorStrt = lvlHintStrt + lvlHintLEN;
		lvlAuthorEnd = lvlDcltyStrt = lvlAuthorStrt + lvlAuthorLEN;
		lvlDcltyEnd = lvlEnd = lvlDcltyStrt + lvlDcltyLEN;

		int r = 0, c = 0;

		for (int i = lvlStrt; i < lvlEnd; i++) {
			if (i < lvlPlayFieldEnd) {
				if (c == 16) {
					r++;
					c = 0;
				}

				final PlayImages po = Utils.getPlayImage(lvlData[i]);
				switch (po) {
					case Villain_D:
					case Villain_L:
					case Villain_R:
					case Villain_U:
					case MBlock:
					case MMirror_D:
					case MMirror_L:
					case MMirror_R:
					case MMirror_U:
					case Flag:
					case RMirror_D:
					case RMirror_L:
					case RMirror_R:
					case RMirror_U:
						lvlBaseField[r][c] = PlayImages.Grass;
						lvlPlayField[r][c] = po;
						break;
					case Hero_U:
						lvlBaseField[r][c] = PlayImages.Grass;
						lvlPlayField[r][c] = po;
						tankOrigPos = new Point(r, c);
						break;
					default:
						lvlBaseField[r][c] = po;
						lvlPlayField[r][c] = po;
						break;
				}
				c++;
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
}
