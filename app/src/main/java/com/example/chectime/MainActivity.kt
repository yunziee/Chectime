package com.example.chectime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chectime.Stopwatch.TimerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // BottomNavigationView 설정
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        
        loadFragment(CalendarFragment())

        // BottomNavigationView 클릭 리스너 설정
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_calendar -> loadFragment(CalendarFragment()) // 캘린더
                R.id.nav_bookshelf -> loadFragment(BookshelfFragment()) // 책장
                R.id.nav_routine -> loadFragment(AlarmFragment()) // 루틴
                R.id.nav_timer -> loadFragment(TimerFragment()) // 타이머
//                R.id.nav_settings -> loadFragment(SettingsFragment()) // 설정
                else -> false
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        // Fragment 트랜잭션 시작
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }
}
