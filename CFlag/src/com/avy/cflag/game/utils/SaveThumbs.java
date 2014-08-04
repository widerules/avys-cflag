package com.avy.cflag.game.utils;

import com.avy.cflag.base.Graphics;
import com.avy.cflag.base.PixelMap;
import com.avy.cflag.base.Point;
import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.elements.LTank;
import com.avy.cflag.game.elements.Level;
import com.avy.cflag.game.elements.Platform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SaveThumbs implements Runnable {
	Graphics g;
	int levelNo;
	Thread saveThread = null;

	public SaveThumbs(Graphics g, int levelNo) {
		this.g = g;
		this.levelNo = levelNo;
	}

	public void start() {
		saveThread = new Thread(this);
		saveThread.start();
	}

	@Override
	public void run() {
		final TextureRegion grass = g.getTexRegion("grass");
		final TextureRegion border = g.getTexRegion("border");
		final PixelMap srcPixMap = new PixelMap(Gdx.files.internal("atlas/play.png"));
		final PixelMap dstPixMap = new PixelMap(128, 128, srcPixMap.getFormat());
		dstPixMap.drawPixmap(srcPixMap, new Point(border.getRegionX(), border.getRegionY()), new Point(border.getRegionWidth(), border.getRegionHeight()), new Point(0, 0), new Point(100, 100));
		dstPixMap.drawPixmap(srcPixMap, new Point(grass.getRegionX(), grass.getRegionY()), new Point(grass.getRegionWidth(), grass.getRegionHeight()), new Point(2, 2), new Point(96, 96));
		final Level lvl = new Level();
		lvl.loadLevel(levelNo);
		final LTank lTank = new LTank(lvl);
		final Platform pf = new Platform(new Point(2, 2), new Point(96, 96));
		pf.paintThumbnailPlatform(g, dstPixMap, srcPixMap, lTank);
		PixmapIO.writeCIM(Gdx.files.external("Android/data/" + CFlagGame.packageName + "/thumbs/level" + levelNo + ".png"), dstPixMap);
		dstPixMap.dispose();
		srcPixMap.dispose();
	}

	public void saveMe() {
		for (int i = 1; i < 65; i++) {
			final TextureRegion grass = g.getTexRegion("grass");
			final TextureRegion border = g.getTexRegion("border");
			final PixelMap srcPixMap = new PixelMap(Gdx.files.internal("atlas/play.png"));
			final PixelMap dstPixMap = new PixelMap(128, 128, srcPixMap.getFormat());
			dstPixMap.drawPixmap(srcPixMap, new Point(border.getRegionX(), border.getRegionY()), new Point(border.getRegionWidth(), border.getRegionHeight()), new Point(0, 0), new Point(100, 100));
			dstPixMap.drawPixmap(srcPixMap, new Point(grass.getRegionX(), grass.getRegionY()), new Point(grass.getRegionWidth(), grass.getRegionHeight()), new Point(2, 2), new Point(96, 96));
			final Level lvl = new Level();
			lvl.loadLevel(i);
			final LTank lTank = new LTank(lvl);
			final Platform pf = new Platform(new Point(2, 2), new Point(96, 96));
			pf.paintThumbnailPlatform(g, dstPixMap, srcPixMap, lTank);
			PixmapIO.writeCIM(Gdx.files.external("Android/data/" + CFlagGame.packageName + "/thumbs/level" + i + ".png"), dstPixMap);
			dstPixMap.dispose();
			srcPixMap.dispose();
		}
	}
}
