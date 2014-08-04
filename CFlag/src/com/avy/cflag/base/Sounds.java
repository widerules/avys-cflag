package com.avy.cflag.base;

import com.avy.cflag.game.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public enum Sounds {
	move, shoot, blast;

	private static boolean enabled = true;
	private static float volume = 0.1f;
	private Sound sound = null;

	public static void loadAll() {
		for (final Sounds snd : Sounds.values()) {
			snd.sound = Gdx.audio.newSound(Gdx.files.internal(Constants.SOUNDS_FLDR + "/" + snd.name() + ".mp3"));
		}
	}

	public void play() {
		if (enabled) {
			sound.stop();
			sound.play(volume);
		}
	}

	public static void setVolume(float volume) {
		Sounds.volume = volume;
	}

	public static void setState(boolean state) {
		Sounds.enabled = state;
	}

	public static void dispose() {
		for (final Sounds snd : Sounds.values()) {
			if (snd.sound != null) {
				snd.sound.dispose();
			}
		}
	}
}
