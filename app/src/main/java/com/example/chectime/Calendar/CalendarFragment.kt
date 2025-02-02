package com.example.chectime

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chectime.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CustomCalendarAdapter
    private lateinit var calendarLayoutManager: GridLayoutManager
    private lateinit var monthYearTextView: TextView
    private lateinit var prevMonthButton: ImageButton
    private lateinit var nextMonthButton: ImageButton
    private lateinit var weekdaysLayout: LinearLayout
    private lateinit var dbHelper: BookshelfDatabaseHelper

    private var currentCalendar: Calendar = Calendar.getInstance()

    // 다 읽은 책과 타이머 데이터를 저장하는 맵
    private val readBooks = mutableMapOf<String, String>() // key: yyyy-MM-dd, value: bookCoverUrl

    private var weekdaysAdded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        dbHelper = BookshelfDatabaseHelper(requireContext())

        calendarRecyclerView = binding.calendarRecyclerView
        monthYearTextView = binding.monthYearTextView
        prevMonthButton = binding.prevMonthButton
        nextMonthButton = binding.nextMonthButton
        weekdaysLayout = binding.weekdaysLayout // 요일 레이아웃 참조

        setupCalendar()

        prevMonthButton.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            setupCalendar()
        }

        nextMonthButton.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            setupCalendar()
        }

        return binding.root
    }

    private fun setupCalendar() {
        val dbReadingLogs = dbHelper.getReadingLogs() // 타이머 기록이 있는 날짜들을 가져옴
        // 현재 연도와 월을 표시
        val monthYearFormat = SimpleDateFormat("yyyy MMMM", Locale.getDefault())
        val formattedMonthYear = monthYearFormat.format(currentCalendar.time)
        monthYearTextView.text = formattedMonthYear

        // 요일 표시 설정 (요일 레이아웃은 한 번만 추가)
        if (!weekdaysAdded) {
            setupWeekdays()
            weekdaysAdded = true
        }

        // 현재 달에 맞는 날짜들을 가져옵니다.
        val daysInMonth = getDaysInMonth(currentCalendar)

        // RecyclerView에 어댑터 설정
        calendarAdapter = CustomCalendarAdapter(
            requireContext(),
            daysInMonth,
            readBooks,
            dbReadingLogs
        ) { date -> onDateClick(date) }

        // GridLayoutManager로 변경
        calendarLayoutManager = GridLayoutManager(requireContext(), 7) // 7개의 열 (일, 월, 화, 수, 목, 금, 토)
        calendarRecyclerView.layoutManager = calendarLayoutManager
        calendarRecyclerView.adapter = calendarAdapter

        showBooksOnCalendar()
    }

    private fun setupWeekdays() {
        val weekdays = listOf("일", "월", "화", "수", "목", "금", "토")
        for (i in 0 until weekdays.size) {
            val weekdayTextView = TextView(requireContext()).apply {
                text = weekdays[i]
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setPadding(10, 5, 10, 5)  // 각 날짜의 padding을 조정
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    // 각 TextView 사이의 간격을 설정
                    setMargins(8, 8, 8, 8) // 좌우 여백을 추가
                }
            }
            weekdaysLayout.addView(weekdayTextView)
        }
    }

    private fun getDaysInMonth(calendar: Calendar): List<CalendarDay> {
        val days = mutableListOf<CalendarDay>()

        // 해당 월의 첫 번째 날로 이동
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        // 해당 월의 첫 번째 날 요일을 구함
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // 해당 월의 마지막 날
        val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // 첫 번째 날 이전의 빈 날짜 (빈 날짜는 isValid = false로 설정)
        for (i in 1 until firstDayOfWeek) {
            days.add(CalendarDay(year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH) + 1, day = 0, isValid = false)) // 빈 날짜는 isValid = false
        }

        // 1일부터 마지막 날까지 날짜 추가
        for (day in 1..lastDayOfMonth) {
            days.add(CalendarDay(year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH) + 1, day = day, isValid = true)) // 실제 날짜는 isValid = true
        }

        return days
    }

    private fun onDateClick(day: String) {
        // 현재 선택된 연도와 월을 가져오기
        val selectedYear = currentCalendar.get(Calendar.YEAR)
        val selectedMonth = currentCalendar.get(Calendar.MONTH) + 1  // Calendar.MONTH는 0부터 시작하므로 +1

        // day가 "2025-01-31" 형식이면, 이 값을 분리하여 사용
        val formattedDate = if (day.contains("-")) {
            // "2025-01-31"과 같은 형식을 처리
            day
        } else {
            // "31"과 같은 숫자 값일 경우 (월/일)
            String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth, day.toInt())
        }

        val dayOnly = if (day.contains("-")) {
            day.split("-")[2]  // day를 "-" 기준으로 분리한 후, 세 번째 요소(일 부분)만 가져옴
        } else {
            day  // 이미 "31" 같은 일 형태로 들어있으면 그대로 사용
        }

        // 변환된 날짜로 DB에서 조회
        val records = dbHelper.getTimerRecordsForDate(formattedDate)

        if (records.isNotEmpty()) {
            val dialog = ReadingLogDialogFragment(records)
            dialog.show(parentFragmentManager, "ReadingLogDialog")

        } else {
            Toast.makeText(requireContext(), "$dayOnly 일의 타이머 기록이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    // 다 읽은 책 표지를 캘린더에 추가하는 함수
    fun showBooksOnCalendar() {
        val dbReadBooks = dbHelper.getReadBooks()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())  // 날짜 포맷 정의

        dbReadBooks.forEach { book ->
            val endDate = book.endDate
            val calendarDate = endDate?.let { convertDateToLong(it) }

            // Long 타입 타임스탬프를 Date 객체로 변환
            val date = calendarDate?.let { Date(it) }

            // 캘린더에 책 표지 표시
            val imageUrl = book.cover
            if (imageUrl != null && date != null) {
                val formattedDate = dateFormat.format(date)  // 날짜 포맷에 맞게 변환
                calendarAdapter.addBookMarker(formattedDate, imageUrl)  // 포맷된 날짜를 사용
            }
        }

        // 캘린더 UI 갱신
        calendarAdapter.notifyDataSetChanged()
    }


    // 날짜를 Long 타입으로 변환하는 함수
    private fun convertDateToLong(date: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObj = sdf.parse(date)
        return dateObj?.time ?: 0L
    }

}