package com.avy.cflag.game.graphics;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import java.lang.reflect.Method;

import com.avy.cflag.base.Graphics;
import com.avy.cflag.base.TouchListener;
import com.avy.cflag.game.screens.PlayScreen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ShortMenu extends Group {
	private Image menuArgb;
	private Image menuBase;
	private Image menuHeading;

	private Group buttonGroup[];
	private Image buttonUp[];
	private Image buttonDown[];
	private Image buttonStr[];

	public ShortMenu(final Graphics g, final PlayScreen pScreen, final String menuName, final String... buttonList) {

		menuArgb = new Image(g.getFlipYTexRegion("argbblack"));
		menuArgb.setPosition(0, 0);
		menuArgb.setSize(pScreen.game.getSrcWidth(), pScreen.game.getSrcHeight());
		menuArgb.getColor().a = 0.5f;

		menuBase = new Image(g.getFlipYTexRegion("menupanel"));
		menuBase.setPosition((pScreen.game.getSrcWidth() - menuBase.getWidth()) / 2, (pScreen.game.getSrcHeight() - menuBase.getHeight()) / 2);

		menuHeading = new Image(g.getFlipYTexRegion(menuName + "str"));
		menuHeading.setPosition(menuBase.getX() + ((menuBase.getWidth() - menuHeading.getWidth()) / 2), menuBase.getY() + 20);

		addActor(menuArgb);
		addActor(menuBase);
		addActor(menuHeading);

		final float totalBaseHeight = (menuBase.getY() + menuBase.getHeight()) - 20 - (menuHeading.getY() + menuHeading.getHeight());
		final float totalButtonHeight = menuHeading.getHeight() * buttonList.length;
		final float spaceY = menuHeading.getHeight() + ((totalBaseHeight - totalButtonHeight) / (buttonList.length - 1));
		final float x = menuBase.getX() + ((menuBase.getWidth() - menuHeading.getWidth()) / 2);
		final float y = menuHeading.getY() + menuHeading.getHeight();

		buttonGroup = new Group[buttonList.length];
		buttonUp = new Image[buttonList.length];
		buttonDown = new Image[buttonList.length];
		buttonStr = new Image[buttonList.length];

		for (int i = 0; i < buttonList.length; i++) {
			buttonUp[i] = new Image(g.getFlipYTexRegion(Integer.toString(i) + "_rectbuttonup"));
			buttonDown[i] = new Image(g.getFlipYTexRegion(Integer.toString(i) + "_rectbuttondown"));
			buttonDown[i].setVisible(false);
			buttonStr[i] = new Image(g.getFlipYTexRegion(buttonList[i] + "str"));
			buttonStr[i].setName(buttonList[i]);

			buttonGroup[i] = new Group();
			buttonGroup[i].addActor(buttonUp[i]);
			buttonGroup[i].addActor(buttonDown[i]);
			buttonGroup[i].addActor(buttonStr[i]);
			buttonGroup[i].setName(Integer.toString(i));
			buttonGroup[i].setPosition(x, y + (spaceY * i));
			buttonGroup[i].addListener(new TouchListener() {
				@Override
				public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
					final int idx = Integer.parseInt(event.getListenerActor().getName());
					buttonDown[idx].addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
					return super.touchDown(event, x, y, pointer, button);
				}

				@Override
				public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
					final int idx = Integer.parseInt(event.getListenerActor().getName());
					buttonDown[idx].addAction(sequence(fadeIn(0.1f), visible(false)));
					final String btnName = buttonStr[idx].getName();
					try {
						final Method method = PlayScreen.class.getDeclaredMethod(btnName);
						method.invoke(pScreen);
					} catch (final Exception e) {
					}
				}
			});
			addActor(buttonGroup[i]);
		}
	}
	
	public Image getMenuBase() {
		return menuBase;
	}
}
