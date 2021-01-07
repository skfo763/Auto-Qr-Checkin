package com.skfo763.base.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @see <a href="https://stackoverflow.com/questions/65407003/android-error-non-static-type-variable-t-cannot-be-referenced-from-a-static-co>코틀린 자체 버그</a>
 * Kotlin 1.4.20 업데이트 이후, kotlin-android-extensions 플러그인을 kotlin-parcelize 플러그인으로 교체하던 중,
 * <T: Parcelize> 형태의 제네릭스를 @Parcelize 어노테이션이 인식하지 못해 컴파일 에러가 났습니다.
 * 추후 코틀린 버전이 올라가고 이 버그가 고쳐지면 @Parcelize 어노테이션을 붙이고 보일러플레이트 코드를 삭제하세요.
 */
// @Parcelize
data class ServiceResult <T: Parcelable> (
    var code: Int = 0,
    var message: String = "",
    var data: T? = null
): Parcelable {

    constructor(parcel: Parcel) : this() {
        code = parcel.readInt()
        message = parcel.readString() ?: ""
        val className = parcel.readString()
        data = className?.let { parcel.readParcelable(Class.forName(it).classLoader) }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeString(message)
        parcel.writeString(data?.javaClass?.name ?: "")
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ServiceResult<Parcelable>> {
        override fun createFromParcel(parcel: Parcel): ServiceResult<Parcelable> {
            return ServiceResult(parcel)
        }

        override fun newArray(size: Int): Array<ServiceResult<Parcelable>?> {
            return arrayOfNulls(size)
        }
    }
}