package com.example.chectime

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BookshelfDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bookshelf.db"
        private const val DATABASE_VERSION = 3

        const val TABLE_NAME = "books"
        const val COLUMN_TITLE = "title"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_PUBLISHER = "publisher"
        const val COLUMN_COVER = "cover"
        const val COLUMN_ISBN = "isbn"  // ISBN 컬럼을 고유 식별자로 설정
        const val COLUMN_PUB_DATE = "pub_date"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PRICE = "price"
        const val COLUMN_STATUS = "status" // 독서 상태 추가
        const val COLUMN_MEMO = "memo" // 메모
        const val COLUMN_START_DATE = "start_date" // 시작 날짜
        const val COLUMN_END_DATE = "end_date" // 종료 날짜
        const val COLUMN_RATING = "rating" // 별점
        const val COLUMN_CURRENT_PAGE = "current_page" // 현재 페이지
        const val COLUMN_TOTAL_PAGES = "total_pages" // 전체 페이지

        const val COLUMN_TIMER_DURATION = "timer_duration" // 타이머 지속 시간 (초 단위)
        const val TIMER_TABLE_NAME = "timer_records"
        const val COLUMN_TIMER_ID = "timer_id"
        const val COLUMN_TIMER_DATE = "timer_date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // isbn을 PRIMARY KEY로 설정하여 고유성을 보장
        val createTableQuery = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ISBN TEXT PRIMARY KEY,  -- ISBN을 Primary Key로 설정
            $COLUMN_TITLE TEXT NOT NULL,
            $COLUMN_AUTHOR TEXT,
            $COLUMN_PUBLISHER TEXT,
            $COLUMN_COVER TEXT,
            $COLUMN_PUB_DATE TEXT,
            $COLUMN_DESCRIPTION TEXT,
            $COLUMN_PRICE TEXT,
            $COLUMN_STATUS TEXT,
            $COLUMN_MEMO TEXT,
            $COLUMN_START_DATE TEXT,
            $COLUMN_END_DATE TEXT,
            $COLUMN_RATING FLOAT,
            $COLUMN_CURRENT_PAGE INT,
            $COLUMN_TOTAL_PAGES INT
        )
    """.trimIndent()
        db.execSQL(createTableQuery)

        // 타이머 기록을 위한 테이블 생성
        val createTimerTableQuery = """
        CREATE TABLE $TIMER_TABLE_NAME (
            $COLUMN_TIMER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_ISBN TEXT,
            $COLUMN_TITLE TEXT,
            $COLUMN_TIMER_DATE TEXT,  -- 타이머 실행 날짜
            $COLUMN_TIMER_DURATION INTEGER,  -- 지속 시간 (초 단위)
            FOREIGN KEY ($COLUMN_ISBN) REFERENCES books($COLUMN_ISBN)
        )
        """.trimIndent()

        db.execSQL(createTimerTableQuery)

        // 테스트 데이터 삽입
        addTestData(db)
    }

    private fun addTestData(db: SQLiteDatabase) {
        val testBooks = listOf(
            ContentValues().apply {
                put(COLUMN_ISBN, "K442934507")
                put(COLUMN_TITLE, "모우어")
                put(COLUMN_AUTHOR, "천선란 (지은이)")
                put(COLUMN_PUBLISHER, "문학동네")
                put(COLUMN_COVER, "https://image.aladin.co.kr/product/35099/68/coversum/k442934507_1.jpg")
                put(COLUMN_PUB_DATE, "2024-11-15")
                put(COLUMN_DESCRIPTION, "2019년 『천 개의 파랑』으로 한국과학문학상 장편 대상을 수상하며 혜성같이 등장해 한국 SF의 눈부신 미래를 만들고 있는 작가 천선란 의 세 번째. 『노랜드』 이후 2년 만에 묶는 소설집으로 미발표작 두 편을 포함해 2020년부터 2024년까지 쓴 단편 여덟 편이 수록되어 있다.")
                put(COLUMN_PRICE, "17000")
                put(COLUMN_STATUS, "read")
                put(COLUMN_MEMO, "책이 조금 어려웠지만 SF는 늘 재미있다 ㅎㅎ")
                put(COLUMN_START_DATE, "2025-01-21")
                put(COLUMN_END_DATE, "2025-01-24")
                put(COLUMN_RATING, 4.0)
                put(COLUMN_CURRENT_PAGE, 321)
                put(COLUMN_TOTAL_PAGES, 321)

            },
            ContentValues().apply {
                put(COLUMN_ISBN, "8954651135")
                put(COLUMN_TITLE, "흰 - 2024 노벨문학상 수상작가, 한강 소설")
                put(COLUMN_AUTHOR, "한강 (지은이), 최진혁 (사진)")
                put(COLUMN_PUBLISHER, "문학동네")
                put(COLUMN_COVER, "https://image.aladin.co.kr/product/14322/3/coversum/8954651135_3.jpg")
                put(COLUMN_PUB_DATE, "2018-04-25")
                put(COLUMN_DESCRIPTION, "2018년 봄, 한강 작가의 소설 흰을 새롭게 선보인다. 이 년 전 오월에 세상에 나와 빛의 겹겹 오라기로 둘러싸인 적 있던 그 &lt;흰&gt;에 새 옷을 입히게 된 건 소설 발간에 즈음해 행했던 작가의 퍼포먼스가 글과 함께 배었으면 하는 바람에서였다.")
                put(COLUMN_PRICE, "14500")
                put(COLUMN_STATUS, "reading")
                put(COLUMN_MEMO, "2월 안으로는 꼭 전부 읽어야지")
                put(COLUMN_START_DATE, "2025-01-29")
                put(COLUMN_CURRENT_PAGE, 85)
                put(COLUMN_TOTAL_PAGES, 196)
            },
            ContentValues().apply {
                put(COLUMN_ISBN, "K462930652")
                put(COLUMN_TITLE, "입속 지느러미")
                put(COLUMN_AUTHOR, "조예은 (지은이)")
                put(COLUMN_PUBLISHER, "한겨레출판")
                put(COLUMN_COVER, "https://image.aladin.co.kr/product/34002/54/coversum/k462930652_1.jpg")
                put(COLUMN_PUB_DATE, "2024-05-30")
                put(COLUMN_DESCRIPTION, "제2회 황금가지 타임리프 소설 공모전에서 〈오버랩 나이프, 나이프〉로 우수상을 받으며 작품 활동을 시작한 조예은 작가가 신작 소설《입속 지느러미》로 야심 차게 돌아왔다. 인어 이야기와 세이렌 신화를 결합해 잔혹하지만 아련하고 서글프지만 사랑스러운 서사로 독자를 새롭게 만난다.")
                put(COLUMN_PRICE, "15000")
                put(COLUMN_STATUS, "toRead")
                put(COLUMN_MEMO, "여름 장마철에 꼭 읽고 싶은 책")
            }
        )

        for (values in testBooks) {
            db.insert(TABLE_NAME, null, values)
            Log.d(TAG, "Inserted test book: ISBN = ${values.getAsString(COLUMN_ISBN)}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE IF NOT EXISTS $TIMER_TABLE_NAME (" +
                    "$COLUMN_TIMER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_ISBN TEXT, " +
                    "$COLUMN_TIMER_DATE TEXT, " +
                    "$COLUMN_TIMER_DURATION INTEGER, " +
                    "FOREIGN KEY ($COLUMN_ISBN) REFERENCES books($COLUMN_ISBN))")
        }
    }

    // 책 추가 & 업데이트
    fun addOrUpdateBook(book: Book): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, book.title)
            put(COLUMN_AUTHOR, book.author)
            put(COLUMN_PUBLISHER, book.publisher)
            put(COLUMN_COVER, book.cover)
            put(COLUMN_ISBN, book.isbn)  // ISBN을 고유 식별자로 사용
            put(COLUMN_PUB_DATE, book.pubDate)
            put(COLUMN_DESCRIPTION, book.description)
            put(COLUMN_PRICE, book.priceStandard)
            put(COLUMN_STATUS, book.status) // 독서 상태 저장
            put(COLUMN_MEMO, book.memo)
            put(COLUMN_START_DATE, book.startDate)
            put(COLUMN_END_DATE, book.endDate)
            put(COLUMN_RATING, book.rating)
            put(COLUMN_CURRENT_PAGE, book.currentPage)
            put(COLUMN_TOTAL_PAGES, book.totalPages)
        }

        // ISBN으로 기존 책을 찾아서 업데이트 또는 삽입
        val rowsAffected = db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ISBN = ?",
            arrayOf(book.isbn)
        )

        // 만약 업데이트된 행이 없다면, 삽입
        return if (rowsAffected == 0) {
            val result = db.insert(TABLE_NAME, null, values)
            Log.d(TAG, "Book inserted with title: ${book.title}, Result: $result")
            result
        } else {
            Log.d(TAG, "Book updated with title: ${book.title}")
            rowsAffected.toLong()
        }
    }

    // 특정 상태의 책들 조회
    fun getBooksByStatus(status: String): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null, // 모든 열 선택
            "$COLUMN_STATUS = ?", // 상태에 따른 필터링
            arrayOf(status),
            null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val author = getString(getColumnIndexOrThrow(COLUMN_AUTHOR))
                val publisher = getString(getColumnIndexOrThrow(COLUMN_PUBLISHER))
                val cover = getString(getColumnIndexOrThrow(COLUMN_COVER))
                val isbn = getString(getColumnIndexOrThrow(COLUMN_ISBN))
                val pubDate = getString(getColumnIndexOrThrow(COLUMN_PUB_DATE))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val price = getString(getColumnIndexOrThrow(COLUMN_PRICE))
                val status = getString(getColumnIndexOrThrow(COLUMN_STATUS)) // 독서 상태
                val startDate = getString(getColumnIndexOrThrow(COLUMN_START_DATE))
                val endDate = getString(getColumnIndexOrThrow(COLUMN_END_DATE))
                val memo = getString(getColumnIndexOrThrow(COLUMN_MEMO))
                val rating = getFloat(getColumnIndexOrThrow(COLUMN_RATING))
                val currentPage = getInt(getColumnIndexOrThrow(COLUMN_CURRENT_PAGE))
                val totalPages = getInt(getColumnIndexOrThrow(COLUMN_TOTAL_PAGES))

                books.add(Book(title, author, publisher, cover, isbn, pubDate, description, price, status, memo, startDate, endDate, rating, currentPage, totalPages))
            }
            close()
        }

        return books
    }

    // 타이머 기록 추가
    fun addOrUpdateTimerRecord(isbn: String, title: String, date: String, duration: Long) {
        val db = writableDatabase

        // 기존에 같은 날짜, 같은 책의 기록이 있는지 확인
        val cursor = db.rawQuery(
            "SELECT $COLUMN_TIMER_DURATION FROM $TIMER_TABLE_NAME WHERE $COLUMN_ISBN = ? AND $COLUMN_TIMER_DATE = ?",
            arrayOf(isbn, date)
        )

        if (cursor.moveToFirst()) {
            // 기존 기록이 있다면 시간 업데이트 (더하기)
            val existingDuration = cursor.getInt(0)
            val newDuration = existingDuration + duration

            val values = ContentValues().apply {
                put(COLUMN_TIMER_DURATION, newDuration)
                put(COLUMN_TITLE, title) // 제목 업데이트
            }

            db.update(
                TIMER_TABLE_NAME, values,
                "$COLUMN_ISBN = ? AND $COLUMN_TIMER_DATE = ?",
                arrayOf(isbn, date)
            )
        } else {
            // 기록이 없으면 새로 삽입
            val values = ContentValues().apply {
                put(COLUMN_ISBN, isbn)
                put(COLUMN_TITLE, title) // 제목도 저장
                put(COLUMN_TIMER_DATE, date)
                put(COLUMN_TIMER_DURATION, duration)
            }

            db.insert(TIMER_TABLE_NAME, null, values)
        }

        cursor.close()
        db.close()
    }



    // 오늘 날짜를 반환하는 함수
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // 특정 책의 날짜별 타이머 기록 조회
    fun getTimerRecordsByBook(isbn: String): List<TimerRecord> {
        val records = mutableListOf<TimerRecord>()
        val db = readableDatabase
        val cursor = db.query(
            TIMER_TABLE_NAME,
            null,
            "$COLUMN_ISBN = ?",
            arrayOf(isbn),
            null, null, "$COLUMN_TIMER_DATE DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val timerId = getLong(getColumnIndexOrThrow(COLUMN_TIMER_ID))
                val timerDate = getString(getColumnIndexOrThrow(COLUMN_TIMER_DATE))
                val duration = getInt(getColumnIndexOrThrow(COLUMN_TIMER_DURATION))

                records.add(TimerRecord(timerId, isbn, timerDate, duration))
            }
            close()
        }

        return records
    }
}

data class TimerRecord(
    val timerId: Long,
    val isbn: String,
    val timerDate: String,
    val duration: Int
)
