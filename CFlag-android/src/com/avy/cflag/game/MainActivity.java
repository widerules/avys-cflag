package com.avy.cflag.game;

import org.acra.ACRA;

import android.os.Bundle;

import com.avy.cflag.base.AcraMap;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        if(android.os.Build.VERSION.SDK_INT>= android.os.Build.VERSION_CODES.KITKAT){
            cfg.useGLSurfaceView20API18=true;
            cfg.useImmersiveMode=true;
        }
        cfg.useAccelerometer=false;
        cfg.useCompass=false;
        cfg.useWakelock=true;
        initialize(new CFlagGame(new AcraMap() {
			
			@Override
			public void putCustomData(String key, String value) {
				ACRA.getErrorReporter().putCustomData(key, value);
			}
		}), cfg);
    }
}