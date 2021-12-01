package sa.edu.tuwaiq.hagzy.view.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import sa.edu.tuwaiq.hagzy.databinding.ItemLayoutBinding
import sa.edu.tuwaiq.hagzy.model.Photo
import sa.edu.tuwaiq.hagzy.util.ShareImageUtil
import sa.edu.tuwaiq.hagzy.view.DetailsDialogFragment
import sa.edu.tuwaiq.hagzy.view.MainActivity
import java.io.ByteArrayOutputStream

var currentPosition: Int = 0

class PhotosRecyclerViewAdapter(val context: Context) :
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
            //Picasso.get().load(item.urlM).into(binding.homeImageView)

            // Caching with glide
            Glide.with(itemView).load(item.urlM).diskCacheStrategy(
                DiskCacheStrategy.ALL).into(binding.homeImageView)

            binding.shareImageButton.setOnClickListener {
                ShareImageUtil.shareImage(binding.homeImageView, context)
            }

            // Open image details
            binding.homeImageView.setOnClickListener {
                val activity = itemView.context as? MainActivity
                DetailsDialogFragment(item.urlM, item.dateUpload).show(
                    activity!!.supportFragmentManager,
                    "DetailsDialogFragment"
                )
            }

        }

    }

}