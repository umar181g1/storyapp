package com.umar.storyapp.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.umar.storyapp.R
import com.umar.storyapp.adapter.StoryAdapter
import com.umar.storyapp.databinding.ActivityMainBinding
import com.umar.storyapp.factoryviewmodel.StroyFactoryViewModel
import com.umar.storyapp.model.ListStoryItem
import com.umar.storyapp.model.Result
import com.umar.storyapp.ui.login.LoginActivity
import com.umar.storyapp.ui.story.AddStoryActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupVM()
    }

    private fun setupVM() {
        if (application.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 1)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }

        val factory: StroyFactoryViewModel = StroyFactoryViewModel.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        mainViewModel.isLogin().observe(this) {
            if (!it) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        mainViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                mainViewModel.getStories(token).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                loading(true)
                            }
                            is Result.Success -> {
                                loading(false)
                                val data = result.data.listStory
                                val adapter = StoryAdapter(data as ArrayList<ListStoryItem>)
                                binding.rvStory.adapter = adapter
                            }
                            is Result.Error -> {
                                loading(false)
                                Toast.makeText(this, "ERROR: ${result.error}", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loading(b: Boolean) {
        binding.progressBar.visibility = if (b) View.VISIBLE else View.GONE

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_story -> {
                startActivity(Intent(this, AddStoryActivity::class.java))
                true
            }
            R.id.settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.logout -> {
                mainViewModel.logout()
                true
            }

            else -> true
        }
    }


}