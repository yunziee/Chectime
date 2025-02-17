package com.example.chectime

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity

class AlarmAdapter(
    private val alarmList: ArrayList<Alarm>,
    private val context: Context,
    private val setAlarm: (Alarm) -> Unit,
    private val cancelAlarm: (Alarm) -> Unit,
    private val editAlarm: (Alarm) -> Unit,
    private val removeAlarm: (Alarm) -> Unit
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]

        holder.timeTextView.text = alarm.getFormattedTime()
        holder.labelTextView.text = alarm.label
        holder.daysTextView.text = alarm.days.joinToString(", ") { day ->
            when (day) {
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

        holder.alarmSwitch.isChecked = alarm.isEnabled

        holder.alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            alarm.isEnabled = isChecked
            if (isChecked) {
                setAlarm(alarm)
            } else {
                cancelAlarm(alarm)
            }
        }

        holder.deleteButton.setOnClickListener {
            removeAlarm(alarm) // 특정 알람만 삭제

            if (position >= 0 && position < alarmList.size) {
                alarmList.removeAt(position) // 리스트에서 제거
                notifyItemRemoved(position) // 항목만 제거
            } else {
                Log.e("AlarmAdapter", "잘못된 접근입니다")
            }
        }

        holder.editButton.setOnClickListener {
            editAlarm(alarm)

            // 데이터가 수정되었을 경우 반영
            notifyItemChanged(position) // 수정된 항목만 갱신
        }
    }

    override fun getItemCount(): Int = alarmList.size

    // ViewHolder 클래스
    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val labelTextView: TextView = itemView.findViewById(R.id.labelTextView)
        val daysTextView: TextView = itemView.findViewById(R.id.daysTextView)
        val alarmSwitch: Switch = itemView.findViewById(R.id.alarmSwitch)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val editButton: ImageButton = itemView.findViewById(R.id.editButton)
    }
}
