package com.example.chectime

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class BookshelfDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "chectime.db"
        private const val DATABASE_VERSION = 4

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

        const val TIMER_TABLE_NAME = "timer_records"
        const val COLUMN_TIMER_DURATION = "timer_duration" // 타이머 지속 시간 (초 단위)
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
            $COLUMN_TIMER_ID INTEGER PRIMARY KEY,
            $COLUMN_ISBN TEXT,
            $COLUMN_TITLE TEXT,
            $COLUMN_TIMER_DATE TEXT,  -- 타이머 실행 날짜
            $COLUMN_TIMER_DURATION INTEGER  -- 지속 시간 (초 단위)
        )
        """.trimIndent()

        db.execSQL(createTimerTableQuery)

        // 테스트 데이터 삽입
        val testDataInserter = TestDataInserter(db!!)
        testDataInserter.addTestBooks()
        testDataInserter.addTestTimer()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 4) {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            db?.execSQL("DROP TABLE IF EXISTS $TIMER_TABLE_NAME")
            onCreate(db)
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

    // 'read' 상태인 책들의 표지 URL을 가져오는 함수
    fun getReadBooks(): List<Book> {
        val readBooks = mutableListOf<Book>()
        val books = getBooksByStatus("read") // 'read' 상태의 책들 가져오기

        for (book in books) {
            if (book.endDate != null && book.cover != null) {
                readBooks.add(book) // 끝난 날짜와 표지가 있는 책만 추가
            }
        }

        return readBooks
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
                put(COLUMN_TITLE, title)
                put(COLUMN_TIMER_DATE, date)
                put(COLUMN_TIMER_DURATION, duration)
            }

            db.insert(TIMER_TABLE_NAME, null, values)
        }

        cursor.close()
        db.close()
    }



    // 날짜별 타이머 기록 조회
    fun getTimerRecordsForDate(date: String): List<TimerRecord> {
        val records = mutableListOf<TimerRecord>()
        val db = readableDatabase
        val cursor = db.query(
            TIMER_TABLE_NAME,
            null,
            "$COLUMN_TIMER_DATE = ?", // 날짜로 필터링
            arrayOf(date),
            null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val timerId = getLong(getColumnIndexOrThrow(COLUMN_TIMER_ID))
                val isbn = getString(getColumnIndexOrThrow(COLUMN_ISBN))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val timerDate = getString(getColumnIndexOrThrow(COLUMN_TIMER_DATE))
                val duration = getInt(getColumnIndexOrThrow(COLUMN_TIMER_DURATION)) // 독서 시간

                records.add(TimerRecord(timerId, isbn, title, timerDate, duration))
                Log.d("ReadingLog", "TimerRecord: title=$title, duration=$duration")

            }
            close()
        }

        return records
    }


    // 날짜별로 타이머 기록이 있는지 확인해서 반환
    fun getReadingLogs(): Set<String> {
        val readingLogs = mutableSetOf<String>() // 날짜만 저장할 Set

        val db = readableDatabase

        // 타이머 기록을 조회
        val cursor = db.query(
            TIMER_TABLE_NAME, // 타이머 기록 테이블
            arrayOf(COLUMN_TIMER_DATE), // 날짜만 조회
            null, null, null, null, null // 그룹화나 정렬은 필요없음
        )

        with(cursor) {
            while (moveToNext()) {
                val timerDate = getString(getColumnIndexOrThrow(COLUMN_TIMER_DATE)) // 날짜
                readingLogs.add(timerDate) // 날짜만 Set에 추가
            }
            close()
        }

        return readingLogs // 타이머 기록이 있는 날짜들의 Set 반환
    }

    fun getReadBooksCountForMonth(yearMonth: String): Int {
        val db = readableDatabase
        val query = """
        SELECT COUNT(DISTINCT isbn) FROM books 
        WHERE status = 'read' 
        AND strftime('%Y-%m', read_date) = ?
    """
        val cursor = db.rawQuery(query, arrayOf(yearMonth))
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }

    fun getReadingDaysCountForMonth(yearMonth: String): Int {
        val db = readableDatabase
        val query = """
        SELECT COUNT(DISTINCT timer_date) FROM timer_table 
        WHERE strftime('%Y-%m', timer_date) = ?
    """
        val cursor = db.rawQuery(query, arrayOf(yearMonth))
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }


}

data class TimerRecord(
    val timerId: Long,
    val isbn: String,
    val title: String,
    val timerDate: String,
    val duration: Int
)
