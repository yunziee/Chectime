package com.example.chectime

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BookshelfFragment : Fragment() {

    private lateinit var bookshelfDatabaseHelper: BookshelfDatabaseHelper
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookshelf, container, false)

        bookshelfDatabaseHelper = BookshelfDatabaseHelper(requireContext())

        // Toolbar 설정
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu_toolbar)

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.iv_search -> {
                    // 돋보기 클릭 시 SearchFragment로 이동
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, SearchFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }
                else -> false
            }
        }

        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)

        // BookshelfPagerAdapter 연결
        val adapter = BookshelfPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "독서 완료"
                1 -> "독서 중"
                2 -> "독서 예정"
                else -> null
            }
        }.attach()

        return view
    }

    // 책 저장 후 데이터 반영
    fun saveBookAndRefresh(book: Book) {
        bookshelfDatabaseHelper.addOrUpdateBook(book)
        Log.d("saveBookAndRefresh", "Book saved: ${book.title}")

        // 저장 후 각 탭 새로고침
        refreshTab("read", "f0") // 독서 완료
        refreshTab("reading", "f1") // 독서 중
        refreshTab("toRead", "f2") // 독서 예정
    }

    // 특정 상태의 책 목록을 탭에 반영
    private fun refreshTab(status: String, fragmentTag: String) {
        val newBooks = bookshelfDatabaseHelper.getBooksByStatus(status)
        Log.d("refreshTab", "Books with status $status: $newBooks")
        val currentFragment = childFragmentManager.findFragmentByTag(fragmentTag)
        if (currentFragment is BookshelfTabFragment) {
            currentFragment.refreshBooks(newBooks)
        }
    }

    // BookshelfPagerAdapter: 각 탭에 맞는 책 목록을 반환하는 어댑터
    inner class BookshelfPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 3 // 독서 완료, 독서 중, 독서 예정

        override fun createFragment(position: Int): Fragment {
            val status = when (position) {
                0 -> "read"      // 독서 완료
                1 -> "reading"   // 독서 중
                2 -> "toRead"    // 독서 예정
                else -> ""
            }

            // status에 맞는 책 목록을 가져옴
            val books = bookshelfDatabaseHelper.getBooksByStatus(status)
            Log.d("BookshelfPagerAdapter", "Books for status $status: $books")
            return BookshelfTabFragment.newInstance(books, position)
        }
    }
}

class BookshelfTabFragment : Fragment() {

    private lateinit var books: List<Book>
    private var tabPosition: Int = 0

    companion object {
        private const val ARG_BOOKS = "books"
        private const val ARG_POSITION = "position"

        fun newInstance(books: List<Book>, position: Int): BookshelfTabFragment {
            val fragment = BookshelfTabFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_BOOKS, ArrayList(books))
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookshelf_tab, container, false)

        // 전달받은 책 목록을 불러옴
        books = arguments?.getParcelableArrayList(ARG_BOOKS) ?: emptyList()
        tabPosition = arguments?.getInt(ARG_POSITION) ?: 0

        // RecyclerView에 어댑터 연결
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = BooksAdapter(books)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    // 새로고침 메소드 (새로운 책 목록 반영)
    fun refreshBooks(newBooks: List<Book>) {
        books = newBooks
        // RecyclerView의 어댑터를 새로 갱신
        (view?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter as? BooksAdapter)?.apply {
            notifyDataSetChanged()
        }
    }
}
