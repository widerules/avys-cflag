package com.avy.cflag.game.graphics;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Cloud extends Action {

	Actor cloud;
	int animIndex;

	public Cloud(final Actor cloud, final int animIndex) {
		this.cloud = cloud;
		this.animIndex = animIndex;
	}

	@Override
	public boolean act(final float delta) {

		final SequenceAction seqAction1 = new SequenceAction();
		final SequenceAction seqAction2 = new SequenceAction();
		switch (animIndex) {
			case 0:
				for (int i = 0; i <= 1023; i = i + 5) {
					seqAction2.addAction(moveTo(cloud.getX() - i, cloud.getY(), 0.1f));
				}
				seqAction2.addAction(moveTo(cloud.getX(), cloud.getY()));
				seqAction1.addAction(forever(seqAction2));
				break;
			case 1:
				seqAction1.addAction(moveTo(cloud.getX() - 1023, cloud.getY()));
				for (int i = 1023; i >= 0; i = i - 5) {
					seqAction2.addAction(moveTo(cloud.getX() - i, cloud.getY(), 0.1f));
				}
				seqAction2.addAction(moveTo(cloud.getX() - 1023, cloud.getY()));
				seqAction1.addAction(forever(seqAction2));
				break;
		}

		cloud.addAction(seqAction1);
		return true;

	}

}
