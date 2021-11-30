package sa.edu.tuwaiq.hagzy.view.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.squareup.picasso.Picasso
import sa.edu.tuwaiq.hagzy.databinding.ItemLayout2Binding
import sa.edu.tuwaiq.hagzy.databinding.ItemLayoutBinding
import sa.edu.tuwaiq.hagzy.model.Photo
import sa.edu.tuwaiq.hagzy.model.PhotoModel
import sa.edu.tuwaiq.hagzy.view.main.MapViewModel
import sa.edu.tuwaiq.hagzy.view.main.PhotosViewModel
import java.io.ByteArrayOutputStream

var currentPosition: Int = 0

private const val TAG = "PhotosRecyclerViewAdapt"
class PhotosRecyclerViewAdapter(val context: Context, val viewModel: PhotosViewModel) :
    RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotosViewHolder>() {

    // DiffUtil --> will keep old data and just change or add the new one
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    // to give the differ our data (the list)
    fun submitList(list: List<Photo>) {
        differ.submitList(list)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotosRecyclerViewAdapter.PhotosViewHolder {

        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        // Assign the position global variable with the local variable inside the onBind
        currentPosition = position
        val item = differ.currentList[position]

        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    inner class PhotosViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Photo) {
            binding.ownerNameTextView.text = item.ownername
            binding.viewsTextView.text = "Views: ${item.views}"
            Picasso.get().load(item.urlM).into(binding.homeImageView)
            binding.favToggleButton.isChecked = item.isFavorite

            binding.shareImageButton.setOnClickListener {
                val image: Bitmap? = getBitmapFromView(binding.homeImageView)

                val share = Intent(Intent.ACTION_SEND)
                share.type = "image/*"
                share.putExtra(Intent.EXTRA_STREAM, getImageUri(context, image!!))
                context.startActivity(Intent.createChooser(share, "Share via"))
            }

            // Favorite toggle button functionality
            binding.favToggleButton.setOnClickListener {
                item.isFavorite = binding.favToggleButton.isChecked
                Log.d(TAG, "adapter: favorite item: ${item.isFavorite}")
                viewModel.updateFavoritePhoto(item)
            }

        }

        fun getBitmapFromView(view: ImageView): Bitmap? {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }

        fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "title",
                null
            )
            return Uri.parse(path)
        }

    }

}