package sa.edu.tuwaiq.hagzy.view.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.squareup.picasso.Picasso
import sa.edu.tuwaiq.hagzy.databinding.ItemLayout2Binding
import sa.edu.tuwaiq.hagzy.model.Photo
import sa.edu.tuwaiq.hagzy.model.PhotoModel

var currentPosition: Int = 0

class PhotosRecyclerViewAdapter() : RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotosViewHolder>() {

    // DiffUtil --> will keep old data and just change or add the new one
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this,DIFF_CALLBACK)

    // to give the differ our data (the list)
    fun submitList(list:List<Photo>){
        differ.submitList(list)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotosRecyclerViewAdapter.PhotosViewHolder {

        val binding = ItemLayout2Binding.inflate(LayoutInflater.from(parent.context),parent,false)
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


    class PhotosViewHolder(val binding: ItemLayout2Binding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item:Photo){
            binding.ownerTextView1.text = item.ownername
            binding.viewsTextView1.text = item.views

            Picasso.get().load(item.urlM).into(binding.photoImageView1)
        }

    }

}