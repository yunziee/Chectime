package com.example.chectime

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private val dateFormatter = SimpleDateFormat("d", Locale.getDefault())
    private var dates: List<Date> = emptyList()
    private var currentMonth: Int = -1
    private var currentYear: Int = -1

    // 달력을 업데이트하기 위한 메서드
    fun updateCurrentMonth(month: Int, year: Int) {
        currentMonth = month
        currentYear = year
        notifyDataSetChanged() // 데이터 변경 알림
    }

    fun submitList(newDates: List<Date>) {
        dates = newDates
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = dates[position]
        holder.bind(date, dateFormatter, currentMonth, currentYear)
    }

    override fun getItemCount(): Int = dates.size

    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(date: Date, formatter: SimpleDateFormat, currentMonth: Int, currentYear: Int) {
            val calendar = Calendar.getInstance().apply { time = date }

            textView.text = formatter.format(date)

            // 현재 월인지 확인
            val isCurrentMonth =
                calendar.get(Calendar.MONTH) == currentMonth && calendar.get(Calendar.YEAR) == currentYear

            // 스타일 설정: 현재 월은 검정색, 다른 월은 연한 회색
            if (isCurrentMonth) {
                textView.setTextColor(itemView.context.getColor(android.R.color.black))
            } else {
                textView.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
            }
        }
    }
}
