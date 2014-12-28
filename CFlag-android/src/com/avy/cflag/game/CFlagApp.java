package com.avy.cflag.game;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import android.app.Application;

	@ReportsCrashes(
		formKey="",
	    formUri = "https://avystudios.iriscouch.com/acra-cflag/_design/acra-storage/_update/report",
	    reportType = HttpSender.Type.JSON,
	    httpMethod = HttpSender.Method.POST,
	    formUriBasicAuthLogin = "rssindian",
	    formUriBasicAuthPassword = "narayan*28482",
		mode = ReportingInteractionMode.DIALOG,
		resToastText = R.string.crash_toast_text, 
		resDialogText = R.string.crash_dialog_text,
		resDialogIcon = android.R.drawable.ic_dialog_info, 
		resDialogTitle = R.string.crash_dialog_title, 
		resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
		resDialogOkToast = R.string.crash_dialog_ok_toast
	)


public class CFlagApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
	}
}
