package com.example.chectime

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ReadingLogDialogFragment(private val records: List<TimerRecord>) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("읽은 책 목록")

        val message = records.joinToString("\n") {
            val minutes = it.duration / 60 // 분
            val seconds = it.duration % 60 // 초
            "${it.title}\n${minutes}분 ${seconds}초 읽음\n\n"
        }

        builder.setMessage(message)
        builder.setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }

        return builder.create()
    }
}
