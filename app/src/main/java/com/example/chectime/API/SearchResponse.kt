package com.example.chectime

import android.os.Parcel
import android.os.Parcelable

data class SearchResponse(
    val item: List<ApiBook> // 'item'은 검색 결과로 반환된 책 목록
)

data class ApiBook(
    val title: String,  // 책 제목
    val author: String, // 저자
    val publisher: String, // 출판사
    val cover: String, // 표지
    val isbn: String, // ISBN 국제 표준 도서 고유 값
    val pubDate: String, // 발매 날짜
    val description: String, // 책 설명
    val priceStandard: String, // 판매 가격
    val status: String // 현재 독서 상태
) : Parcelable { // Parcelable 인터페이스 구현

    constructor(parcel: Parcel) : this(
        title = parcel.readString() ?: "",
        author = parcel.readString() ?: "",
        publisher = parcel.readString() ?: "",
        cover = parcel.readString() ?: "",
        isbn = parcel.readString() ?: "",
        pubDate = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        priceStandard = parcel.readString() ?: "",
        status = parcel.readString() ?: "" // status를 읽어오는 부분 추가
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
        parcel.writeString(status) // status를 쓰는 부분 추가
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApiBook> {
        override fun createFromParcel(parcel: Parcel): ApiBook {
            return ApiBook(parcel)
        }

        override fun newArray(size: Int): Array<ApiBook?> {
            return arrayOfNulls(size)
        }
    }
}
