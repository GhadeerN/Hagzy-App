package sa.edu.tuwaiq.hagzy.model


import com.google.gson.annotations.SerializedName

data class PhotoModel(
    @SerializedName("farm")
    val farm: Int,
    @SerializedName("height_m")
    val heightM: Int,
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