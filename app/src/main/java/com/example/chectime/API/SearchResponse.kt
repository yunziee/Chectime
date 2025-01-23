package com.example.chectime

data class SearchResponse(
    val item: List<Book> // 'item'은 검색 결과로 반환된 책 목록
)

data class Book(
    val title: String,  // 책 제목
    val author: String, // 저자
    val publisher: String, // 출판사
    val cover: String, // 표지
    val isbn: String // ISBN 국제 표준 도서 고유 값
)
