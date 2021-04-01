package com.example.tripreminderapp.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tripreminderapp.HomeActivity;
import com.example.tripreminderapp.R;

import java.util.Random;

public class MyWorker extends Worker {
    WindowManager windowManager2;
    private Context context;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        displayNotification("My Worker", "Hey I finished my work");
      //  dNotification();
        Intent intent = new Intent(getApplicationContext(),MyService.class);
        context.startService(intent);
/*
        IntentFilter filter = new IntentFilter("com.example.workmanagerexample");
        MyReceiver myReceiver = new MyReceiver();
        context.registerReceiver(myReceiver, filter);
        Intent intentToBC = new Intent();
        //intentToBC.putExtra("fname", FILE_NAME);
        intentToBC.setAction("com.example.workmanagerexample");
        //intentToBC.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intentToBC);
        //context.startActivity(new Intent(context,MainActivity2.class));*/
        return Result.success();
    }

    public void dNotification(){
        Intent fullScreenIntent = new Intent(getApplicationContext(), HomeActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(),"ssnn")
                        .setSmallIcon(R.drawable.add)
                        .setContentTitle("Incoming call")
                        .setContentText("(919) 555-1234")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)

                        // Use a full-screen intent only for the highest-priority alerts where you
                        // have an associated activity that you would like to launch after the user
                        // interacts with the notification. Also, if your app targets Android 10
                        // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                        // order for the platform to invoke this notification.
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        Notification incomingCallNotification = notificationBuilder.build();
        //startForeground(notificationId, notification);

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        //incomingCallNotification.notify(1, notificationBuilder.build());
    }

    private void displayNotification(String title, String task) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        RemoteViews notificationLayout = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_reminder);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                .setContentTitle(title)
                .setContentText(task)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setSmallIcon(R.drawable.add);

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(1, notification.build());
    }

}









































//package com.example.tripreminderiti;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.view.WindowManager;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import java.util.Random;
//
//public class MyWorker extends Worker {
//    WindowManager windowManager2;
//    private Context context;
//
//    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//        displayNotification("My Worker", "Hey I finished my work");
//
//
////        IntentFilter filter = new IntentFilter("com.example.workmanagerexample");
////        MyReceiver myReceiver = new MyReceiver();
////        context.registerReceiver(myReceiver, filter);
////        Intent intentToBC = new Intent();
////        //intentToBC.putExtra("fname", FILE_NAME);
////        intentToBC.setAction("com.example.workmanagerexample");
////        intentToBC.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
////        context.sendBroadcast(intentToBC);
////        context.startActivity(new Intent(context,MainActivity2.class));
//        return Result.success();
//    }
//
//    private void displayNotification(String title, String task) {
//        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "channel_id")
//                .setContentTitle(title)
//                .setContentText(task)
//                .setSmallIcon(R.mipmap.ic_launcher);
//        Random random = new Random();
//        int m = random.nextInt(9999 - 1000) + 1000;
//
//        notificationManager.notify(1, notification.build());
//    }
//
//}