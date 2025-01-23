package com.example.chectime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BookshelfFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookshelf, container, false)

        // Toolbar 설정
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        // 툴바 메뉴를 인플레이트
        toolbar.inflateMenu(R.menu.menu_toolbar)

        // 메뉴 아이템 클릭 처리
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.iv_search -> {
                    // 돋보기 클릭 시 SearchFragment로 이동
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, SearchFragment()) // fragment_container는 프래그먼트가 담길 컨테이너 ID
                    transaction.addToBackStack(null) // 뒤로가기 스택에 추가
                    transaction.commit()
                    true
                }
                else -> false
            }
        }

        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        // BookshelfPagerAdapter 연결
        val adapter = BookshelfPagerAdapter(this)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager2 연결
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

}
