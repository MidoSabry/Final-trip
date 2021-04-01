package com.example.tripreminderapp.reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.tripreminderapp.HomeActivity;
import com.example.tripreminderapp.R;

import java.util.Random;

public class MyReceiver extends BroadcastReceiver {
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Intent intent1 = new Intent(context,MyService.class);
        context.startService(intent1);
    }

    private void displayNotification(Context context, String title, String task) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_reminder);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "channel_id")
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
//import android.app.AlertDialog;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.util.Log;
//import android.view.WindowManager;
//
//import androidx.core.app.NotificationCompat;
//
//public class MyReceiver extends BroadcastReceiver {
//    Context context;
//    WindowManager windowManager2;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.i("myReciever", "in my receiever");
//        this.context = context;
//
//        //displayAlert(context);
//
//
////        Intent intent1 = new Intent(context,MainActivity2.class);
////        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        context.startActivity(intent1);
//    }
//
//    private void displayNotification(String title, String task) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "channel_id")
//                .setContentTitle(title)
//                .setContentText(task)
//                .setSmallIcon(R.mipmap.ic_launcher);
//
//        notificationManager.notify(1, notification.build());
//    }
//
//    private void displayAlert(Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage("Are you sure you want to exit?").setCancelable(
//                false).setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                }).setNegativeButton("No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//
//}