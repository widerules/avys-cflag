package com.avy.cflag.game;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.acra.ACRA;

import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import com.avy.cflag.base.AcraMap;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Json;

public class MainActivity extends AndroidApplication {
	private static Json jSon = new Json();
	private static byte[] enocodedCert;;
	private static byte[] certSignature;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadCertInfo();

		final AcraMap acraMap = new AcraMap() {
			@Override
			public void put(final String key, final Object value) {
				ACRA.getErrorReporter().putCustomData(key, jSon.toJson(value));
			}
		};

		final AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			cfg.useGLSurfaceView20API18 = true;
			cfg.useImmersiveMode = true;
		}
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		cfg.useWakelock = true;

		initialize(new CFlagGame(acraMap, enocodedCert, certSignature), cfg);
	}

	public void loadCertInfo() {
		try {
			final Signature[] arrSignatures = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES).signatures;
			for (final Signature sig : arrSignatures) {
				final byte[] rawCert = sig.toByteArray();
				final InputStream certStream = new ByteArrayInputStream(rawCert);
				final CertificateFactory certFactory;
				final X509Certificate x509Cert;
				certFactory = CertificateFactory.getInstance("X509");
				x509Cert = (X509Certificate) certFactory.generateCertificate(certStream);

				enocodedCert = x509Cert.getEncoded();
				certSignature = x509Cert.getSignature();
			}
		} catch (final Exception e) {
			final StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			ACRA.getErrorReporter().putCustomData("HandledException", sw.toString());
			throw new RuntimeException("Error Loading Certificate Details");
		}
	}
}