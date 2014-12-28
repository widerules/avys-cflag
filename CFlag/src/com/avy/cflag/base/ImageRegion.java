package com.avy.cflag.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageRegion extends TextureRegion {

	public ImageRegion() {
		super();
		flip(false, true);
	}

	public ImageRegion(final Texture texture) {
		super(texture);
		flip(false, true);
	}

	public ImageRegion(final TextureRegion textureRegion) {
		super(textureRegion);
		flip(false, true);
	}

	public ImageRegion(final TextureRegion textureRegion, final int x, final int y, final int width, final int height) {
		super(textureRegion, x, y, width, height);
		flip(false, true);
	}

	public ImageRegion(final Texture texture, final int x, final int y, final int width, final int height) {
		super(texture, x, y, width, height);
		flip(false, true);
	}
}
