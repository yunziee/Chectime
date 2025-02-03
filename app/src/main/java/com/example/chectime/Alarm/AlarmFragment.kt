package com.example.chectime

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chectime.databinding.FragmentAlarmBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    private val alarmList = ArrayList<Alarm>()
    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)

        // Android 13(API 33) 이상에서 정확한 알람 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }

        loadAlarms() // 알람 목록 불러오기
        setupRecyclerView()
        setupAddAlarmButton()

        return binding.root
    }

    private fun setupRecyclerView() {
        alarmAdapter = AlarmAdapter(
            alarmList,
            requireContext(),
            ::setAlarm,
            ::cancelAlarm,
            ::editAlarm
        )

        binding.alarmRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.alarmRecyclerView.adapter = alarmAdapter
    }

    private fun setupAddAlarmButton() {
        binding.addAlarmButton.setOnClickListener { openAlarmDialog() }
    }

    private fun openAlarmDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_alarm, null)

        val hourPicker: NumberPicker = dialogView.findViewById(R.id.hourPicker)
        val minutePicker: NumberPicker = dialogView.findViewById(R.id.minutePicker)
        val amPmPicker: NumberPicker = dialogView.findViewById(R.id.amPmPicker)
        val alarmLabel: EditText = dialogView.findViewById(R.id.alarmLabel)
        val saveAlarmButton: Button = dialogView.findViewById(R.id.saveAlarmButton)

        val dayCheckBoxes = listOf(
            dialogView.findViewById<CheckBox>(R.id.checkbox_mon),
            dialogView.findViewById<CheckBox>(R.id.checkbox_tue),
            dialogView.findViewById<CheckBox>(R.id.checkbox_wed),
            dialogView.findViewById<CheckBox>(R.id.checkbox_thu),
            dialogView.findViewById<CheckBox>(R.id.checkbox_fri),
            dialogView.findViewById<CheckBox>(R.id.checkbox_sat),
            dialogView.findViewById<CheckBox>(R.id.checkbox_sun)
        )

        hourPicker.minValue = 1
        hourPicker.maxValue = 12

        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.setFormatter { i -> String.format("%02d", i) }

        amPmPicker.minValue = 0
        amPmPicker.maxValue = 1
        amPmPicker.displayedValues = arrayOf("AM", "PM")

        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()

        saveAlarmButton.setOnClickListener {
            val hour = hourPicker.value
            val minute = minutePicker.value
            val isPM = amPmPicker.value == 1
            val label = alarmLabel.text.toString()

            val selectedDays = dayCheckBoxes
                .mapIndexed { index, checkBox -> if (checkBox.isChecked) index + 1 else null }
                .filterNotNull()

            Log.d("AlarmFragment", "Selected Days: $selectedDays")

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR, if (hour == 12) 0 else hour)
                set(Calendar.AM_PM, if (isPM) Calendar.PM else Calendar.AM)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }

            if (selectedDays.isEmpty()) {
                Toast.makeText(requireContext(), "요일을 선택해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val alarm = Alarm(
                id = (alarmList.size + 1),
                timeInMillis = calendar.timeInMillis,
                label = label,
                days = selectedDays
            )

            alarmList.add(alarm)
            saveAlarms() // 알람 데이터 저장
            alarmAdapter.notifyDataSetChanged()
            setAlarm(alarm)
            Toast.makeText(requireContext(), "루틴 설정 완료", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

    private fun editAlarm(alarm: Alarm) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_alarm, null)

        val hourPicker: NumberPicker = dialogView.findViewById(R.id.hourPicker)
        val minutePicker: NumberPicker = dialogView.findViewById(R.id.minutePicker)
        val amPmPicker: NumberPicker = dialogView.findViewById(R.id.amPmPicker)
        val alarmLabel: EditText = dialogView.findViewById(R.id.alarmLabel)
        val saveAlarmButton: Button = dialogView.findViewById(R.id.saveAlarmButton)

        val dayCheckBoxes = listOf(
            dialogView.findViewById<CheckBox>(R.id.checkbox_mon),
            dialogView.findViewById<CheckBox>(R.id.checkbox_tue),
            dialogView.findViewById<CheckBox>(R.id.checkbox_wed),
            dialogView.findViewById<CheckBox>(R.id.checkbox_thu),
            dialogView.findViewById<CheckBox>(R.id.checkbox_fri),
            dialogView.findViewById<CheckBox>(R.id.checkbox_sat),
            dialogView.findViewById<CheckBox>(R.id.checkbox_sun)
        )

        val calendar = Calendar.getInstance().apply { timeInMillis = alarm.timeInMillis }
        hourPicker.minValue = 1
        hourPicker.maxValue = 12
        hourPicker.value = calendar.get(Calendar.HOUR).let { if (it == 0) 12 else it }

        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.value = calendar.get(Calendar.MINUTE)
        minutePicker.setFormatter { i -> String.format("%02d", i) }

        amPmPicker.minValue = 0
        amPmPicker.maxValue = 1
        amPmPicker.value = if (calendar.get(Calendar.AM_PM) == Calendar.PM) 1 else 0
        amPmPicker.displayedValues = arrayOf("AM", "PM")

        alarmLabel.setText(alarm.label)

        dayCheckBoxes.forEachIndexed { index, checkBox ->
            checkBox.isChecked = alarm.days.contains(index + 1)
        }

        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()

        saveAlarmButton.setOnClickListener {
            val updatedHour = hourPicker.value
            val updatedMinute = minutePicker.value
            val updatedIsPM = amPmPicker.value == 1
            val updatedLabel = alarmLabel.text.toString()

            val updatedDays = dayCheckBoxes
                .mapIndexed { index, checkBox -> if (checkBox.isChecked) index + 1 else null }
                .filterNotNull()

            calendar.set(Calendar.HOUR, if (updatedHour == 12) 0 else updatedHour)
            calendar.set(Calendar.AM_PM, if (updatedIsPM) Calendar.PM else Calendar.AM)
            calendar.set(Calendar.MINUTE, updatedMinute)

            // Alarm 객체 수정
            alarm.timeInMillis = calendar.timeInMillis
            alarm.label = updatedLabel
            alarm.days = updatedDays

            // Adapter 갱신
            alarmAdapter.notifyDataSetChanged()

            // 알람 업데이트
            setAlarm(alarm)

            saveAlarms() // 수정된 알람 저장
            Toast.makeText(requireContext(), "수정 완료!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

    private fun saveAlarms() {
        val gson = Gson()
        val json = gson.toJson(alarmList)
        sharedPreferences.edit().putString("alarms", json).apply()
    }

    private fun loadAlarms() {
        val gson = Gson()
        val json = sharedPreferences.getString("alarms", null)
        if (json != null) {
            val type = object : TypeToken<List<Alarm>>() {}.type
            alarmList.clear()
            alarmList.addAll(gson.fromJson(json, type))
        }
    }

    fun setAlarm(alarm: Alarm) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra("label", alarm.label)
            putExtra("days", alarm.days.toIntArray())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 기존 알람이 있다면 먼저 취소
        alarmManager.cancel(pendingIntent)

        // 새로운 알람 설정
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.timeInMillis, pendingIntent)
    }


    private fun cancelAlarm(alarm: Alarm) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context, alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            val cancelIntent = PendingIntent.getBroadcast(
                context, alarm.id, intent, PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(cancelIntent)
            cancelIntent.cancel()
        }


        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()

        // 리스트에서 해당 알람 삭제
        val removed = alarmList.removeIf { it.id == alarm.id }

        if (removed) {
            saveAlarms()
            Log.d("AlarmFragment", "알람 삭제 완료: ID=${alarm.id}")
        } else {
            Log.d("AlarmFragment", "알람 삭제 실패: ID=${alarm.id} 찾을 수 없음")
        }

        alarmAdapter.notifyDataSetChanged()
        Toast.makeText(requireContext(), "루틴 삭제 완료", Toast.LENGTH_SHORT).show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
