package com.example.chectime

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val label = intent.getStringExtra("label") ?: "Alarm"
        val days = intent?.getIntArrayExtra("days")?: intArrayOf()

        // Toast 메시지 표시
        Toast.makeText(context, "루틴이 설정되었습니다.", Toast.LENGTH_SHORT).show()

        // 알림 소리 울리기
        showNotification(context, label)
    }

    // 알림을 띄우는 함수
    private fun showNotification(context: Context, label: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android 8.0 이상에서는 NotificationChannel이 필요합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_channel"
            val channelName = "Alarm Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel for alarm notifications"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 알림을 생성합니다.
        val notification: Notification = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // 알림 아이콘
            .setContentTitle("Alarm!")
            .setContentText("Time for: $label")
            .setAutoCancel(true)
            .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI) // 기본 알람 소리
            .build()

        // 알림을 표시합니다.
        notificationManager.notify(0, notification)
    }
}