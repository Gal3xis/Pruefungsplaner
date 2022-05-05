package com.fachhochschulebib.fhb.pruefungsplaner.utils;

import java.lang.System;

/**
 * Class with Functionalities to send notifications to the users smartphone.
 *
 * @author Alexander Lange
 * @since 1.6
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J4\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\rH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/fachhochschulebib/fhb/pruefungsplaner/utils/PushService;", "", "()V", "CHANNEL_ID", "", "createNotificationChannel", "", "context", "Landroid/content/Context;", "sendNotification", "message", "title", "pIntent", "Landroid/app/PendingIntent;", "app_debug"})
public final class PushService {
    @org.jetbrains.annotations.NotNull()
    public static final com.fachhochschulebib.fhb.pruefungsplaner.utils.PushService INSTANCE = null;
    
    /**
     * Unique ID to identify a notification from this application
     */
    private static final java.lang.String CHANNEL_ID = "FH_Pruefungsplaner_NotificationChannel";
    
    private PushService() {
        super();
    }
    
    /**
     * Sends a notification to the smartphone.
     *
     * **See Also:**[Documentation](https://developer.android.com/training/notify-user/build-notification#kotlin)
     */
    @android.annotation.SuppressLint(value = {"UnspecifiedImmutableFlag"})
    public final void sendNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    android.app.PendingIntent pIntent) {
    }
    
    /**
     * Used to create a channel to send the notifications!
     *
     * **See Also:**[Documentation](https://developer.android.com/training/notify-user/build-notification#kotlin)
     * Before you can deliver the notification on Android 8.0 and higher, you must register your app's notification channel with the system by passing an instance of NotificationChannel to createNotificationChannel(). So the following code is blocked by a condition on the SDK_INT version:
     *   Because you must create the notification channel before posting any notifications on Android 8.0 and higher, you should execute this code as soon as your app starts. It's safe to call this repeatedly because creating an existing notification channel performs no operation.
     *
     *   Notice that the NotificationChannel constructor requires an importance, using one of the constants from the NotificationManager class. This parameter determines how to interrupt the user for any notification that belongs to this channel—though you must also set the priority with setPriority() to support Android 7.1 and lower (as shown above).
     *
     *   Although you must set the notification importance/priority as shown here, the system does not guarantee the alert behavior you'll get. In some cases the system might change the importance level based other factors, and the user can always redefine what the importance level is for a given channel.
     *
     *   For more information about what the different levels mean, read about notification importance levels.
     */
    private final void createNotificationChannel(android.content.Context context) {
    }
}