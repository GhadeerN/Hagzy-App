package sa.edu.tuwaiq.hagzy.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import java.io.ByteArrayOutputStream

object ShareImageUtil {
    fun shareImage(imageView: ImageView, context: Context) {
        val image: Bitmap? = getBitmapFromView(imageView)

        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/*"
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(context, image!!))
        context.startActivity(Intent.createChooser(share, "Share via"))
    }

    fun getBitmapFromView(view: ImageView): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG,100,bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "title",null)
        return Uri.parse(path)
    }
}