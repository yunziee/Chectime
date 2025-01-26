package com.example.chectime

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class BookshelfDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "bookshelf.db"
        private const val DATABASE_VERSION = 1

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
            $COLUMN_STATUS TEXT
        )
    """.trimIndent()
        db.execSQL(createTableQuery)

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
            }
        )

        for (values in testBooks) {
            db.insert(TABLE_NAME, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
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

                books.add(Book(title, author, publisher, cover, isbn, pubDate, description, price, status))
            }
            close()
        }
        return books
    }

    // 모든 책 조회
    fun getAllBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null, // 모든 열 선택
            null, null, null, null, null
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

                books.add(Book(title, author, publisher, cover, isbn, pubDate, description, price, status))
            }
            close()
        }
        return books
    }

    // ApiBook을 Book으로 변환하는 메서드
    fun convertApiBookToBook(apiBook: ApiBook): Book {
        return Book(
            title = apiBook.title,
            author = apiBook.author,
            publisher = apiBook.publisher,
            cover = apiBook.cover,
            isbn = apiBook.isbn,
            pubDate = apiBook.pubDate,
            description = apiBook.description,
            priceStandard = apiBook.priceStandard,
            status = apiBook.status
        )
    }
}
