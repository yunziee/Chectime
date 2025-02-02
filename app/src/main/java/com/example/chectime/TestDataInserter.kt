package com.example.chectime

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_ISBN
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_TITLE
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_AUTHOR
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_PUBLISHER
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_COVER
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_PUB_DATE
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_DESCRIPTION
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_PRICE
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_STATUS
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_MEMO
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_START_DATE
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_END_DATE
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_RATING
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_CURRENT_PAGE
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_TIMER_DATE
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_TIMER_DURATION
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_TIMER_ID
import com.example.chectime.BookshelfDatabaseHelper.Companion.COLUMN_TOTAL_PAGES
import com.example.chectime.BookshelfDatabaseHelper.Companion.TABLE_NAME
import com.example.chectime.BookshelfDatabaseHelper.Companion.TIMER_TABLE_NAME

class TestDataInserter(private val db: SQLiteDatabase) {

    fun addTestBooks() {
        val testBooks = listOf(
            ContentValues().apply {
                put(COLUMN_ISBN, "K442934507")
                put(COLUMN_TITLE, "모우어")
                put(COLUMN_AUTHOR, "천선란 (지은이)")
                put(COLUMN_PUBLISHER, "문학동네")
                put(
                    COLUMN_COVER,
                    "https://image.aladin.co.kr/product/35099/68/coversum/k442934507_1.jpg"
                )
                put(COLUMN_PUB_DATE, "2024-11-15")
                put(
                    COLUMN_DESCRIPTION,
                    "2019년 『천 개의 파랑』으로 한국과학문학상 장편 대상을 수상하며 혜성같이 등장해 한국 SF의 눈부신 미래를 만들고 있는 작가 천선란 의 세 번째. 『노랜드』 이후 2년 만에 묶는 소설집으로 미발표작 두 편을 포함해 2020년부터 2024년까지 쓴 단편 여덟 편이 수록되어 있다."
                )
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
                put(COLUMN_ISBN, "899098257X")
                put(COLUMN_TITLE, "가면 산장 살인 사건")
                put(COLUMN_AUTHOR, "히가시노 게이고 (지은이), 김난주 (옮긴이)")
                put(COLUMN_PUBLISHER, "재인")
                put(
                    COLUMN_COVER,
                    "https://image.aladin.co.kr/product/4745/51/coversum/899098257x_2.jpg"
                )
                put(COLUMN_PUB_DATE, "2014-09-26")
                put(
                    COLUMN_DESCRIPTION,
                    "히가시노 게이고 장편소설. 아버지 소유의 별장 근처 작은 교회에서 결혼식을 올리는 것이 꿈이었던 도모미는 그 꿈이 이루어질 날을 불과 일주일 앞두고 식장으로 예정된 교회에 다녀오다가 운전 부주의로 인해 가드레일을 들이받고 절벽에서 추락해 사망한다."
                )
                put(COLUMN_PRICE, "16800")
                put(COLUMN_STATUS, "read")
                put(COLUMN_MEMO, "추리 명작임...")
                put(COLUMN_START_DATE, "2025-01-03")
                put(COLUMN_END_DATE, "2025-01-06")
                put(COLUMN_RATING, 4.0)
            },
            ContentValues().apply {
                put(COLUMN_ISBN, "K502837053")
                put(COLUMN_TITLE, "칵테일, 러브, 좀비 (리커버)")
                put(COLUMN_AUTHOR, "조예은 (지은이)")
                put(COLUMN_PUBLISHER, "안전가옥")
                put(
                    COLUMN_COVER,
                    "https://image.aladin.co.kr/product/29543/72/coversum/k502837053_1.jpg"
                )
                put(COLUMN_PUB_DATE, "2020-04-13")
                put(COLUMN_DESCRIPTION, "")
                put(COLUMN_PRICE, "13000")
                put(COLUMN_STATUS, "read")
                put(COLUMN_MEMO, "물과 숲의 이야기가 가장 좋았음!!")
                put(COLUMN_START_DATE, "2025-01-14")
                put(COLUMN_END_DATE, "2025-01-15")
                put(COLUMN_RATING, 4.5)
            },
            ContentValues().apply {
                put(COLUMN_ISBN, "K222931917")
                put(COLUMN_TITLE, "음악소설집 音樂小說集")
                put(COLUMN_AUTHOR, "김애란, 김연수, 윤성희, 은희경, 편혜영 (지은이)")
                put(COLUMN_PUBLISHER, "프란츠")
                put(
                    COLUMN_COVER,
                    "https://image.aladin.co.kr/product/34130/64/coversum/k222931917_2.jpg"
                )
                put(COLUMN_PUB_DATE, "2024-06-13")
                put(
                    COLUMN_DESCRIPTION,
                    "음악 전문 출판사 ‘프란츠’가 선보이는 첫 소설 앤솔러지 『음악소설집音樂小說集』. 한국문학을 대표하는 다섯 소설가인 김애란, 김연수, 윤성희, 은희경, 편혜영의 음악을 테마로 한 신작 단편소설이 실렸다. 일상에서 마주치는 다채로운 음악의 장면들을 섬세하게 포착한 다섯 개의 이야기를 통해, 삶의 어떤 순간이라도 음악이 될 수 있다는 것을 깨닫게 한다."
                )
                put(COLUMN_PRICE, "18000")
                put(COLUMN_STATUS, "read")
                put(COLUMN_START_DATE, "2025-01-30")
                put(COLUMN_END_DATE, "2025-02-01")
            },
            ContentValues().apply {
                put(COLUMN_ISBN, "8954699375")
                put(COLUMN_TITLE, "눈부신 안부")
                put(COLUMN_AUTHOR, "백수린 (지은이)")
                put(COLUMN_PUBLISHER, "문학동네")
                put(
                    COLUMN_COVER,
                    "https://image.aladin.co.kr/product/31688/62/coversum/8954699375_2.jpg"
                )
                put(COLUMN_PUB_DATE, "2023-05-24")
                put(
                    COLUMN_DESCRIPTION,
                    "소설가 백수린의 장편소설. 2011년 데뷔한 이래 세 권의 소설집과 한 권의 중편소설, 짧은 소설들과 산문을 발표하는 동안 조급해하지 않고 장편의 그릇에 담고 싶은 이야기를 기다린 그가 등단 12년 만에 펴내는 첫 장편소설이다."
                )
                put(COLUMN_PRICE, "16000")
                put(COLUMN_STATUS, "read")
                put(COLUMN_START_DATE, "2025-01-09")
                put(COLUMN_END_DATE, "2025-01-11")
            },
            ContentValues().apply {
                put(COLUMN_ISBN, "8954673104")
                put(COLUMN_TITLE, "여름의 빌라")
                put(COLUMN_AUTHOR, "백수린 (지은이)")
                put(COLUMN_PUBLISHER, "문학동네")
                put(
                    COLUMN_COVER,
                    "https://image.aladin.co.kr/product/24489/98/coversum/8954673104_1.jpg"
                )
                put(COLUMN_PUB_DATE, "2020-07-07")
                put(
                    COLUMN_DESCRIPTION,
                    "소설집 &lt;폴링 인 폴&gt; &lt;참담한 빛&gt;, 중편소설 &lt;친애하고 친애하는&gt;을 통해 한국문학을 대표하는 작가로 자리매김한 백수린. 대체 불가능한 아름다운 문장과 섬세한 플롯으로 문단과 독자의 신뢰를 한몸에 받아온 백수린이 세번째 소설집 &lt;여름의 빌라&gt;를 선보인다."
                )
                put(COLUMN_PRICE, "14800")
                put(COLUMN_STATUS, "read")
                put(COLUMN_START_DATE, "2025-01-12")
                put(COLUMN_END_DATE, "2025-01-26")
            },
            ContentValues().apply {
                put(COLUMN_ISBN, "8954651135")
                put(COLUMN_TITLE, "흰 - 2024 노벨문학상 수상작가, 한강 소설")
                put(COLUMN_AUTHOR, "한강 (지은이), 최진혁 (사진)")
                put(COLUMN_PUBLISHER, "문학동네")
                put(
                    COLUMN_COVER,
                    "https://image.aladin.co.kr/product/14322/3/coversum/8954651135_3.jpg"
                )
                put(COLUMN_PUB_DATE, "2018-04-25")
                put(
                    COLUMN_DESCRIPTION,
                    "2018년 봄, 한강 작가의 소설 흰을 새롭게 선보인다. 이 년 전 오월에 세상에 나와 빛의 겹겹 오라기로 둘러싸인 적 있던 그 <흰>에 새 옷을 입히게 된 건 소설 발간에 즈음해 행했던 작가의 퍼포먼스가 글과 함께 배었으면 하는 바람에서였다."
                )
                put(COLUMN_PRICE, "14500")
                put(COLUMN_STATUS, "reading")
                put(COLUMN_MEMO, "2월 안으로는 꼭 전부 읽어야지")
                put(COLUMN_START_DATE, "2025-01-29")
                put(COLUMN_CURRENT_PAGE, 85)
                put(COLUMN_TOTAL_PAGES, 196)
            },
            ContentValues().apply {
                put(COLUMN_ISBN, "K632036994")
                put(COLUMN_TITLE, "설자은, 불꽃을 쫓다")
                put(COLUMN_AUTHOR, "정세랑 (지은이)")
                put(COLUMN_PUBLISHER, "문학동네")
                put(
                    COLUMN_COVER,
                    "https://image.aladin.co.kr/product/35598/7/coversum/k632036994_1.jpg"
                )
                put(COLUMN_PUB_DATE, "2025-01-20")
                put(
                    COLUMN_DESCRIPTION,
                    "‘설자은 시리즈’는 정세랑이 펴낸 첫 역사소설이자 첫 추리소설, 그리고 첫 시리즈이다. 통일신라시대의 수도 금성을 배경으로, 집사부 대사 설자은이 주변에서 일어나는 미스터리한 사건들을 해결해나가는 이야기를 담고 있다."
                )
                put(COLUMN_PRICE, "16800")
                put(COLUMN_STATUS, "reading")
            },
            ContentValues().apply {
                put(COLUMN_ISBN, "8936437542")
                put(COLUMN_TITLE, "디디의 우산 - 황정은 연작소설")
                put(COLUMN_AUTHOR, "황정은 (지은이)")
                put(COLUMN_PUBLISHER, "창비")
                put(
                    COLUMN_COVER,
                    "https://image.aladin.co.kr/product/17729/72/coversum/8936437542_1.jpg"
                )
                put(COLUMN_PUB_DATE, "2019-01-20")
                put(
                    COLUMN_DESCRIPTION,
                    "한국문학의 대표주자 중 한 사람인 황정은 작가의 신간. 김유정문학상 수상작 'd'(발표 당시 '웃는 남자')와 「문학3」 웹 연재 당시 뜨거운 호응을 얻었던 '아무것도 말할 필요가 없다'. 인물과 서사는 다르지만 서로 묘하고도 아름답게 공명하는 이 두 중편이 연작소설 &lt;dd의 우산&gt;으로 묶였다."
                )
                put(COLUMN_PRICE, "15000")
                put(COLUMN_STATUS, "reading")
            },
            ContentValues().apply {
                put(COLUMN_ISBN, "K462930652")
                put(COLUMN_TITLE, "입속 지느러미")
                put(COLUMN_AUTHOR, "조예은 (지은이)")
                put(COLUMN_PUBLISHER, "한겨레출판")
                put(
                    COLUMN_COVER,
                    "https://image.aladin.co.kr/product/34002/54/coversum/k462930652_1.jpg"
                )
                put(COLUMN_PUB_DATE, "2024-05-30")
                put(
                    COLUMN_DESCRIPTION,
                    "제2회 황금가지 타임리프 소설 공모전에서 〈오버랩 나이프, 나이프〉로 우수상을 받으며 작품 활동을 시작한 조예은 작가가 신작 소설《입속 지느러미》로 야심 차게 돌아왔다. 인어 이야기와 세이렌 신화를 결합해 잔혹하지만 아련하고 서글프지만 사랑스러운 서사로 독자를 새롭게 만난다."
                )
                put(COLUMN_PRICE, "15000")
                put(COLUMN_STATUS, "toRead")
                put(COLUMN_MEMO, "여름 장마철에 꼭 읽고 싶은 책")
            }
        )

        for (values in testBooks) {
            db.insert(TABLE_NAME, null, values)
        }

    }

