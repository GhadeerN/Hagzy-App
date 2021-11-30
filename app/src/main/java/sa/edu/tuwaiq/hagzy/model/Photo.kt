package sa.edu.tuwaiq.hagzy.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class Photo(
    @SerializedName("height_m")
    val heightM: Int,
    @SerializedName("id")
    @PrimaryKey
    val id: String,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("ownername")
    val ownername: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url_m")
    val urlM: String,
    @SerializedName("views")
    val views: String,
    @SerializedName("width_m")
    val widthM: Int,

    var isFavorite: Boolean = false
)