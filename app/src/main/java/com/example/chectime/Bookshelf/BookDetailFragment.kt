package com.example.chectime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso

class BookDetailFragment : Fragment() {

    private lateinit var book: Book
    private lateinit var dbHelper: BookshelfDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_detail, container, false)

        // 전달받은 책 데이터
        val apiBook: ApiBook = arguments?.getParcelable("book")!!
        book = Book(apiBook) // ApiBook을 Book으로 변환

        // 데이터 표시
        view.findViewById<TextView>(R.id.tv_title).text = book.title
        view.findViewById<TextView>(R.id.tv_author).text = book.author ?: "저자 정보 없음"
        view.findViewById<TextView>(R.id.tv_publisher).text = book.publisher ?: "출판사 정보 없음"
        view.findViewById<TextView>(R.id.tv_description).text = book.description ?: "설명 없음"
        view.findViewById<TextView>(R.id.tv_pubDate).text = book.pubDate ?: "발매일 정보 없음"
        view.findViewById<TextView>(R.id.tv_priceStandard).text = book.priceStandard ?: "가격 정보 없음"

        // 책 표지 이미지 로드
        val coverImageView = view.findViewById<ImageView>(R.id.iv_cover)
        Picasso.get().load(book.cover).into(coverImageView)

        // BookshelfDatabaseHelper 초기화
        dbHelper = BookshelfDatabaseHelper(requireContext())

        // 책 상태에 맞는 버튼 UI 업데이트
        updateButtonsState(view)

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
            Toast.makeText(requireContext(), "책이 책장에 저장되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "책 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 버튼 UI 업데이트
    private fun updateButtonsState(view: View) {
        // 상태에 맞는 버튼을 비활성화/활성화
        val btnReading = view.findViewById<Button>(R.id.btn_reading)
        val btnRead = view.findViewById<Button>(R.id.btn_read)
        val btnToRead = view.findViewById<Button>(R.id.btn_to_read)

        // 상태에 따라 버튼 비활성화
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
                // 상태가 설정되지 않은 경우 모두 활성화
                btnReading.isEnabled = true
                btnRead.isEnabled = true
                btnToRead.isEnabled = true
            }
        }
    }
}
