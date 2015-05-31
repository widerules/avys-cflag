package com.avy.cflag.base;

import com.avy.cflag.game.Constants;
import com.avy.cflag.game.EnumStore.Difficulty;
import com.avy.cflag.game.utils.SaveThumbs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	private ShapeRenderer sr;

	public Graphics() {
		imageAtlas = null;
		sr = null;
	}

	public TextureAtlas createImageAtlas(final String texturePackName) {
		return new TextureAtlas(Gdx.files.internal(Constants.ATLAS_FLDR + "/" + texturePackName + ".pack"));
	}

	@SuppressWarnings("deprecation")
	public BitmapFont createFont(final String fontName, final int fontSize, final boolean flipped) {
		final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + fontName + ".ttf"));
		final BitmapFont fnt = generator.generateFont(fontSize, FreeTypeFontGenerator.DEFAULT_CHARS, flipped);
		if (!flipped) {
			fnt.getData().setScale(1, -1);
		}
		generator.dispose();
		return fnt;
	}

	public void setImageAtlas(final TextureAtlas textureAtlas) {
		imageAtlas = textureAtlas;
	}

	public TextureAtlas getImageAtlas() {
		return imageAtlas;
	}

	public void setSr(final ShapeRenderer sr) {
		this.sr = sr;
	}

	public TextureRegion getTexRegion(final String regionName) {
		final TextureRegion textureRegion = new ImageRegion(imageAtlas.findRegion(regionName), false, false);
		return textureRegion;
	}

	public Array<ImageRegion> getFlipYTexRegions(final String regionName) {
		final Array<AtlasRegion> atlasArr = imageAtlas.findRegions(regionName);
		final Array<ImageRegion> imgArr = new Array<ImageRegion>();
		for (final AtlasRegion atlasRegion : atlasArr) {
			imgArr.add(new ImageRegion(atlasRegion));
		}
		return imgArr;
	}
	
	public TextureRegion getFlipYTexRegion(final String regionName) {
		final TextureRegion textureRegion = new ImageRegion(imageAtlas.findRegion(regionName), false, true);
		return textureRegion;
	}

	public TextureRegion getFlipXTexRegion(final String regionName) {
		final TextureRegion textureRegion = new ImageRegion(imageAtlas.findRegion(regionName), true, false);
		return textureRegion;
	}

	public TextureRegion getFlipXYTexRegion(final String regionName) {
		final TextureRegion textureRegion = new ImageRegion(imageAtlas.findRegion(regionName), true, true);
		return textureRegion;
	}

	public TextureRegion getThumbTexRegion(final Difficulty dclty, final int levelNo) {
		final SaveThumbs st = new SaveThumbs(dclty, levelNo);
		st.setPlayAtlas();
		final FileHandle fh = st.createThumb();
		st.disposePlayAtlas();
		final TextureRegion textureRegion = new TextureRegion(new Texture(PixmapIO.readCIM(fh)), 100, 100);
		textureRegion.flip(false, true);
		return textureRegion;
	}

	public CheckBoxStyle getCheckBoxStyle(final String fontName, final int fontSize) {
		final CheckBoxStyle cs = new CheckBoxStyle();
		cs.checkboxOff = new TextureRegionDrawable(getFlipYTexRegion("checkboxoff"));
		cs.checkboxOn = new TextureRegionDrawable(getFlipYTexRegion("checkboxon"));
		cs.font = createFont(fontName, fontSize, false);
		cs.fontColor = Color.RED;
		return cs;
	}

	public SliderStyle getSliderStyle() {
		final SliderStyle ss = new SliderStyle();
		ss.background = new TextureRegionDrawable(getFlipYTexRegion("sliderbase"));
		ss.knob = new TextureRegionDrawable(getFlipYTexRegion("sliderknob"));
		return ss;
	}

	public TextFieldStyle getTextBoxStyle(final String fontName, final int fontSize) {
		final TextFieldStyle ts = new TextFieldStyle();
		ts.background = new TextureRegionDrawable(getFlipYTexRegion("textfieldbase"));
		ts.background.setLeftWidth(10);
		ts.background.setRightWidth(10);
		ts.cursor = new TextureRegionDrawable(getFlipYTexRegion("cursor"));
		ts.selection = new TextureRegionDrawable(getFlipYTexRegion("cursor"));
		ts.font = createFont(fontName, fontSize, false);
		ts.fontColor = Color.WHITE;
		return ts;
	}

	public LabelStyle getLabelStyle(final String fontName, final int fontSize) {
		final LabelStyle ls = new LabelStyle();
		ls.font = createFont(fontName, fontSize, false);
		ls.fontColor = Color.WHITE;
		return ls;
	}

	public void drawRectWithBorder(final Rect rect, final Color color) {
		sr.setColor(Color.BLACK);
		sr.rect(rect.left - 1, rect.top - 1, (rect.right - rect.left) + 1, (rect.bottom - rect.top) + 1);
		sr.setColor(color);
		sr.rect(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
	}

}
