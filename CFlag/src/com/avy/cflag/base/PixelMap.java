package com.avy.cflag.base;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

public class PixelMap extends Pixmap {

	public PixelMap(FileHandle file) {
		super(file);
	}

	public PixelMap(int width, int height, Format format) {
		super(width, height, format);
	}

	public void drawPixmap(Pixmap pixmap, Point srcPos, Point srcLen, Point dstPos, Point dstLen) {
		super.drawPixmap(pixmap, srcPos.x, srcPos.y, srcLen.x, srcLen.y, dstPos.x, dstPos.y, dstLen.x, dstLen.y);
	}

	public void drawPixmap(Pixmap pixmap, Point srcPos) {
		super.drawPixmap(pixmap, srcPos.x, srcPos.y);
	}

	public void drawPixmap(Pixmap pixmap, Point dstPos, Point srcPos, Point srcLen) {
		super.drawPixmap(pixmap, dstPos.x, dstPos.y, srcPos.x, srcPos.y, srcLen.x, srcLen.y);
	}
}
