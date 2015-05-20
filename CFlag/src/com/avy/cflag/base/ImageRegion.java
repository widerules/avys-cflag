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
	
	public ImageRegion(final TextureRegion textureRegion, boolean flipX, boolean flipY) {
		super(textureRegion);
		flip(flipX, flipY);
	}

}
