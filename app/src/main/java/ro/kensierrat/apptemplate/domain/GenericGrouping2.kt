package ro.kensierrat.apptemplate.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenericGrouping2 (val genericPrefix: String, val genericInt: Int) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GenericGrouping

        if (genericPrefix != other.genericMonth) return false
        if (genericInt != other.genericInt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = genericPrefix.hashCode()
        result = 31 * result + genericInt.hashCode()
        return result
    }

    override fun toString(): String {
        return "GenericGrouping(genericMonth='$genericPrefix', genericInt='$genericInt')"
    }

}
