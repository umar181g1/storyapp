package com.umar.storyapp.ui.Detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.umar.storyapp.databinding.ActivityDetailBinding
import com.umar.storyapp.model.ListStoryItem

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDetail()
    }

    private fun setupDetail() {
        val stroy = intent.getParcelableExtra<ListStoryItem>(Extra_Story)
        binding.apply {
            nameDe.text = stroy?.name
            desDe.text = stroy?.description
        }
        Glide.with(this)
            .load(stroy?.photoUrl)
            .into(binding.imageSty)

    }

    companion object {
        const val Extra_Story = "extra_story"
    }
}