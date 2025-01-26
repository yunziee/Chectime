// 책 표시 어댑터

package com.example.chectime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class BookAdapter(
    private val bookList: List<ApiBook>,
    private val fragmentManager: FragmentManager // FragmentManager 추가
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    // ViewHolder 클래스, RecyclerView 항목을 관리
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.book_title)
        val authorTextView: TextView = itemView.findViewById(R.id.book_author)
        val publisherTextView: TextView = itemView.findViewById(R.id.book_publisher)
        val isbnTextView: TextView = itemView.findViewById(R.id.book_isbn)
        val imageView: ImageView = itemView.findViewById(R.id.book_image)
    }

    // 항목을 위한 뷰를 생성하는 메소드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    // 데이터를 각 항목에 바인딩하는 메소드
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        holder.publisherTextView.text = book.publisher
        holder.isbnTextView.text = book.isbn

        // 이미지 URL을 가져와 Picasso를 사용해 이미지 로딩
        Picasso.get().load(book.cover).into(holder.imageView)

        // 책 아이템 클릭 시 상세 페이지로 이동
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("book", book)
            }

            val fragment = BookDetailFragment().apply {
                arguments = bundle
            }
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    // 전체 아이템 개수를 반환하는 메소드
    override fun getItemCount(): Int = bookList.size
}
