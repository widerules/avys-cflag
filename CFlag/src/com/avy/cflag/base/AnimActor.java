package com.avy.cflag.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class AnimActor extends Image {
	private final AnimDrawable drawable;

	public AnimActor(final AnimDrawable drawable) {
		super(drawable);
		this.drawable = drawable;
	}

	@Override
	public void act(final float delta) {
		drawable.act(delta);
		super.act(delta);
	}

	@Override
	public void draw(final Batch batch, final float parentAlpha) {
		validate();

		final Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		final float x = getX();
		final float y = getY();
		final float scaleX = getScaleX();
		final float scaleY = getScaleY();

		final float rotation = getRotation();
		if ((scaleX == 1) && (scaleY == 1) && (rotation == 0)) {
			drawable.draw(batch, x + getImageX(), y + getImageY(), getImageWidth(), getImageHeight());
		} else {
			drawable.draw(batch, x + getImageX(), y + getImageY(), getOriginX() - getImageX(), getOriginY() - getImageY(), getImageWidth(), getImageHeight(), scaleX, scaleY, rotation);
		}
	}

	@Override
	public AnimDrawable getDrawable() {
		return drawable;
	}

	public void stopAnim() {
		drawable.stopAnim();
	}

	public void startAnim() {
		drawable.startAnim();
	}
}