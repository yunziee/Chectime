package com.example.chectime

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class TimerFragment : Fragment() {

    private lateinit var tvTimer: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnReset: Button

    private var seconds = 0 // 총 초
    private var running = false
    private var wasRunning = false

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // 프래그먼트의 UI를 정의할 레이아웃 설정
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTimer = view.findViewById(R.id.tvTimer)
        btnStart = view.findViewById(R.id.btnStart)
        btnStop = view.findViewById(R.id.btnStop)
        btnReset = view.findViewById(R.id.btnReset)

        // 상태 복구 (화면 회전 등)
        savedInstanceState?.let {
            seconds = it.getInt("seconds")
            running = it.getBoolean("running")
            wasRunning = it.getBoolean("wasRunning")
        }

        // 시작 버튼
        btnStart.setOnClickListener {
            running = true
        }

        // 정지 버튼
        btnStop.setOnClickListener {
            running = false
        }

        // 초기화 버튼
        btnReset.setOnClickListener {
            running = false
            seconds = 0
            updateTimer()
        }

        runTimer()
    }

    private fun runTimer() {
        handler.post(object : Runnable {
            override fun run() {
                if (running) {
                    seconds++
                }
                updateTimer()
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun updateTimer() {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        val time = String.format("%02d:%02d:%02d", hours, minutes, secs)
        tvTimer.text = time
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
    }
}
