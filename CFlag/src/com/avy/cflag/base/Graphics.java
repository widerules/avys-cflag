package com.avy.cflag.base;

import com.avy.cflag.game.CFlagGame;
import com.avy.cflag.game.Constants;
import com.avy.cflag.game.MemStore.Difficulty;
import com.avy.cflag.game.utils.SaveThumbs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class Graphics {
	private TextureAtlas imageAtlas;
	private SpriteBatch batch;
	private ShapeRenderer sr;
	private BitmapFont font;

	public Graphics() {
		imageAtlas = null;
		batch = null;
		sr = null;
		font = null;
	}

	public TextureAtlas createImageAtlas(String texturePackName) {
		return new TextureAtlas(Gdx.files.internal(Constants.ATLAS_FLDR + "/" + texturePackName + ".pack"));
	}

	@SuppressWarnings("deprecation")
	public BitmapFont createFont(String fontName, int fontSize, boolean flipped) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + fontName + ".ttf"));
		BitmapFont fnt = generator.generateFont(fontSize, FreeTypeFontGenerator.DEFAULT_CHARS, flipped ? true : false);
		if (!flipped)
			fnt.setScale(1, -1);
		generator.dispose();
		return fnt;
	}

	public void setImageAtlas(TextureAtlas textureAtlas) {
		imageAtlas = textureAtlas;
	}

	public void setFont(BitmapFont fnt) {
		font = fnt;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public void setSr(ShapeRenderer sr) {
		this.sr = sr;
	}

	public TextureRegion getFlipTexRegion(String regionName) {
		TextureRegion textureRegion = new ImageRegion(imageAtlas.findRegion(regionName));
		return textureRegion;
	}

	public TextureRegion getTexRegion(String regionName) {
		TextureRegion textureRegion = new TextureRegion(imageAtlas.findRegion(regionName));
		return textureRegion;
	}

	public Array<ImageRegion> getFlipTexRegions(String regionName) {
		Array<AtlasRegion> atlasArr = imageAtlas.findRegions(regionName);
		Array<ImageRegion> imgArr = new Array<ImageRegion>();
		for (AtlasRegion atlasRegion : atlasArr) {
			imgArr.add(new ImageRegion(atlasRegion));
		}
		return imgArr;
	}

	public TextureRegion getThumbTexRegion(Difficulty dclty, int levelNo) {
		FileHandle fh = Gdx.files.external("Android/data/" + CFlagGame.packageName + "/thumbs/" + dclty.name() + levelNo + ".png");
		if (!fh.exists()) {
			SaveThumbs st = new SaveThumbs(dclty, levelNo);
			st.createThumb();
		}
		final TextureRegion textureRegion = new TextureRegion(new Texture(PixmapIO.readCIM(fh)), 100, 100);
		textureRegion.flip(false, true);
		return textureRegion;
	}

	public void drawString(String txt, int x, int y, Color color) {
		font.setColor(color);
		final TextBounds tb = font.getBounds(txt);
		font.draw(batch, txt, x - tb.width / 2, y - tb.height / 2);
	}

	public void drawStringWrapped(String txt, int x, int y, Color color) {
		font.setColor(color);
		final TextBounds tb = font.getBounds(txt);
		final float h = tb.height * (int) (tb.width / 100);
		font.drawWrapped(batch, txt, x - 50, y - h / 2, 100, HAlignment.CENTER);
	}

	public void drawStringWrapped(BitmapFont font, String txt, int x, int y, Color color) {
		font.setColor(color);
		final TextBounds tb = font.getBounds(txt);
		final float h = tb.height * (int) (tb.width / 100);
		font.drawWrapped(batch, txt, x - 50, y - h / 2, 100, HAlignment.CENTER);
	}

	public void drawStringWrapped(BitmapFont font, String txt, float x, float y, float width, Color color) {
		font.setColor(color);
		font.drawWrapped(batch, txt, x, y, width, HAlignment.CENTER);
	}

	public void drawMultiLineString(String txt, float x, float y, Color color) {
		final String lines[] = txt.split("\n");
		final TextBounds tb = new TextBounds();
		tb.width = 0;
		tb.height = 0;
		for (final String lineStr : lines) {
			final TextBounds tmp = font.getBounds(lineStr);
			if (tmp.width > tb.width) {
				tb.width = tmp.width;
			}
			tb.height = tb.height + tmp.height;
		}
		font.setColor(color);
		font.drawMultiLine(batch, txt, x - tb.width / 2, y - tb.height / 2);
	}

	public CheckBoxStyle getCheckBoxStyle(String fontName, int fontSize) {
		final CheckBoxStyle cs = new CheckBoxStyle();
		cs.checkboxOff = new TextureRegionDrawable(getFlipTexRegion("checkboxoff"));
		cs.checkboxOn = new TextureRegionDrawable(getFlipTexRegion("checkboxon"));
		cs.font = createFont(fontName, fontSize, false);
		cs.fontColor = Color.RED;
		return cs;
	}

	public SliderStyle getSliderStyle() {
		final SliderStyle ss = new SliderStyle();
		ss.background = new TextureRegionDrawable(getFlipTexRegion("sliderbase"));
		ss.knob = new TextureRegionDrawable(getFlipTexRegion("sliderknob"));
		return ss;
	}

	public TextFieldStyle getTextBoxStyle(String fontName, int fontSize) {
		final TextFieldStyle ts = new TextFieldStyle();
		ts.background = new TextureRegionDrawable(getFlipTexRegion("textfieldbase"));
		ts.background.setLeftWidth(10);
		ts.background.setRightWidth(10);
		ts.cursor = new TextureRegionDrawable(getFlipTexRegion("cursor"));
		ts.selection = new TextureRegionDrawable(getFlipTexRegion("cursor"));
		ts.font = createFont(fontName, fontSize, false);
		ts.fontColor = Color.WHITE;
		return ts;
	}

	public LabelStyle getLabelStyle(String fontName, int fontSize) {
		final LabelStyle ls = new LabelStyle();
		ls.font = createFont(fontName, fontSize, false);
		ls.fontColor = Color.WHITE;
		return ls;
	}

	public void drawRectWithBorder(Rect rect, Color color) {
		sr.setColor(Color.BLACK);
		sr.rect(rect.left - 1, rect.top - 1, rect.right - rect.left + 1, rect.bottom - rect.top + 1);
		sr.setColor(color);
		sr.rect(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
	}

}
