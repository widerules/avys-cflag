package com.avy.cflag.game.graphics;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.visible;

import com.avy.cflag.base.Graphics;
import com.avy.cflag.base.ImageString;
import com.avy.cflag.base.ImageString.PrintFormat;
import com.avy.cflag.base.TouchListener;
import com.avy.cflag.game.screens.PlayScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class HintMenu extends Group {
	private final Image menuArgb;
	private final Image menuBase;
	private final Image menuHeading;
	private String hints[];
	private Group hintsGroup[];
	
	private Image leftArrow_Up;
	private Image leftArrow_Down;
	private Group leftArrow;
	
	private Image rightArrow_Up;
	private Image rightArrow_Down;
	private Group rightArrow;
	
	private Image solnButton_Up;
	private Image solnButton_Down;
	
	private int curHint=0;
	private int totHints;
	
	public HintMenu(final Graphics g, final PlayScreen pScreen, BitmapFont inFont, String[] inHints, int inCurHint) {
		hints=inHints;
		curHint = inCurHint>0?inCurHint-1:0;
		totHints = inHints.length+1;
		hintsGroup = new Group[totHints];
		
		menuArgb = new Image(g.getFlipTexRegion("argbblack"));
		menuArgb.setPosition(0, 0);
		menuArgb.setSize(pScreen.game.getSrcWidth(), pScreen.game.getSrcHeight());
		menuArgb.getColor().a = 0.5f;

		menuBase = new Image(g.getFlipTexRegion("hintpanel"));
		menuBase.setPosition((pScreen.game.getSrcWidth() - menuBase.getWidth()) / 2, (pScreen.game.getSrcHeight() - menuBase.getHeight()) / 2);

		menuHeading = new Image(g.getFlipTexRegion("gamehintstr"));
		menuHeading.setPosition(menuBase.getX() + ((menuBase.getWidth() - menuHeading.getWidth()) / 2), menuBase.getY()+5);

		addActor(menuArgb);
		addActor(menuBase);
		
		for (int i = 0; i < totHints; i++) {
			hintsGroup[i] = new Group();
			
			if(i<totHints-1) {
				ImageString hintStr = new ImageString(hints[i], inFont, Color.GREEN, PrintFormat.Wrapped);
				hintStr.setBounds(menuBase.getX()+15,menuBase.getY()+50, menuBase.getWidth()-30, 40);
				hintsGroup[i].addActor(hintStr);
			} else {
				solnButton_Up = new Image(g.getFlipTexRegion("2_rectbuttonup"));
				solnButton_Down = new Image(g.getFlipTexRegion("2_rectbuttondown"));
				solnButton_Down.setVisible(false);
				Image solnButtonStr = new Image(g.getFlipTexRegion("solutionstr"));
				
				Group solnButton = new Group();
				solnButton.addActor(solnButton_Up);
				solnButton.addActor(solnButton_Down);
				solnButton.addActor(solnButtonStr);
				solnButton.setPosition(getX() + (getWidth()-solnButton_Up.getWidth())/2, getY() + (getHeight()-solnButton_Up.getHeight())/2);
				
				solnButton.addListener(new TouchListener() {
					@Override
					public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
						solnButton_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
						return super.touchDown(event, x, y, pointer, button);
					};

					@Override
					public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
						solnButton_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
					};
				});
				hintsGroup[i].addActor(solnButton);
			}

			Image hintNo = new Image(g.getFlipTexRegion("hintno" + i));
			hintNo.setPosition((pScreen.game.getSrcWidth() - hintNo.getWidth()) / 2, menuBase.getY() + 100);
			hintsGroup[i].addActor(hintNo);
			
			if(i==curHint) {
				hintsGroup[i].setVisible(true);
			} else {
				hintsGroup[i].setVisible(false);
			}
			addActor(hintsGroup[i]);
		}
		
		leftArrow_Down = new Image(g.getFlipTexRegion("leftarrowdown"));
		leftArrow_Down.setPosition(menuBase.getX() + 25,menuBase.getY() + 102);
		leftArrow_Down.setVisible(false);
		
		leftArrow = new Group();
		leftArrow_Up = new Image(g.getFlipTexRegion("leftarrowup"));
		leftArrow_Up.setPosition(leftArrow_Down.getX(), leftArrow_Down.getY());

		leftArrow.addActor(leftArrow_Up);
		leftArrow.addActor(leftArrow_Down);
		
		rightArrow_Down = new Image(g.getFlipTexRegion("rightarrowdown"));
		rightArrow_Down.setPosition(menuBase.getX() + 377,menuBase.getY() + 102);
		rightArrow_Down.setVisible(false);
		
		rightArrow = new Group();
		rightArrow_Up = new Image(g.getFlipTexRegion("rightarrowup"));
		rightArrow_Up.setPosition(rightArrow_Down.getX(), rightArrow_Down.getY());
		
		rightArrow.addActor(rightArrow_Up);
		rightArrow.addActor(rightArrow_Down);

		if(curHint==0)
			leftArrow.setVisible(false);
		else if(curHint==totHints-1)
			rightArrow.setVisible(false);
		
		addActor(leftArrow);
		addActor(rightArrow);
		
		addActor(menuHeading);

		leftArrow_Up.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				leftArrow_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return super.touchDown(event, x, y, pointer, button);
			};

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				leftArrow_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				if(curHint>0) {
					hintsGroup[curHint].setVisible(false);
					curHint--;
					hintsGroup[curHint].setVisible(true);
					rightArrow.setVisible(true);
					if(curHint==0)
						leftArrow.setVisible(false);
				}
			};
		});

		rightArrow_Up.addListener(new TouchListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				rightArrow_Down.addAction(sequence(alpha(0), visible(true), fadeIn(0.2f)));
				return super.touchDown(event, x, y, pointer, button);
			};

			@Override
			public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				rightArrow_Down.addAction(sequence(fadeIn(0.1f), visible(false)));
				if(curHint<totHints-1) {
					hintsGroup[curHint].setVisible(false);
					curHint++;
					pScreen.incrementHint(curHint+1);
					hintsGroup[curHint].setVisible(true);
					leftArrow.setVisible(true);
					if(curHint==totHints-1)
						rightArrow.setVisible(false);
				}
			};
		});
	}
	
	@Override
	public float getX(){
		return menuBase.getX();
	}

	@Override
	public float getY(){
		return menuBase.getY();
	}

	@Override
	public float getHeight() {
		return menuBase.getHeight();
	}
	
	@Override
	public float getWidth() {
		return menuBase.getWidth();
	}
	
}
