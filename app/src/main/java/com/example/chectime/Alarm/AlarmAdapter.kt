package com.example.chectime

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView

class AlarmAdapter(
    private val alarmList: ArrayList<Alarm>,
    private val context: Context,
    private val setAlarm: (Alarm) -> Unit,
    private val cancelAlarm: (Alarm) -> Unit,
    private val editAlarm: (Alarm) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]

        val daysString = alarm.days.joinToString(", ") { day ->
            when (day){
                1 -> "Mon"
                2 -> "Tue"
                3 -> "Wed"
                4 -> "Thu"
                5 -> "Fri"
                6 -> "Sat"
                7 -> "Sun"
                else -> ""
            }
        }

        holder.timeTextView.text = alarm.getFormattedTime()
        holder.labelTextView.text = alarm.label
        holder.daysTextView.text = daysString
        holder.alarmSwitch.isChecked = alarm.isEnabled

        holder.alarmSwitch.setOnCheckedChangeListener {_, isChecked ->
            alarm.isEnabled = isChecked
            if (isChecked) setAlarm(alarm) else cancelAlarm(alarm)
        }
        holder.deleteButton.setOnClickListener {
            alarmList.removeAt(position)
            notifyDataSetChanged()
        }
        // AlarmAdapter에서 editAlarm 콜백을 처리하는 부분
        holder.editButton.setOnClickListener {
            // EditAlarmFragment를 생성하여 알람 객체를 전달
            val fragment = EditAlarmFragment.newInstance(alarm)

            // 프래그먼트 트랜잭션을 통해 EditAlarmFragment로 화면 전환
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment) // fragment_container는 프래그먼트를 담을 컨테이너 뷰의 ID
            transaction.addToBackStack(null) // 뒤로 가기 스택에 추가
            transaction.commit()
        }

    }

    override fun getItemCount(): Int = alarmList.size

    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val labelTextView: TextView = itemView.findViewById(R.id.labelTextView)
        val daysTextView: TextView = itemView.findViewById(R.id.daysTextView)
        val alarmSwitch: Switch = itemView.findViewById(R.id.alarmSwitch)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val editButton: ImageButton = itemView.findViewById(R.id.editButton)
    }
}