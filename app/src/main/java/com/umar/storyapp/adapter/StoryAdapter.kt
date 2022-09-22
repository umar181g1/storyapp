package com.umar.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umar.storyapp.databinding.ItemStoryRowBinding
import com.umar.storyapp.model.ListStoryItem
import com.umar.storyapp.ui.Detail.DetailActivity

class StoryAdapter(private val listStoryItem: ArrayList<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemStoryRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listStoryItem[position])
    }

    override fun getItemCount(): Int = listStoryItem.size

    class ListViewHolder(private var binding: ItemStoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.imgPhoto)
            binding.apply {
                txtName.text = story.name
                txtDeskripsi.text = story.description
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.Extra_Story, story)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgPhoto, "image"),
                        Pair(binding.txtName, "name"),
                        Pair(binding.txtDeskripsi, "desc")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}