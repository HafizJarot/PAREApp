package com.hafiz.pareapp.utils.fcm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hafiz.pareapp.R
import com.hafiz.pareapp.SplashScreenActivity

object NotificationHandler {

    fun displayNotification(context : Context, title : String, body : String, i : Map<String, String>){
        val intent = Intent(context, SplashScreenActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val mBuilder = NotificationCompat.Builder(context, SplashScreenActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.badge_default)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val mNotificationMgr = NotificationManagerCompat.from(context)
        mNotificationMgr.notify(1, mBuilder.build())
    }
}