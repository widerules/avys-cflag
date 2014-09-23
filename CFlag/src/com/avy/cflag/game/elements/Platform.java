package com.avy.cflag.game.elements;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;

import java.util.ArrayList;
import java.util.Random;

import com.avy.cflag.base.AnimActor;
import com.avy.cflag.base.Graphics;
import com.avy.cflag.base.PixelMap;
import com.avy.cflag.base.Point;
import com.avy.cflag.game.MemStore;
import com.avy.cflag.game.MemStore.PlayImages;
import com.avy.cflag.game.Utils;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Platform {

	private Group pltfrm;
	private Image grass;

	private final Point pltfrmStrt;
	private Point pltfrmLen;
	private final Point playImageOrigLen;
	private Point PlayImageScaledLen;

	private int[][] baseId;
	private int[][] topId;
	private AnimActor[][] baseActor;
	private AnimActor[][] topActor;

	private boolean firstRun;

	private int villainCount;
	private int mmirrorCount;
	private int rmirrorCount;
	private int animDelayCount;
	private int curDelayCount;

	public boolean heroReady;

	public Platform(Group pltfrm) {
		MemStore.pltfrmStartPOS = new Point((int) pltfrm.getX(), (int) pltfrm.getY());
		MemStore.pltfrmLEN = pltfrmLen = new Point((int) pltfrm.getWidth(), (int) pltfrm.getHeight());
		pltfrmStrt = new Point(0, 0);
		this.pltfrm = pltfrm;
		playImageOrigLen = MemStore.playImageOrigLEN;
		MemStore.playImageScaledLEN = PlayImageScaledLen = new Point(pltfrmLen.x / MemStore.lvlFieldLEN.x, pltfrmLen.y / MemStore.lvlFieldLEN.y);
		grass = PlayImages.Grass.getAnimActor();
		grass.setSize(pltfrmLen.x, pltfrmLen.y);
		this.pltfrm.addActor(grass);
		baseId = new int[MemStore.lvlFieldLEN.x][MemStore.lvlFieldLEN.y];
		topId = new int[MemStore.lvlFieldLEN.x][MemStore.lvlFieldLEN.y];
		baseActor = new AnimActor[MemStore.lvlFieldLEN.x][MemStore.lvlFieldLEN.y];
		topActor = new AnimActor[MemStore.lvlFieldLEN.x][MemStore.lvlFieldLEN.y];
		for (int i = 0; i < MemStore.lvlFieldLEN.x; i++) {
			for (int j = 0; j < MemStore.lvlFieldLEN.y; j++) {
				baseId[i][j] = 0;
				topId[i][j] = 0;
				baseActor[i][j] = null;
				topActor[i][j] = null;
			}
		}
		villainCount = 0;
		mmirrorCount = 0;
		rmirrorCount = 0;
		animDelayCount = 60;
		curDelayCount = 0;
		firstRun = true;
		heroReady = false;
	}

	public Platform(Point pltfrmStrt, Point pltfrmLen) {
		this.pltfrmStrt = pltfrmStrt;
		this.pltfrmLen = pltfrmLen;
		playImageOrigLen = MemStore.playImageOrigLEN;
		MemStore.playImageScaledLEN = PlayImageScaledLen = new Point(pltfrmLen.x / MemStore.lvlFieldLEN.x, pltfrmLen.y / MemStore.lvlFieldLEN.y);
	}

	public void randomizeAnims() {
		if (curDelayCount > animDelayCount && (villainCount > 0 || mmirrorCount > 0 || rmirrorCount > 0)) {
			curDelayCount = 0;
			int tempVillainCnt = 0;
			int tempMmirrorCnt = 0;
			int tempRmirrorCnt = 0;
			final ArrayList<Integer> villainNos = new ArrayList<Integer>();
			final ArrayList<Integer> mmirrorNos = new ArrayList<Integer>();
			final ArrayList<Integer> rmirrorNos = new ArrayList<Integer>();
			final Random rand = new Random();
			if (villainCount > 0) {
				for (int i = 0; i < 1 + villainCount / 5; i++) {
					villainNos.add(rand.nextInt(villainCount) + 1);
				}
			}
			if (mmirrorCount > 0) {
				for (int i = 0; i < 1 + mmirrorCount / 5; i++) {
					mmirrorNos.add(rand.nextInt(mmirrorCount) + 1);
				}
			}
			if (rmirrorCount > 0) {
				for (int i = 0; i < 1 + rmirrorCount / 5; i++) {
					rmirrorNos.add(rand.nextInt(rmirrorCount) + 1);
				}
			}
			for (int i = 0; i < MemStore.lvlFieldLEN.x; i++) {
				for (int j = 0; j < MemStore.lvlFieldLEN.y; j++) {
					final PlayImages topImage = Utils.getPlayImage(topId[i][j]);
					if (topImage.name().indexOf("Villain") == 0) {
						tempVillainCnt++;
						if (villainNos.contains(tempVillainCnt)) {
							topActor[i][j].startAnim();
						} else {
							topActor[i][j].stopAnim();
						}
					}
					if (topImage.name().indexOf("MMirror") == 0) {
						tempMmirrorCnt++;
						if (mmirrorNos.contains(tempMmirrorCnt)) {
							topActor[i][j].startAnim();
						} else {
							topActor[i][j].stopAnim();
						}
					}
					if (topImage.name().indexOf("RMirror") == 0) {
						tempRmirrorCnt++;
						if (rmirrorNos.contains(tempRmirrorCnt)) {
							topActor[i][j].startAnim();
						} else {
							topActor[i][j].stopAnim();
						}
					}
				}
			}
		}
		curDelayCount++;
	}

	public void paintPlatform(LTank lTank) {
		villainCount = 0;
		mmirrorCount = 0;
		rmirrorCount = 0;
		final PlayImages lvlBaseField[][] = lTank.getLvlBaseField();
		final PlayImages lvlPlayField[][] = lTank.getLvlPlayField();
		final Point curPos = new Point(pltfrmStrt.x, pltfrmStrt.y);
		for (int i = 0; i < MemStore.lvlFieldLEN.x; i++) {
			for (int j = 0; j < MemStore.lvlFieldLEN.y; j++) {
				final PlayImages baseImage = PlayImages.valueOf(lvlBaseField[j][i].name());
				final String topImageName = lvlPlayField[j][i].name();
				final PlayImages topImage = PlayImages.valueOf(topImageName);

				if (topImageName.indexOf("Villain") == 0) {
					villainCount++;
				}
				if (topImageName.indexOf("MMirror") == 0) {
					mmirrorCount++;
				}
				if (topImageName.indexOf("RMirror") == 0) {
					rmirrorCount++;
				}
				// if(topImageName.contains("Hero")){
				// Gdx.app.log("CFLAG", "test");
				// }
				pltfrm.removeActor(baseActor[i][j]);
				if (baseImage != PlayImages.Grass) {
					baseId[i][j] = baseImage.id;
					baseActor[i][j] = baseImage.getAnimActor();
					baseActor[i][j].setPosition(curPos.x, curPos.y);
					if (playImageOrigLen != PlayImageScaledLen) {
						baseActor[i][j].setSize(PlayImageScaledLen.x, PlayImageScaledLen.y);
					}
					pltfrm.addActor(baseActor[i][j]);
				}
				pltfrm.removeActor(topActor[i][j]);
				if (topImage != PlayImages.Grass) {
					topId[i][j] = topImage.id;
					topActor[i][j] = topImage.getAnimActor();
					topActor[i][j].setPosition(curPos.x, curPos.y);
					if (playImageOrigLen != PlayImageScaledLen) {
						topActor[i][j].setSize(PlayImageScaledLen.x, PlayImageScaledLen.y);
					}
					pltfrm.addActor(topActor[i][j]);
				}
				if (playImageOrigLen != PlayImageScaledLen) {
					curPos.x = curPos.x + PlayImageScaledLen.x;
				} else {
					curPos.x = curPos.x + playImageOrigLen.x;
				}
				if (topImage == PlayImages.Hero_U && firstRun) {
					topActor[i][j].setOrigin(topActor[i][j].getWidth() / 2, topActor[i][j].getHeight() / 2);
					float x = topActor[i][j].getX();
					float y = topActor[i][j].getY();

					final SequenceAction sequence = new SequenceAction();
					sequence.addAction(rotateBy(350 - ((x + topActor[i][j].getWidth()) / 5 * 10) % 360));
					for (int k = -(int) topActor[i][j].getWidth(); k < x + 5; k = k + 5) {
						sequence.addAction(parallel(moveTo(k, y), rotateBy(10)));
					}
					sequence.addAction(new Action() {
						@Override
						public boolean act(float delta) {
							heroReady = true;
							return false;
						}
					});
					topActor[i][j].addAction(sequence);
					firstRun = false;
				}
			}
			if (playImageOrigLen != PlayImageScaledLen) {
				curPos.x = 0;
				curPos.y = curPos.y + PlayImageScaledLen.y;
			} else {
				curPos.x = 0;
				curPos.y = curPos.y + playImageOrigLen.y;
			}
		}
	}

	public void paintThumbnailPlatform(Graphics g, PixelMap dstPixMap, PixelMap srcPixMap, LTank lTank) {
		final PlayImages lvlPlayField[][] = lTank.getLvlPlayField();
		final Point curPos = new Point(pltfrmStrt.x, pltfrmStrt.y);
		for (int i = 0; i < MemStore.lvlFieldLEN.x; i++) {
			for (int j = 0; j < MemStore.lvlFieldLEN.y; j++) {
				PlayImages topImage = PlayImages.valueOf(lvlPlayField[j][i].name());
				final TextureRegion tRegion = g.getTexRegion(topImage.name().toLowerCase());
				if (!topImage.name().equalsIgnoreCase("Ground")) {
					dstPixMap.drawPixmap(srcPixMap, new Point(tRegion.getRegionX(), tRegion.getRegionY()), playImageOrigLen, curPos, PlayImageScaledLen);
				}
				curPos.x = curPos.x + PlayImageScaledLen.x;
				topImage = null;
			}
			curPos.x = pltfrmStrt.x;
			curPos.y = curPos.y + PlayImageScaledLen.y;
		}
	}
}
