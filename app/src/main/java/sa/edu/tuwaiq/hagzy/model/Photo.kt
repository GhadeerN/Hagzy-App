package sa.edu.tuwaiq.hagzy.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class Photo(
    @SerializedName("farm")
    val farm: Int,
    @SerializedName("height_m")
    val heightM: Int,
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("isfamily")
    val isfamily: Int,
    @SerializedName("isfriend")
    val isfriend: Int,
    @SerializedName("ispublic")
    val ispublic: Int,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("ownername")
    val ownername: String,
    @SerializedName("secret")
    val secret: String,
    @SerializedName("server")
    val server: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url_m")
    val urlM: String,
    @SerializedName("views")
    val views: String,
    @SerializedName("width_m")
    val widthM: Int
)