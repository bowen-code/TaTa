package com.tata.version;


import com.tata.R;
import com.tata.activity.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;


public class DownloadReceiver extends BroadcastReceiver {
	public static NotificationManager mNotificationManager;
	private RemoteViews mRemoteViews;
	private Intent mIntent;
	private PendingIntent mPendingIntent;
	public Handler mHandler;
	public static int id;
	@Override
	public void onReceive(Context context, Intent intent) {
		int progress=intent.getIntExtra("pro", 0);
		id=intent.getIntExtra("id", 0);
		final Notification notification = new Notification();
		mNotificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.download);
		notification.icon = R.drawable.icon;
//		notification.when=id;
		notification.flags=Notification.FLAG_NO_CLEAR;
//		notification.defaults=Notification.DEFAULT_SOUND;
//		mRemoteViews.setImageViewResource(R.id.image_download,
//				R.drawable.download);
		mIntent = new Intent(context, MainActivity.class);
		mPendingIntent = PendingIntent.getActivity(context, 0,
				mIntent, 0);
				mRemoteViews.setProgressBar(R.id.progress_down, 100,
						progress, false);
				mRemoteViews.setTextViewText(R.id.text_download,
						progress + "%");
				notification.contentView = mRemoteViews;
				notification.contentIntent = mPendingIntent;
				mNotificationManager.notify(id, notification);
               if(progress==100){
            	   mNotificationManager.cancel(id);
               }
	}
}
