package com.avy.cflag.base;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class AnimDrawable extends BaseDrawable {
	public final Animation anim;
	private float stateTime = 0;
	private boolean animOn;
	private boolean isAnimating;

	public AnimDrawable(final Animation anim) {
		this.anim = anim;
		animOn = true;
		isAnimating = true;
		setMinWidth(anim.getKeyFrame(0).getRegionWidth());
		setMinHeight(anim.getKeyFrame(0).getRegionHeight());
	}

	public void act(final float delta) {
		if (isAnimating && (anim.getKeyFrameIndex(stateTime) == 0)) {
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
	}

	public void stopAnim() {
		animOn = false;
	}

	public void startAnim() {
		animOn = true;
		stateTime = 0;
	}

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(final float stateTime) {
		this.stateTime = stateTime;
	}

	@Override
	public void draw(final Batch batch, final float x, final float y, final float width, final float height) {
		batch.draw(anim.getKeyFrame(stateTime), x, y, width, height);
	}

	public void draw(final Batch batch, final float x, final float y, final float originX, final float originY, final float width, final float height, final float scaleX, final float scaleY,
			final float rotation) {
		batch.draw(anim.getKeyFrame(stateTime), x, y, originX, originY, width, height, scaleX, scaleY, rotation);
	}

	public void cloneProperties(final AnimDrawable inDrawable) {
		animOn = inDrawable.animOn;
		isAnimating = inDrawable.isAnimating;
		stateTime = inDrawable.stateTime;
	}

}