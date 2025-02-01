package com.example.chectime

import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class TimerFragment : Fragment() {

    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button
    private lateinit var saveButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var statusSpinner: Spinner
    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false
    private var seconds: Long = 0
    private lateinit var databaseHelper: BookshelfDatabaseHelper
    private lateinit var currentBook: Book
    private lateinit var bookList: List<Book> // 책 목록을 저장할 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_timer, container, false)

        // 버튼 및 텍스트 뷰 초기화
        startButton = binding.findViewById(R.id.btnStart)
        stopButton = binding.findViewById(R.id.btnStop)
        resetButton = binding.findViewById(R.id.btnReset)
        saveButton = binding.findViewById(R.id.btnSave)
        timerTextView = binding.findViewById(R.id.tvTimer)
        statusSpinner = binding.findViewById(R.id.spinner)

        // Database Helper 초기화
        databaseHelper = BookshelfDatabaseHelper(requireContext())

        // 현재 읽고 있는 책 목록을 DB에서 가져오기
        loadReadingBooks()

        // 스피너에 책 목록을 설정
        val bookTitles = bookList.map { it.title } // 책 제목 목록
        val bookAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bookTitles)
        bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = bookAdapter

        // 타이머 시작 버튼 클릭 리스너
        startButton.setOnClickListener {
            if (!isTimerRunning) {
                startTimer()
            }
        }

        // 타이머 정지 버튼 클릭 리스너
        stopButton.setOnClickListener {
            if (isTimerRunning) {
                stopTimer()
            }
        }

        // 타이머 리셋 버튼 클릭 리스너
        resetButton.setOnClickListener {
            resetTimer()
        }

        // 타이머 저장 버튼 클릭 리스너
        saveButton.setOnClickListener {
            saveTimerRecord()
        }

        return binding
    }

    // 현재 읽고 있는 책을 DB에서 가져오는 함수
    private fun loadReadingBooks() {
        bookList = databaseHelper.getBooksByStatus("reading") // 'reading' 상태인 책만 불러오기
    }

    private fun startTimer() {
        isTimerRunning = true
        startButton.text = "타이머 진행 중..."
        stopButton.isEnabled = true
        resetButton.isEnabled = false
        saveButton.isEnabled = false

        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                seconds++
                timerTextView.text = DateUtils.formatElapsedTime(seconds)
            }

            override fun onFinish() {}
        }.start()
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
        startButton.text = "Start"
        stopButton.isEnabled = false
        resetButton.isEnabled = true
        saveButton.isEnabled = true
    }

    private fun resetTimer() {
        seconds = 0
        timerTextView.text = "00:00:00"
        startButton.text = "Start"
        stopButton.isEnabled = false
        resetButton.isEnabled = false
        saveButton.isEnabled = false
    }

    private fun saveTimerRecord() {
        val timerDurationInSeconds = seconds
        val timerDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // 스피너에서 선택된 책을 가져옴
        val selectedBookTitle = statusSpinner.selectedItem.toString()
        val selectedBook = bookList.find { it.title == selectedBookTitle }

        selectedBook?.let { book ->
            book.isbn?.let { isbn ->
                book.title?.let { title ->
                    databaseHelper.addOrUpdateTimerRecord(isbn, title, timerDate, timerDurationInSeconds)
                    resetTimer() // 타이머 초기화
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }
}
