package com.example.chectime

import android.os.Parcel
import android.os.Parcelable
import java.util.Calendar

// Alarm 클래스를 Parcelable로 구현
data class Alarm(
    val id: Int,
    var timeInMillis: Long,
    var label: String,
    var isEnabled: Boolean = true,
    var days: List<Int> // 알람이 울리는 요일 리스트 (1=일요일, 2=월요일, ...)
) : Parcelable {

    // Parcel로부터 Alarm 객체를 생성하는 생성자
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString() ?: "", // null 방지를 위한 처리
        parcel.readByte() != 0.toByte(), // isEnabled 처리
        parcel.createIntArray()?.toList() ?: emptyList() // 요일 처리
    )

    // Alarm 객체를 Parcel에 기록하는 메서드
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id) // 알람 ID
        parcel.writeLong(timeInMillis) // 알람 시간
        parcel.writeString(label) // 알람 레이블
        parcel.writeByte(if (isEnabled) 1 else 0) // isEnabled
        parcel.writeIntArray(days.toIntArray()) // 알람 요일 (1~7)
    }

    // 알람의 내용이 특별한 형식을 가진지 확인하는 메서드 (일반적으로 0을 반환)
    override fun describeContents(): Int = 0

    // 알람 시간 포맷을 반환하는 메서드
    fun getFormattedTime(): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = this@Alarm.timeInMillis
        }
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"

        val displayHour = if (hour == 0) 12 else hour

        return String.format("%02d:%02d %s", displayHour, minute, amPm)
    }

    // Parcelable.Creator를 사용하여 Alarm 객체를 생성하는 객체
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Alarm> = object : Parcelable.Creator<Alarm> {
            override fun createFromParcel(parcel: Parcel): Alarm {
                return Alarm(parcel)
            }

            override fun newArray(size: Int): Array<Alarm?> {
                return arrayOfNulls(size)
            }
        }
    }
}