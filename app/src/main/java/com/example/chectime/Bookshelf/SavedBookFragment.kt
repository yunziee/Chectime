package com.example.chectime

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import java.util.*

class SavedBookFragment : Fragment() {

    private lateinit var book: Book
    private lateinit var dbHelper: BookshelfDatabaseHelper
    private lateinit var dateRangeEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var currentPageEditText: EditText
    private lateinit var totalPageEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_book, container, false)

        // 책 정보 전달받기
        arguments?.getParcelable<Book>("book")?.let {
            book = it
        }

        // BookshelfDatabaseHelper 초기화
        dbHelper = BookshelfDatabaseHelper(requireContext())

        // 데이터 표시
        view.findViewById<TextView>(R.id.sb_title).text = book.title
        view.findViewById<TextView>(R.id.sb_author).text = book.author ?: "저자 정보 없음"
        Picasso.get().load(book.cover).into(view.findViewById<ImageView>(R.id.sb_cover))

        // 날짜 범위 EditText 초기화
        dateRangeEditText = view.findViewById(R.id.sb_date)
        dateRangeEditText.setText("${book.startDate} to ${book.endDate}")

        // 날짜 선택 함수 연결
        dateRangeEditText.setOnClickListener { showDateRangePicker() }

        // 진행률 및 별점 관련 UI 요소 초기화
        currentPageEditText = view.findViewById(R.id.editTextNumber)
        totalPageEditText = view.findViewById(R.id.editTextNumber2)
        view.findViewById<RatingBar>(R.id.sb_rating).rating = book.rating!!

        // 저장된 memo 보이게
        view.findViewById<EditText>(R.id.sb_memo).setText(book.memo)

        // 진행률 계산
        val currentPage = book.currentPage
        val totalPages = book.totalPages
        currentPageEditText.setText(currentPage.toString())
        totalPageEditText.setText(totalPages.toString())

        // 진행률 계산 함수
        val progress = calculateProgress(currentPage, totalPages)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.max = 100
        progressBar.progress = progress

        // 상태에 맞는 버튼 UI 업데이트
        updateButtonsState(view)

        // 저장 버튼
        view.findViewById<Button>(R.id.sb_save).setOnClickListener {
            saveBookToDatabase(view)
        }

        // 읽고 있어요 버튼 클릭 이벤트
        val btnReading = view.findViewById<Button>(R.id.btn_reading)
        btnReading.setOnClickListener {
            saveBookToBookshelf(book.copy(status = "reading"))
            updateButtonsState(view)
        }

        // 다 읽었어요 버튼 클릭 이벤트
        val btnRead = view.findViewById<Button>(R.id.btn_read)
        btnRead.setOnClickListener {
            saveBookToBookshelf(book.copy(status = "read"))
            updateButtonsState(view)
        }

        // 읽고 싶어요 버튼 클릭 이벤트
        val btnToRead = view.findViewById<Button>(R.id.btn_to_read)
        btnToRead.setOnClickListener {
            saveBookToBookshelf(book.copy(status = "toRead"))
            updateButtonsState(view)
        }

        return view
    }

    private fun saveBookToBookshelf(updatedBook: Book) {
        // 책을 책장에 저장하는 로직
        val result = dbHelper.addOrUpdateBook(updatedBook)

        if (result > 0) {
            Toast.makeText(requireContext(), "책이 이동되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "책장 이동에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveBookToDatabase(view: View) {
        // 메모, 진행률, 날짜, 별점 등을 DB에 저장
        val memo = view.findViewById<EditText>(R.id.sb_memo).text.toString()
        val rating = view.findViewById<RatingBar>(R.id.sb_rating).rating
        val currentPage = view.findViewById<EditText>(R.id.editTextNumber).text.toString().toIntOrNull() ?: 0
        val totalPages = view.findViewById<EditText>(R.id.editTextNumber2).text.toString().toIntOrNull() ?: 0

        // 날짜 범위
        val dateRange = dateRangeEditText.text.toString()
        val (startDate, endDate) = parseDateRange(dateRange)

        // 책 객체 업데이트
        val updatedBook = book.copy(
            memo = memo,
            rating = rating,
            startDate = startDate,
            endDate = endDate,
            currentPage = currentPage,
            totalPages = totalPages
        )

        // DB에 저장
        val result = dbHelper.addOrUpdateBook(updatedBook)

        if (result > 0) {
            Toast.makeText(requireContext(), "기록되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "기록에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateProgress(currentPage: Int, totalPage: Int): Int {
        return if (totalPage > 0) {
            (currentPage.toDouble() / totalPage * 100).toInt()
        } else {
            0
        }
    }

    // 날짜 범위 문자열에서 시작일과 종료일을 파싱
    private fun parseDateRange(dateRange: String): Pair<String?, String?> {
        val dates = dateRange.split(" to ")
        return if (dates.size == 2) {
            Pair(dates[0], dates[1])
        } else {
            Pair(null, null)
        }
    }

    // 날짜 범위 선택을 위한 DatePickerDialog
    private fun showDateRangePicker() {
        val calendar = Calendar.getInstance()

        // 시작일 DatePicker
        val startDatePickerDialog = DatePickerDialog(
            requireContext(),
            { _, startYear, startMonth, startDay ->
                val startDate = "$startYear-${startMonth + 1}-$startDay"
                // 시작일을 EditText에 표시
                dateRangeEditText.setText("$startDate")

                // 종료일 DatePicker
                val endDatePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, endYear, endMonth, endDay ->
                        val endDate = "$endYear-${endMonth + 1}-$endDay"
                        // 종료일을 EditText에 표시
                        val dateRange = "$startDate to $endDate"
                        dateRangeEditText.setText(dateRange)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                endDatePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        startDatePickerDialog.show()
    }

    // 버튼 UI 업데이트
    private fun updateButtonsState(view: View) {
        val btnReading = view.findViewById<Button>(R.id.btn_reading)
        val btnRead = view.findViewById<Button>(R.id.btn_read)
        val btnToRead = view.findViewById<Button>(R.id.btn_to_read)

        when (book.status) {
            "reading" -> {
                btnReading.isEnabled = false
                btnRead.isEnabled = true
                btnToRead.isEnabled = true
            }
            "read" -> {
                btnReading.isEnabled = true
                btnRead.isEnabled = false
                btnToRead.isEnabled = true
            }
            "toRead" -> {
                btnReading.isEnabled = true
                btnRead.isEnabled = true
                btnToRead.isEnabled = false
            }
            else -> {
                btnReading.isEnabled = true
                btnRead.isEnabled = true
                btnToRead.isEnabled = true
            }
        }
    }
}
