package com.avy.cflag.game.utils;

import static com.avy.cflag.game.MemStore.lvlFieldLEN;

import com.avy.cflag.base.Point;
import com.avy.cflag.game.PlayUtils;
import com.avy.cflag.game.EnumStore.Direction;
import com.avy.cflag.game.EnumStore.PlayImages;
import com.avy.cflag.game.EnumStore.TankState;
import com.avy.cflag.game.elements.Bullet;
import com.avy.cflag.game.elements.Hero;

public class UnDoData {
	private Point curTankPos;
	private Direction curTankDirection;
	private TankState curTankState;
	private TankState storedTankState;
	private int tankStateTime;
	private int tankMoves;
	private int tankShots;
	private int undoChangeCount;
	private Bullet curTankBullet;
	private String lvlBaseFieldStr;
	private String lvlPlayFieldStr;
	

	public void shrinkData(Hero hero) {
		curTankPos=hero.getCurTankPos();
		curTankDirection=hero.getCurTankDirection();
		curTankState=hero.getCurTankState();
		storedTankState=hero.getStoredTankState();
		tankStateTime=hero.getTankStateTime();
		tankMoves=hero.getTankMoves();
		tankShots=hero.getTankShots();
		undoChangeCount=hero.getUndoChangeCount();
		curTankBullet=hero.getCurTankBullet().clone();
		
		PlayImages[][] lvlBaseField = hero.getLvlBaseField();
		PlayImages[][] lvlPlayField = hero.getLvlPlayField();
		
		lvlBaseFieldStr="";
		lvlPlayFieldStr="";
		
		for (int x = 0; x < lvlFieldLEN.x; x++) {
			for (int y = 0; y < lvlFieldLEN.y; y++) {
				lvlBaseFieldStr = lvlBaseFieldStr + lvlBaseField[x][y].id + ",";
				lvlPlayFieldStr=lvlPlayFieldStr+lvlPlayField[x][y].id + ",";
			}
			lvlBaseFieldStr = lvlBaseFieldStr + "~";
			lvlPlayFieldStr = lvlPlayFieldStr + "~";
		}
	}

	public Hero expandData() {
		final Hero newData = new Hero();
		newData.setCurTankPos(curTankPos);
		newData.setCurTankDirection(curTankDirection);
		newData.setCurTankState(curTankState);
		newData.setStoredTankState(storedTankState);
		newData.setTankStateTime(tankStateTime);
		newData.setTankMoves(tankMoves);
		newData.setTankShots(tankShots);
		newData.setUndoChangeCount(undoChangeCount);
		newData.setCurTankBullet(curTankBullet.clone());
		
		PlayImages baseField[][] = new PlayImages[lvlFieldLEN.x][lvlFieldLEN.y];
		PlayImages playField[][] = new PlayImages[lvlFieldLEN.x][lvlFieldLEN.y];
		
		String baseFieldRowData[] = lvlBaseFieldStr.split("~");
		String playFieldRowData[] = lvlPlayFieldStr.split("~");
		for (int x = 0; x < lvlFieldLEN.x; x++) {
			String baseFieldColData[] = baseFieldRowData[x].split(",");
			String playFieldColData[] = playFieldRowData[x].split(",");
			for (int y = 0; y < lvlFieldLEN.y; y++) {
				baseField[x][y]=PlayUtils.getPlayImage(Integer.parseInt(baseFieldColData[y]));
				playField[x][y]=PlayUtils.getPlayImage(Integer.parseInt(playFieldColData[y]));
			}
		}

		newData.setLvlBaseField(baseField);
		newData.setLvlPlayField(playField);
		return newData;
	}
	
}
