package com.avy.cflag.game.graphics;

import com.avy.cflag.base.Graphics;
import com.avy.cflag.base.ImageString;
import com.avy.cflag.base.ImageString.PrintFormat;
import com.avy.cflag.game.screens.PlayScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class HintMenu extends Group {
	private final Image menuArgb;
	private final Image menuBase;
	private final Image menuHeading;
	private Image hintNo;
	private ImageString hintStr;
	
	private Image leftArrow_Up;
	private Image leftArrow_Down;
	private Group leftArrow;
	
	private Image rightArrow_Up;
	private Image rightArrow_Down;
	private Group rightArrow;
	
	private String hints[];
	
	public HintMenu(final Graphics g, final PlayScreen pScreen, BitmapFont inFont, String[] inHints) {
		hints=inHints;

		menuArgb = new Image(g.getFlipTexRegion("argbblack"));
		menuArgb.setPosition(0, 0);
		menuArgb.setSize(pScreen.game.getSrcWidth(), pScreen.game.getSrcHeight());
		menuArgb.getColor().a = 0.5f;

		menuBase = new Image(g.getFlipTexRegion("hintpanel"));
		menuBase.setPosition((pScreen.game.getSrcWidth() - menuBase.getWidth()) / 2, (pScreen.game.getSrcHeight() - menuBase.getHeight()) / 2);

		menuHeading = new Image(g.getFlipTexRegion("gamehintstr"));
		menuHeading.setPosition(menuBase.getX() + ((menuBase.getWidth() - menuHeading.getWidth()) / 2), menuBase.getY() + 18);

		hintStr = new ImageString(hints[0], inFont, Color.GREEN, PrintFormat.Wrapped);
		hintStr.setWidth(400);
		hintStr.setPosition(menuBase.getX() + (menuBase.getWidth()-hintStr.getWidth())/2,menuHeading.getY()+menuHeading.getHeight()+10);
		
		hintNo = new Image(g.getFlipTexRegion("hintno0"));
		hintNo.setPosition((pScreen.game.getSrcWidth() - menuBase.getWidth()) / 2, (pScreen.game.getSrcHeight() - menuBase.getHeight()) / 2);
		
		
		addActor(menuArgb);
		addActor(menuBase);
		addActor(hintStr);
		addActor(menuHeading);

	}
}
