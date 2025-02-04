package ro.kensierrat.apptemplate.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Date

@Parcelize
data class GenericModel(val id: Int, val genericDate: String, val genericInt: Int, val genericString: String) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true;
        if (other !is GenericModel) return false;

        return (this.id == other.id);
    }

    // needed for "equals" to function properly. auto-generated.
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + genericDate.hashCode()
        result = 31 * result + genericInt
        result = 31 * result + genericString.hashCode()
        return result
    }

    override fun toString(): String {
        return "GenericModel #$id: {\n\tgenericDate: $genericDate\n\tgenericInt: $genericInt\n\tgenericString: $genericString\n}";
    }
}

// keep in mind that if the model is COMPLICATED, a DTO needs to be added for the relation between server and client.
