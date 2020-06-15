package com.venado.test_server.server

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.venado.test_server.R
import com.venado.test_server.ui.MainActivity

const val APP_NOTIFICATION_ID = 5584320
const val APP_NOTIFICATION_CHANNEL = "com.venado.vcbsimulator.APP_NOTIFICATION"

class NotificationSender(val context: Context?,
                         val notificationManager: NotificationManager?) {

    fun cancelNotification(id: Int) {
        notificationManager?.cancel(id)
    }

    fun cancelAllNotification() {
        notificationManager?.cancelAll()
    }

    fun showNotification(title: String, text: String, id: Int) {
        val ctx = context ?: return
        val notification = getAppNotification(title, text, ctx, true)
        notification?.run { showNotification(notification, id) }
    }

    private fun showNotification(notification: Notification, id: Int) =
        notificationManager?.notify(id, notification)

    fun getAppNotification(title: String, text: String, context: Context?, initChannel: Boolean = false): Notification? {
        val ctx = context ?: return null
        if (initChannel) {
            buildNotificationChannel(ctx)
        }
        val notificationIntent = Intent().setClass(ctx, MainActivity::class.java)
        notificationIntent.action = Intent.ACTION_MAIN
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        val contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, 0)

        val builder = NotificationCompat.Builder(ctx, APP_NOTIFICATION_CHANNEL)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)

        val notification = builder.build()
        //notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
        //notification.`when` = TimeUtil.getCurrentTimeMillis()
        return notification
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun buildNotificationChannel(context: Context?, channelId: String = APP_NOTIFICATION_CHANNEL, isImportant: Boolean = false): NotificationManager {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(channelId,
                context.getString(R.string.app_name),
                if(isImportant) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT).apply {
                enableVibration(false)
                enableLights(false)
                setSound(null, null)
            })
            notificationManager
        } else {
            notificationManager
        }
    }
}