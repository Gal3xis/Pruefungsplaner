package com.fachhochschulebib.fhb.pruefungsplaner.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fachhochschulebib.fhb.pruefungsplaner.R
import com.fachhochschulebib.fhb.pruefungsplaner.view.activities.StartActivity
import java.util.*

/**
 * Class with Functionalities to send notifications to the users smartphone.
 *
 * @author Alexander Lange
 * @since 1.6
 */
object PushService {
    /**
     * Unique ID to identify a notification from this application
     */
    private const val CHANNEL_ID = "FH_Pruefungsplaner_NotificationChannel"

    /**
     * Sends a notification to the smartphone.
     *
     * **See Also:**[Documentation](https://developer.android.com/training/notify-user/build-notification#kotlin)
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    fun sendNotification(context: Context, message:String?=null, title:String? = null, pIntent: PendingIntent? = null){
        createNotificationChannel(context)

        //Create intent to navigate the user after he tapped the notification. That starts the StartActivity.
        val intent = Intent(context, StartActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = pIntent?:PendingIntent.getActivity(context, 0, intent, 0)

        val id = Calendar.getInstance().timeInMillis.toInt()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title?:context.resources.getString(R.string.app_name))
                .setContentText(message?:"")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setSilent(!(context.getSharedPreferences("settings",Context.MODE_PRIVATE)?.getBoolean("notification_sounds",false)?:false))

        with(NotificationManagerCompat.from(context))
        {
            notify(id,builder.build())
        }
    }

    /**
     * Used to create a channel to send the notifications!
     *
     * **See Also:**[Documentation](https://developer.android.com/training/notify-user/build-notification#kotlin)
     * Before you can deliver the notification on Android 8.0 and higher, you must register your app's notification channel with the system by passing an instance of NotificationChannel to createNotificationChannel(). So the following code is blocked by a condition on the SDK_INT version:
     *    Because you must create the notification channel before posting any notifications on Android 8.0 and higher, you should execute this code as soon as your app starts. It's safe to call this repeatedly because creating an existing notification channel performs no operation.
     *
     *    Notice that the NotificationChannel constructor requires an importance, using one of the constants from the NotificationManager class. This parameter determines how to interrupt the user for any notification that belongs to this channel—though you must also set the priority with setPriority() to support Android 7.1 and lower (as shown above).
     *
     *    Although you must set the notification importance/priority as shown here, the system does not guarantee the alert behavior you'll get. In some cases the system might change the importance level based other factors, and the user can always redefine what the importance level is for a given channel.
     *
     *    For more information about what the different levels mean, read about notification importance levels.
     */
    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}