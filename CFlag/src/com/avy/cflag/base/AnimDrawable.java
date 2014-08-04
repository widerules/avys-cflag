package com.avy.cflag.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class AnimDrawable extends BaseDrawable {
	public final Animation anim;
	private float stateTime = 0;
	private boolean animOn;
	private boolean isAnimating;

	public AnimDrawable(Animation anim) {
		this.anim = anim;
		animOn = true;
		isAnimating = true;
		setMinWidth(anim.getKeyFrame(0).getRegionWidth());
		setMinHeight(anim.getKeyFrame(0).getRegionHeight());
	}

	public void act(float delta) {
		if (isAnimating && anim.getKeyFrameIndex(stateTime) == 0) {
			isAnimating = false;
			stateTime = 0;
		}
		if (anim.getKeyFrameIndex(stateTime) > 0) {
			isAnimating = true;
		}
		if (animOn) {
			stateTime += delta;
		} else {
			if (stateTime != 0) {
				stateTime += delta;
			}
		}
	}

	public void reset() {
		stateTime = 0;
		Gdx.app.log("CFLAG", "reset");
	}

	public void stopAnim() {
		animOn = false;
	}

	public void startAnim() {
		animOn = true;
		stateTime = 0;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height) {
		batch.draw(anim.getKeyFrame(stateTime), x, y, width, height);
	}

	public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
		batch.draw(anim.getKeyFrame(stateTime), x, y, originX, originY, width, height, scaleX, scaleY, rotation);
	}
}