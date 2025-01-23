package com.example.chectime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var calendarAdapter: CalendarAdapter
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트의 레이아웃을 inflate
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvMonthYear = view.findViewById<TextView>(R.id.tvMonthYear)
        val tvSuccessRate = view.findViewById<TextView>(R.id.tvSuccessRate)
        val btnPrevMonth = view.findViewById<ImageButton>(R.id.btnPrevMonth)
        val btnNextMonth = view.findViewById<ImageButton>(R.id.btnNextMonth)
        val calendarRecyclerView = view.findViewById<RecyclerView>(R.id.calendarRecyclerView)

        // RecyclerView 설정
        calendarRecyclerView.layoutManager = GridLayoutManager(context, 7)
        calendarAdapter = CalendarAdapter()
        calendarRecyclerView.adapter = calendarAdapter

        // 초기 데이터 설정
        updateCalendar(tvMonthYear)

        // 초기 UI 설정
        tvTitle.text = "이 달의 독서 성공률"
        tvSuccessRate.text = "오늘까지 성공률 85%"
        updateCalendar(tvMonthYear)

        // 이전 월 버튼 동작
        btnPrevMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateCalendar(tvMonthYear)
        }

        // 다음 월 버튼 동작
        btnNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateCalendar(tvMonthYear)
        }

        return view
    }

    private fun updateCalendar(tvMonthYear: TextView) {
        // 현재 월과 연도 가져오기
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        // 상단 월/연도 텍스트 업데이트
        val dateFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        tvMonthYear.text = dateFormatter.format(calendar.time)

        // 날짜 리스트 생성 및 Adapter 업데이트
        val daysInMonth = getDaysInMonth(calendar)
        calendarAdapter.submitList(daysInMonth)
        calendarAdapter.updateCurrentMonth(currentMonth, currentYear) // 현재 월/연도 전달
    }

    // 월의 시작 요일과 끝 요일을 고려한 유동적인 달력을 구성
    private fun getDaysInMonth(calendar: Calendar): List<Date> {
        val days = mutableListOf<Date>()
        val tempCalendar = calendar.clone() as Calendar

        // 현재 월의 첫 번째 날 설정
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1)
        // 월요일 기준으로 시작 요일 계산
        val startDayOfWeek = (tempCalendar.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1

        // 이전 달 날짜를 채우기 위해 시작 날짜 조정
        tempCalendar.add(Calendar.DAY_OF_MONTH, -(startDayOfWeek - 1))

        // 총 셀 개수(6주 x 7일 = 42개) 채우기
        val totalCells = 42
        while (days.size < totalCells) {
            days.add(tempCalendar.time)
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1) // 하루씩 증가
        }

        return days
    }

    private fun getMonth(calendar: Calendar): String {
        val dateFormatter = SimpleDateFormat("MMMM", Locale.getDefault())
        return dateFormatter.format(calendar.time)
    }
}
