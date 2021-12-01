package sa.edu.tuwaiq.hagzy.view.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import sa.edu.tuwaiq.hagzy.R
import sa.edu.tuwaiq.hagzy.databinding.FavoriteItemLayoutBinding
import sa.edu.tuwaiq.hagzy.model.Photo

private const val TAG = "FavoriteRecyclerViewAda"

class FavoriteRecyclerViewAdapter() :
    RecyclerView.Adapter<FavoriteRecyclerViewAdapter.FavoriteViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {

        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id

        }
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK )

    fun submitList(list: List<Photo>){
        differ.submitList(list)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteRecyclerViewAdapter.FavoriteViewHolder {

        val binding = FavoriteItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoriteViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {

        val item = differ.currentList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size   }


    class FavoriteViewHolder(val binding: FavoriteItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Photo){
            binding.favoriteToggleButton.isChecked = item.isFavorite
            Glide.with(itemView).load(item.urlM).into(binding.favoriteImageView)
        }

    }

}