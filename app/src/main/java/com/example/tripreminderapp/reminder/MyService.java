package com.example.tripreminderapp.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.tripreminderapp.HomeActivity;
import com.example.tripreminderapp.R;
import com.example.tripreminderapp.ui.trip_details.TripDetailsActivity;

public class MyService extends Service {
    private static final String CHANNEL_ID = "ch_id";

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RemoteViews customView =new RemoteViews(getPackageName(), R.layout.notification_reminder);
        Intent notificationIntent =new Intent(getApplicationContext(), HomeActivity.class);
        Intent hungupIntent =new Intent(getApplicationContext(), MyReceiver.class);
        Intent answerIntent = new Intent(this, TripDetailsActivity.class);

        if (intent.hasExtra("caller_text")) {
            answerIntent.putExtra("caller_text", intent.getStringExtra("caller_text"));
            customView.setTextViewText(R.id.callType, intent.getStringExtra("caller_text"));
        } else
            customView.setTextViewText(R.id.name, "app_name");
            //customView.setImageViewBitmap(R.id.photo, NotificationImageManager().getImageBitmap(intent.getStringExtra("user_thumbnail_image")))

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent hungupPendingIntent = PendingIntent.getBroadcast(this, 0, hungupIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent answerPendingIntent = PendingIntent.getActivity(this, 0, answerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        customView.setOnClickPendingIntent(R.id.btnAnswer, answerPendingIntent);
        customView.setOnClickPendingIntent(R.id.btnDecline, hungupPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel =new NotificationChannel("IncomingCall",
                    "IncomingCall", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "IncomingCall");
            notification.setContentTitle("reminder");
            notification.setTicker("Call_STATUS");
            notification.setContentText("IncomingCall");
            notification.setSmallIcon(R.drawable.add);
            notification.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
            notification.setCategory(NotificationCompat.CATEGORY_CALL);
            notification.setVibrate(null);
            notification.setOngoing(true);
            notification.setFullScreenIntent(pendingIntent, true);
            notification.setPriority(NotificationCompat.PRIORITY_HIGH);
            notification.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
            notification.setCustomContentView(customView);
            notification.setCustomBigContentView(customView);

            startForeground(1124, notification.build());
        } else {
            NotificationCompat.Builder notification =new NotificationCompat.Builder(this);
            notification.setContentTitle("app_name");
            notification.setTicker("Call_STATUS");
            notification.setContentText("IncomingCall");
            notification.setSmallIcon(R.drawable.add);
            notification.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.add));
            notification.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
            notification.setVibrate(null);
            notification.setContentIntent(pendingIntent);
            notification.setOngoing(true);
            notification.setCategory(NotificationCompat.CATEGORY_CALL);
            notification.setPriority(NotificationCompat.PRIORITY_HIGH);
             NotificationCompat.Action hangupAction =new NotificationCompat.Action.Builder(android.R.drawable.sym_action_chat, "HANG UP", hungupPendingIntent)
                    .build();
            notification.addAction(hangupAction);
            startForeground(1124, notification.build());
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }
}