package com.avy.cflag.base;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

public class PixelMap extends Pixmap {

	public PixelMap(final FileHandle file) {
		super(file);
	}

	public PixelMap(final int width, final int height, final Format format) {
		super(width, height, format);
	}

	public void drawPixmap(final Pixmap pixmap, final Point srcPos, final Point srcLen, final Point dstPos, final Point dstLen) {
		super.drawPixmap(pixmap, srcPos.x, srcPos.y, srcLen.x, srcLen.y, dstPos.x, dstPos.y, dstLen.x, dstLen.y);
	}

	public void drawPixmap(final Pixmap pixmap, final Point srcPos) {
		super.drawPixmap(pixmap, srcPos.x, srcPos.y);
	}

	public void drawPixmap(final Pixmap pixmap, final Point dstPos, final Point srcPos, final Point srcLen) {
		super.drawPixmap(pixmap, dstPos.x, dstPos.y, srcPos.x, srcPos.y, srcLen.x, srcLen.y);
	}
}
