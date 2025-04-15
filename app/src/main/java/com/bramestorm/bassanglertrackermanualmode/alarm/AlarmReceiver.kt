package com.bramestorm.bassanglertrackermanualmode.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast
import com.bramestorm.bassanglertrackermanualmode.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Toast.makeText(context, "⏰ Alarm Triggered!", Toast.LENGTH_LONG).show()
        val player = MediaPlayer.create(context, R.raw.alarm_sound)
        player?.start()
    }
}
