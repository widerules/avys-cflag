package com.avy.cflag.game.graphics;

import com.avy.cflag.base.Graphics;
import com.avy.cflag.game.screens.PlayScreen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class HintMenu extends Group {
	private Image menuArgb;
	private Image menuBase;
	private Image menuHeading;

	public HintMenu(Graphics g, final PlayScreen pScreen, String menuName) {

		menuArgb = new Image(g.getFlipTexRegion("argbblack"));
		menuArgb.setPosition(0, 0);
		menuArgb.setSize(pScreen.game.getSrcWidth(), pScreen.game.getSrcHeight());
		menuArgb.getColor().a = 0.5f;

		menuBase = new Image(g.getFlipTexRegion("hintpanel"));
		menuBase.setPosition((pScreen.game.getSrcWidth() - menuBase.getWidth()) / 2, (pScreen.game.getSrcHeight() - menuBase.getHeight()) / 2);
		menuBase.setName("base");

		menuHeading = new Image(g.getFlipTexRegion(menuName + "str"));
		menuHeading.setPosition(menuBase.getX() + (menuBase.getWidth() - menuHeading.getWidth()) / 2, menuBase.getY() + 20);
		menuHeading.setName("heading");

		addActor(menuArgb);
		addActor(menuBase);
		addActor(menuHeading);

	}
}
