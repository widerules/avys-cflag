package com.avy.cflag.base;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class TouchListener extends InputListener {

	@Override
	public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
		Sounds.click.play();
		return true;
	}
}
