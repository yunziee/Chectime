package com.example.chectime

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AladinApiService {

    // 알라딘 API 책 검색 요청
    @GET("https://www.aladin.co.kr/ttb/api/ItemSearch.aspx")
    fun searchBooks(
        @Query("ttbkey") apiKey: String, // 발급받은 API 키
        @Query("Query") query: String,   // 검색어
        @Query("SearchTarget") searchTarget: String = "Book", // 기본값: Book
        @Query("output") output: String = "JS", // JSON 형식으로 결과 받기
        @Query("MaxResults") maxResults: Int = 10, // 일단 최대 검색 결과 개수 10개 설정
        @Query("Version") version: String = "20131101" // 버전 명시
    ): Call<SearchResponse> // API 응답을 SearchResponse 객체로 받기
}
