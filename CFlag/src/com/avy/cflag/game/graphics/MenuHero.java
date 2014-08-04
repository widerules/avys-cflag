package com.avy.cflag.game.graphics;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class MenuHero  extends Action{
	
	Actor menuHero;
	
	public MenuHero(Actor menuHero) {
		this.menuHero =menuHero;
	}
	
	@Override
	public boolean act(float delta) {
		final SequenceAction sequence = new SequenceAction();
		for (int i = 0; i < 800 - 45; i = i + 5) {
			sequence.addAction(parallel(moveTo(i, menuHero.getY()), rotateBy(10)));
		}
		for (int i = 800 - 45; i >= 0; i = i - 5) {
			sequence.addAction(parallel(moveTo(i, menuHero.getY()), rotateBy(-10)));
		}
		menuHero.addAction(sequence(fadeIn(1f), forever(sequence)));
		return true;
	}

}
