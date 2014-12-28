package com.avy.cflag.base;

import com.avy.cflag.game.Constants;
import com.avy.cflag.game.MemStore;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public enum Musics {
	track1, track2, track3, track4;

	private static boolean enabled = true;
	private static float volume = 0.1f;
	private static Music curMusic = null;

	public void loadAndPlay() {
		if (curMusic != null) {
			curMusic.stop();
			curMusic.dispose();
		}
		if (enabled) {
			curMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.MUSICS_FLDR + "/" + name() + ".ogg"));
			curMusic.setLooping(true);
			curMusic.setVolume(volume);
			curMusic.play();
		}
	}

	public static void setVolume(final float volume) {
		Musics.volume = volume;
		if (curMusic != null) {
			curMusic.setVolume(volume);
		}
	}

	public static void setState(final boolean state) {
		Musics.enabled = state;
		if (curMusic != null) {
			if (curMusic.isPlaying() && !Musics.enabled) {
				curMusic.stop();
			} else if (!curMusic.isPlaying() && Musics.enabled) {
				curMusic.play();
			}
		} else {
			if (Musics.enabled) {
				MemStore.curUserOPTS.getMusicTrack().loadAndPlay();
			}
		}
	}

	public static void dispose() {
		if (curMusic != null) {
			curMusic.dispose();
		}
	}
}