    fun addTestTimer() {
        val testTimers = listOf(
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 1)
                put(COLUMN_ISBN, "899098257X")
                put(COLUMN_TITLE, "가면 산장 살인 사건")
                put(COLUMN_TIMER_DATE, "2025-01-03")
                put(COLUMN_TIMER_DURATION, 2120)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 2)
                put(COLUMN_ISBN, "899098257X")
                put(COLUMN_TITLE, "가면 산장 살인 사건")
                put(COLUMN_TIMER_DATE, "2025-01-06")
                put(COLUMN_TIMER_DURATION, 2816)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 3)
                put(COLUMN_ISBN, "8954699375")
                put(COLUMN_TITLE, "눈부신 안부")
                put(COLUMN_TIMER_DATE, "2025-01-09")
                put(COLUMN_TIMER_DURATION, 1284)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 4)
                put(COLUMN_ISBN, "8954699375")
                put(COLUMN_TITLE, "눈부신 안부")
                put(COLUMN_TIMER_DATE, "2025-01-10")
                put(COLUMN_TIMER_DURATION, 1431)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 5)
                put(COLUMN_ISBN, "8954699375")
                put(COLUMN_TITLE, "눈부신 안부")
                put(COLUMN_TIMER_DATE, "2025-01-11")
                put(COLUMN_TIMER_DURATION, 999)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 6)
                put(COLUMN_ISBN, "8954673104")
                put(COLUMN_TITLE, "여름의 빌라")
                put(COLUMN_TIMER_DATE, "2025-01-11")
                put(COLUMN_TIMER_DURATION, 354)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 7)
                put(COLUMN_ISBN, "K502837053")
                put(COLUMN_TITLE, "칵테일, 러브, 좀비 (리커버)")
                put(COLUMN_TIMER_DATE, "2025-01-14")
                put(COLUMN_TIMER_DURATION, 214)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 8)
                put(COLUMN_ISBN, "K502837053")
                put(COLUMN_TITLE, "칵테일, 러브, 좀비 (리커버)")
                put(COLUMN_TIMER_DATE, "2025-01-15")
                put(COLUMN_TIMER_DURATION, 1657)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 9)
                put(COLUMN_ISBN, "8954673104")
                put(COLUMN_TITLE, "여름의 빌라")
                put(COLUMN_TIMER_DATE, "2025-01-18")
                put(COLUMN_TIMER_DURATION, 2100)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 10)
                put(COLUMN_ISBN, "K442934507")
                put(COLUMN_TITLE, "모우어")
                put(COLUMN_TIMER_DATE, "2025-01-21")
                put(COLUMN_TIMER_DURATION, 2976)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 11)
                put(COLUMN_ISBN, "K442934507")
                put(COLUMN_TITLE, "모우어")
                put(COLUMN_TIMER_DATE, "2025-01-24")
                put(COLUMN_TIMER_DURATION, 2564)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 12)
                put(COLUMN_ISBN, "8954673104")
                put(COLUMN_TITLE, "여름의 빌라")
                put(COLUMN_TIMER_DATE, "2025-01-26")
                put(COLUMN_TIMER_DURATION, 1043)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 13)
                put(COLUMN_ISBN, "8954651135")
                put(COLUMN_TITLE, "흰 - 2024 노벨문학상 수상작가, 한강 소설")
                put(COLUMN_TIMER_DATE, "2025-01-29")
                put(COLUMN_TIMER_DURATION, 1867)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 14)
                put(COLUMN_ISBN, "8954651135")
                put(COLUMN_TITLE, "흰 - 2024 노벨문학상 수상작가, 한강 소설")
                put(COLUMN_TIMER_DATE, "2025-01-29")
                put(COLUMN_TIMER_DURATION, 461)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 15)
                put(COLUMN_ISBN, "K222931917")
                put(COLUMN_TITLE, "음악소설집 音樂小說集")
                put(COLUMN_TIMER_DATE, "2025-01-30")
                put(COLUMN_TIMER_DURATION, 1764)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 16)
                put(COLUMN_ISBN, "K222931917")
                put(COLUMN_TITLE, "음악소설집 音樂小說集")
                put(COLUMN_TIMER_DATE, "2025-02-01")
                put(COLUMN_TIMER_DURATION, 1655)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 17)
                put(COLUMN_ISBN, "K632036994")
                put(COLUMN_TITLE, "설자은, 불꽃을 쫓다")
                put(COLUMN_TIMER_DATE, "2025-02-01")
                put(COLUMN_TIMER_DURATION, 618)
            },
            ContentValues().apply {
                put(COLUMN_TIMER_ID, 18)
                put(COLUMN_ISBN, "K632036994")
                put(COLUMN_TITLE, "설자은, 불꽃을 쫓다")
                put(COLUMN_TIMER_DATE, "2025-02-02")
                put(COLUMN_TIMER_DURATION, 1410)
            }
        )

        for (values in testTimers) {
            db.insert(TIMER_TABLE_NAME, null, values)
        }
    }
}

