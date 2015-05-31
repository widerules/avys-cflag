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
		printFont.getData().markupEnabled=true;
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
		
		Color actorColor = getColor();
		Color fontColor = new Color(actorColor.r, actorColor.g, actorColor.b, actorColor.a * parentAlpha);

		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();
		switch (printFormat) {
			case Normal_Left:
				layout.setText(printFont, printStr, fontColor,width,Align.left,false);
				printFont.draw(batch, layout, x, y);
				break;
			case Normal_Center:
				layout.setText(printFont, printStr,fontColor,width,Align.center,false);
				printFont.draw(batch, layout, x, y);
				break;
			case Wrapped_Left:
				layout.setText(printFont, printStr,fontColor,width,Align.left,true);
				printFont.draw(batch, layout, x, y+(height-layout.height)/2);
				break;
			case Wrapped_Center:
				layout.setText(printFont, printStr,fontColor,width,Align.center,true);
				printFont.draw(batch, layout, x, y+(height-layout.height)/2);
				break;
			case MultiLine:
				layout.setText(printFont, printStr,fontColor,width,Align.center,true);
				printFont.draw(batch, layout, x, y+(height-layout.height)/2);
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
