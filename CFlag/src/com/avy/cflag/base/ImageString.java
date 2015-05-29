package com.avy.cflag.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class ImageString extends Actor{
	public static enum PrintFormat {
		Normal_Left, Normal_Center, Wrapped_Left, Wrapped_Center, MultiLine
	}
	
	private String printStr;
	private BitmapFont printFont;
	private PrintFormat printFormat;
	private GlyphLayout layout;
	
	public ImageString(String inStr, BitmapFont inFont, Color inColor) {
		printStr=inStr;
		printFont=inFont;
		printFormat = PrintFormat.Normal_Center;
		setColor(inColor);
		layout = new GlyphLayout();
	}
	
	public ImageString(String inStr, BitmapFont inFont, Color inColor, PrintFormat inFormat) {
		printStr=inStr;
		printFont=inFont;
		printFormat = inFormat;
		setColor(inColor);
		layout = new GlyphLayout();
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
		switch (printFormat) {
			case Normal_Left:
				layout.setText(printFont, printStr);
				printFont.draw(batch, layout, x, y);
				break;
			case Normal_Center:
				layout.setText(printFont, printStr,color,width,Align.center,false);
				printFont.draw(batch, layout, x, y);
				break;
			case Wrapped_Left:
				layout.setText(printFont, printStr,color,width,Align.center,false);
				printFont.draw(batch, layout, x, y);
				break;
			case Wrapped_Center:
				y = y+(height-tb.height)/2;
				printFont.drawWrapped(batch, printStr, x, y, width, Align.center);
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
		layout.setText(printFont, printStr);
		this.printStr = printStr;
	}
}
