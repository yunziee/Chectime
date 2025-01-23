package com.example.chectime

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.Calendar

class EditAlarmFragment : Fragment(R.layout.fragment_edit_alarm) {

    private lateinit var alarm: Alarm // 수정할 Alarm 객체

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 인텐트를 통해 전달된 Alarm 객체 받기
        alarm = arguments?.getParcelable("alarm") ?: return // Alarm 객체가 없으면 종료

        // UI 초기화
        val labelTextView: EditText = view.findViewById(R.id.alarmLabel)
        val dayCheckboxes = listOf(
            view.findViewById<CheckBox>(R.id.checkbox_mon),
            view.findViewById<CheckBox>(R.id.checkbox_tue),
            view.findViewById<CheckBox>(R.id.checkbox_wed),
            view.findViewById<CheckBox>(R.id.checkbox_thu),
            view.findViewById<CheckBox>(R.id.checkbox_fri),
            view.findViewById<CheckBox>(R.id.checkbox_sat),
            view.findViewById<CheckBox>(R.id.checkbox_sun)
        )

        // 기존 알람 데이터로 초기화
        labelTextView.setText(alarm.label)

        // 선택된 요일 체크
        alarm.days.forEach { day ->
            dayCheckboxes[day - 1].isChecked = true
        }

        // NumberPicker 초기화
        val hourPicker : NumberPicker = view.findViewById(R.id.hourPicker)
        hourPicker.minValue = 1
        hourPicker.maxValue = 12

        val minutePicker : NumberPicker = view.findViewById(R.id.minutePicker)
        minutePicker.minValue = 0
        minutePicker.maxValue = 59

        val amPmPicker : NumberPicker = view.findViewById(R.id.amPmPicker)
        amPmPicker.minValue = 0
        amPmPicker.maxValue = 1
        amPmPicker.wrapSelectorWheel = false
        amPmPicker.displayedValues = arrayOf("AM", "PM")

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = alarm.timeInMillis
        var hour = calendar.get(Calendar.HOUR)
        //if (hour == 0) = 12
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = calendar.get(Calendar.AM_PM)

        hourPicker.value = hour
        minutePicker.value = minute
        amPmPicker.value = amPm

        // 저장 버튼 클릭 이벤트 처리
        val saveButton: Button = view.findViewById(R.id.saveAlarmButton)
        saveButton.setOnClickListener {
            val updatedLabel = labelTextView.text.toString()
            val updatedDays = dayCheckboxes
                .mapIndexed { index, checkBox -> if (checkBox.isChecked) index + 1 else null }
                .filterNotNull()

            // TimePicker에서 시간을 가져와 알람 시간 업데이트
            val updatedCalendar = Calendar.getInstance()

            //ampm

            updatedCalendar.set(Calendar.HOUR_OF_DAY, hourPicker.value)
            updatedCalendar.set(Calendar.MINUTE, minutePicker.value)

            // 알람 객체 업데이트
            alarm.label = updatedLabel
            alarm.days = updatedDays
            alarm.timeInMillis = updatedCalendar.timeInMillis // 새로운 시간 설정

            // 알람 새로 설정
            setAlarm(alarm)

            //변경사항 어댑터 반영
            //(requireActivity() as MainActivity.update)

            // 변경된 알람을 저장
            Toast.makeText(requireContext(), "루틴이 수정되었습니다.", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack() // 이전 화면으로 돌아가기
        }
    }

    // 알람 설정 메서드
    private fun setAlarm(alarm: Alarm) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra("label", alarm.label)
            putExtra("days", alarm.days.toIntArray()) // 요일 정보 전달
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarm.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 알람 시간에 맞게 알람을 설정
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.timeInMillis, pendingIntent)
    }

    companion object {
        // Fragment를 호출할 때 Alarm 객체를 전달하는 방법
        fun newInstance(alarm: Alarm): EditAlarmFragment {
            val fragment = EditAlarmFragment()
            val bundle = Bundle().apply {
                putParcelable("alarm", alarm)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}
