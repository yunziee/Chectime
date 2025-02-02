package com.example.chectime

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomCalendarAdapter(
    private val context: Context,
    private val days: List<CalendarDay>,
    private val readBooks: MutableMap<String, String>, // key: yyyy-MM-dd, value: bookCoverUrl
    private val readingLogs: Set<String>, // 타이머 기록이 있는 날짜들을 Set으로 받아옴
    private val onDateClick: (String) -> Unit
) : RecyclerView.Adapter<CustomCalendarAdapter.CalendarViewHolder>() {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    // onBindViewHolder에서 bind 메서드 호출 시 formatDateToString을 전달
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = days[position]
        val dateKey = formatDateToString(day)  // formatDateToString 호출
        holder.bind(day, dateKey, readBooks, readingLogs, onDateClick)
    }

    override fun getItemCount(): Int = days.size

    // addBookMarker 메서드 수정
    fun addBookMarker(date: String, imageUrl: String) {
        Log.d("Calendar", "Adding book marker for $date with imageUrl: $imageUrl")
        readBooks[date] = imageUrl
        Log.d("Calendar", "Updated readBooks: $readBooks") // readBooks 상태 확인
        notifyItemChanged(getItemPosition(date))  // 해당 아이템만 업데이트
    }

    // 날짜 포맷을 yyyy-MM-dd로 변환하는 함수
    private fun formatDateToString(date: CalendarDay): String {
        val calendar = Calendar.getInstance().apply {
            set(date.year, date.month - 1, date.day)
        }
        return dateFormatter.format(calendar.time)
    }

    // 날짜에 해당하는 위치를 찾아주는 함수
    private fun getItemPosition(date: String): Int {
        return days.indexOfFirst { formatDateToString(it) == date }  // 해당 날짜가 있는 위치를 찾아 반환
    }

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bookCover: ImageView = view.findViewById(R.id.bookCover)
        private val highlightCircle: View = view.findViewById(R.id.highlightCircle)
        private val dateTextView: TextView = view.findViewById(R.id.dateText)

        fun bind(
            day: CalendarDay,
            dateKey: String,
            readBooks: Map<String, String>, // 날짜별 책 표지 URL
            readingLogs: Set<String>, // 타이머 기록이 있는 날짜들을 Set으로 받음
            onDateClick: (String) -> Unit
        ) {
            // 1일 이전의 날짜들은 표시하지 않음
            if (day.day < 1) {
                dateTextView.visibility = View.GONE  // 1일 전 날짜들은 보이지 않도록 처리
            } else {
                dateTextView.text = day.day.toString()  // CalendarDay에서 날짜(day)를 가져와 표시
                dateTextView.visibility = View.VISIBLE // 보이게 설정
            }

            // 타이머 기록이 있는 날짜 강조
            if (readingLogs.contains(dateKey)) {
                highlightCircle.visibility = View.VISIBLE // 타이머 기록이 있는 날짜 강조
            } else {
                highlightCircle.visibility = View.GONE  // View.INVISIBLE 대신 View.GONE을 사용
            }

            // 다 읽은 책의 표지 표시
            val imageUrl = readBooks[dateKey]
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(bookCover, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        Log.d("Picasso", "Image successfully loaded for $dateKey")
                    }

                    override fun onError(e: Exception?) {
                        Log.e("Picasso", "Failed to load image for $dateKey", e)
                    }
                })
                bookCover.visibility = View.VISIBLE
            } else {
                bookCover.visibility = View.INVISIBLE
            }

            itemView.setOnClickListener { onDateClick(dateKey) }
        }
    }
}
