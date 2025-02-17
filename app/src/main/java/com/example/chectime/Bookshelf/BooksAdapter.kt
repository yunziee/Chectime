// 책 목록 표시 어댑터
package com.example.chectime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class BooksAdapter(private var books: List<Book>, private val onBookClick: (Book) -> Unit) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.book_title)
        private val authorTextView: TextView = itemView.findViewById(R.id.book_author)
        private val coverImageView: ImageView = itemView.findViewById(R.id.book_image)
        private val publisherTextView: TextView = itemView.findViewById(R.id.book_publisher)
        private val isbnTextView: TextView = itemView.findViewById(R.id.book_isbn)


        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
            publisherTextView.text = book.publisher
            isbnTextView.text = book.isbn
            Picasso.get().load(book.cover).into(coverImageView)
            itemView.setOnClickListener {
                // 책 클릭 시 onBookClick 실행
                onBookClick(book)
            }
        }
    }
}
