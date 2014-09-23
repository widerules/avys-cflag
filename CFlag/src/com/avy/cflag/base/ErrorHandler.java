package com.avy.cflag.base;

import java.lang.Thread.UncaughtExceptionHandler;

import com.avy.cflag.game.CFlagGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ErrorHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		final String threadName = t.toString();
		StackTraceElement[] traceInfo = e.getStackTrace();

		String report = e.toString() + "\n\n";
		report = report + "--------- Stack trace ---------\n\n";
		report = report + threadName;

		for (final StackTraceElement element : traceInfo) {
			report = report + "    " + element.toString() + "\n";
		}

		report = report + "-------------------------------\n\n";
		report = report + "------------ Cause ------------\n\n";

		final Throwable cause = e.getCause();
		if (cause != null) {
			report = report + cause.toString() + "\n\n";
			traceInfo = cause.getStackTrace();
			for (final StackTraceElement element : traceInfo) {
				report = report + "    " + element.toString() + "\n";
			}
		}

		report = report + "-------------------------------\n\n";

		Gdx.app.error("CFLAG", report);

		FileHandle fh = Gdx.files.external("Android/data/" + CFlagGame.packageName + "/Trace/StackTrace.log");
		fh.writeString(report, false);
		Gdx.app.exit();
		// TODO - Create a screen to display error and send bug report
	}
}
