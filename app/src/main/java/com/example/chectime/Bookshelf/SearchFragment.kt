package com.example.chectime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private val apiKey = "ttbwannabe17171929001" // 발급받은 API 키
    private lateinit var bookAdapter: BookAdapter
    private val books = mutableListOf<ApiBook>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val searchButton = view.findViewById<Button>(R.id.btn_search)
        val searchInput = view.findViewById<EditText>(R.id.et_search)

        // RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // SearchFragment에서 parentFragmentManager를 전달
        bookAdapter = BookAdapter(books, requireActivity().supportFragmentManager)
        recyclerView.adapter = bookAdapter

        searchButton.setOnClickListener {
            val query = searchInput.text.toString()
            if (query.isNotBlank()) {
                performBookSearch(query)
            } else {
                Toast.makeText(requireContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun performBookSearch(query: String) {
        val call = RetrofitClient.apiService.searchBooks(apiKey, query)
        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    val bookList = response.body()?.item ?: emptyList()
                    books.clear() // 이전 데이터 초기화
                    if (bookList.isNotEmpty()) {
                        books.addAll(bookList) // 검색 결과 추가
                        bookAdapter.notifyDataSetChanged() // RecyclerView 갱신
                    } else {
                        Toast.makeText(requireContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "검색 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "검색 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
