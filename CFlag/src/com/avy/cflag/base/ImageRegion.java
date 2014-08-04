package com.avy.cflag.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageRegion extends TextureRegion {

	public ImageRegion() {
		super();
		flip(false, true);
	}

	public ImageRegion(Texture texture) {
		super(texture);
		flip(false, true);
	}

	public ImageRegion(TextureRegion textureRegion) {
		super(textureRegion);
		flip(false, true);
	}

	public ImageRegion(TextureRegion textureRegion, int x, int y, int width, int height) {
		super(textureRegion, x, y, width, height);
		flip(false, true);
	}

	public ImageRegion(Texture texture, int x, int y, int width, int height) {
		super(texture, x, y, width, height);
		flip(false, true);
	}
}
