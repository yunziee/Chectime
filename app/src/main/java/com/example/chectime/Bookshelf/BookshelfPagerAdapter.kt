package com.example.chectime

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BookshelfPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        // 탭 개수 (읽은 책, 독서 중, 읽는 책)
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        // 각 탭에 맞는 Fragment 반환
        return when (position) {
            0 -> ReadBooksFragment()
            1 -> ReadingBooksFragment()
            2 -> ToReadBooksFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
