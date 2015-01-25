package com.avy.cflag.desktop;

import java.io.FileInputStream;

import com.avy.cflag.base.AcraMap;
import com.avy.cflag.game.CFlagGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class CFlagDesktop {
	public static void main(final String[] args) {
		try {
			// final FileInputStream fi1 = new FileInputStream("D:/PersonalWork/Andriod/KeyStore/ReleaseCerts/encodedCert.bin");
			final FileInputStream fi1 = new FileInputStream("D:/PersonalWork/Android/KeyStore/DebugCerts/encodedCert.bin");
			final byte encodedCert[] = new byte[32];
			fi1.read(encodedCert);
			fi1.close();

			// final FileInputStream fi2 = new FileInputStream("D:/PersonalWork/Andriod/KeyStore/ReleaseCerts/certSignature.bin");
			final FileInputStream fi2 = new FileInputStream("D:/PersonalWork/Android/KeyStore/DebugCerts/certSignature.bin");
			final byte certSignature[] = new byte[16];
			fi2.read(certSignature);
			fi2.close();

			final AcraMap acraMap = new AcraMap() {
				@Override
				public void put(final String key, final Object value) {
					// Do Nothing - As this is just a interface for Android Acra Map
				}
			};

			final LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
			cfg.title = "CaptureTheFlag";
			cfg.width = 800;
			cfg.height = 480;
			cfg.resizable = false;

			new LwjglApplication(new CFlagGame(acraMap, encodedCert, certSignature), cfg);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
