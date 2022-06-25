package ge.gmikeladze.assignment4

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ge.gmikeladze.assignment4.view.MainActivity
import ge.gmikeladze.assignment4.view.MainActivity.Companion.INTENT_TIME_EXTRA

class AlarmBroadcast : BroadcastReceiver() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Broadcast Received")
        context?.let {
            val notificationManager = NotificationManagerCompat.from(context)
            val alarmTime = intent?.getStringExtra(INTENT_TIME_EXTRA)
            var piRequestCode = 0
            alarmTime?.let {
                piRequestCode =
                    alarmTime.substringAfter(":").toInt() + alarmTime.substringBefore(":")
                        .toInt() * 60
            }
            val notificationClickPendingIntent = PendingIntent.getActivity(
                context,
                piRequestCode,
                Intent(context, MainActivity::class.java),
                0
            )
            val snoozeClick = PendingIntent.getBroadcast(
                context, piRequestCode, Intent(SNOOZE_ACTION_NAME).apply {
                    `package` = context.packageName
                    putExtra(INTENT_TIME_EXTRA, alarmTime)
                },
                0
            )

            val cancelClick = PendingIntent.getBroadcast(
                context, piRequestCode, Intent(CANCEL_ACTION_NAME).apply {
                    `package` = context.packageName
                    putExtra(INTENT_TIME_EXTRA, alarmTime)
                },
                0
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(notificationManager)
            }
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm_clock)
                .setContentTitle(ALARM_TITLE)
                .setContentText(ALARM_TEXT + alarmTime)
                .setContentIntent(notificationClickPendingIntent)
                .addAction(R.mipmap.ic_launcher, CANCEL_TEXT, cancelClick)
                .addAction(R.mipmap.ic_launcher, SNOOZE_TEXT, snoozeClick)
                .build()

            intent?.let {
                when (intent.action) {
                    SNOOZE_ACTION_NAME -> {
                        notificationManager.cancel(piRequestCode)
                        // https://stackoverflow.com/questions/19096475/android-i-need-to-delay-a-notification
                        Handler(Looper.getMainLooper()).postDelayed({
                            notificationManager.notify(
                                piRequestCode,
                                notification
                            )
                        }, SNOOZE_DELAY_MILLIS)
                    }
                    CANCEL_ACTION_NAME -> {
                        notificationManager.cancel(piRequestCode)
                    }
                    else -> {
                        notificationManager.notify(piRequestCode, notification)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(notificationManager: NotificationManagerCompat) {
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

    }

    companion object {
        const val TAG = "Alarm_Receiver"
        const val ALARM_ACTION_NAME = "ge.gmikeladze.assignment4.ALARM_ACTION"
        const val CHANNEL_ID = "ge.gmikeladze.assignment4.CHANNEL_1"
        const val CHANNEL_NAME = "channel_name"
        const val SNOOZE_ACTION_NAME = "ge.gmikeladze.assignment4.SNOOZE_ACTION"
        const val CANCEL_ACTION_NAME = "ge.gmikeladze.assignment4.CANCEL_ACTION"
        const val SNOOZE_DELAY_MILLIS: Long = 60000
        private const val ALARM_TITLE = "Alarm message!"
        private const val ALARM_TEXT = "Alarm set on"
        private const val SNOOZE_TEXT = "SNOOZE"
        private const val CANCEL_TEXT = "CANCEL"
    }
}