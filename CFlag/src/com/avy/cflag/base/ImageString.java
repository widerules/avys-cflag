package com.avy.cflag.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ImageString extends Actor{
	public static enum PrintFormat {
		Normal, Wrapped, MultiLine
	}
	
	private String printStr;
	private BitmapFont printFont;
	private PrintFormat printFormat;
	
	public ImageString(String inStr, BitmapFont inFont, Color inColor) {
		printStr=inStr;
		printFont=inFont;
		printFormat = PrintFormat.Normal;
		setColor(inColor);
	}
	
	public ImageString(String inStr, BitmapFont inFont, Color inColor, PrintFormat inFormat) {
		printStr=inStr;
		printFont=inFont;
		printFormat = inFormat;
		setColor(inColor);
	}

	public void setBounds(float x, float y, float width, float height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color color = getColor();
		printFont.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();
		final TextBounds tb = printFont.getWrappedBounds(printStr, width);
		switch (printFormat) {
			case Normal:
				printFont.draw(batch, printStr, x - (tb.width / 2), y - (tb.height / 2));
				break;
			case Wrapped:
				y = y+(height-tb.height)/2;
				printFont.drawWrapped(batch, printStr, x, y, width, HAlignment.CENTER);
				break;
			case MultiLine:
				final String lines[] = printStr.split("\n");
				tb.width = 0;
				tb.height = 0;
				for (final String lineStr : lines) {
					final TextBounds tmp = printFont.getBounds(lineStr);
					if (tmp.width > tb.width) {
						tb.width = tmp.width;
					}
					tb.height = tb.height + tmp.height;
				}
				printFont.drawMultiLine(batch, printStr, x - (tb.width / 2), y - (tb.height / 2));
				break;
			default:
				break;
		}
	}
	
	public BitmapFont getPrintFont() {
		return printFont;
	}

	public void setPrintFont(BitmapFont printFont) {
		this.printFont = printFont;
	}

	public PrintFormat getPrintFormat() {
		return printFormat;
	}

	public void setPrintFormat(PrintFormat printFormat) {
		this.printFormat = printFormat;
	}

	public String getPrintStr() {
		return printStr;
	}

	public void setPrintStr(String printStr) {
		this.printStr = printStr;
	}
}
