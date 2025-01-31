package com.example.chectime

import android.os.Parcel
import android.os.Parcelable

data class Book(
    val title: String?,
    val author: String?,
    val publisher: String?,
    val cover: String?,
    val isbn: String?,
    val pubDate: String?,
    val description: String?,
    val priceStandard: String?,
    val status: String?,
    val memo: String?,
    val startDate: String?,
    val endDate: String?,
    val rating: Float?,
    val currentPage: Int,
    val totalPages: Int
) : Parcelable {

    constructor(apiBook: ApiBook) : this(
        title = apiBook.title,
        author = apiBook.author,
        publisher = apiBook.publisher,
        cover = apiBook.cover,
        isbn = apiBook.isbn,
        pubDate = apiBook.pubDate,
        description = apiBook.description,
        priceStandard = apiBook.priceStandard,
        status = apiBook.status,
        memo = "",
        rating = null,
        startDate = null,
        endDate = null,
        currentPage = 0,
        totalPages = 0
    )

    constructor(parcel: Parcel) : this(
        title = parcel.readString(),
        author = parcel.readString(),
        publisher = parcel.readString(),
        cover = parcel.readString(),
        isbn = parcel.readString(),
        pubDate = parcel.readString(),
        description = parcel.readString(),
        priceStandard = parcel.readString(),
        status = parcel.readString(),
        memo = parcel.readString(),
        startDate = parcel.readString(),
        endDate = parcel.readString(),
        rating = parcel.readFloat(),
        currentPage = parcel.readInt(),
        totalPages = parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(publisher)
        parcel.writeString(cover)
        parcel.writeString(isbn)
        parcel.writeString(pubDate)
        parcel.writeString(description)
        parcel.writeString(priceStandard)
        parcel.writeString(status)
        parcel.writeValue(rating)  // 평점 저장
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}
