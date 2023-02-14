package com.spectrum.moviedbapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.spectrum.moviedbapp.data.utils.Utils
import com.spectrum.moviedbapp.data.utils.Utils.launchSearchFragment
import com.spectrum.moviedbapp.data.utils.Utils.searchQueryOption
import com.spectrum.moviedbapp.databinding.ActivityMainBinding
import com.spectrum.moviedbapp.ui.favourite.MovieFavouriteListFragment
import com.spectrum.moviedbapp.ui.movie.ViewPagerAdapter
import com.spectrum.moviedbapp.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setToolbar()

        setUpTabs()

    }

    /**
     * set up NO of tabs using viewpager2
     */
    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabs = resources.getStringArray(R.array.tabs)
            tab.text = tabs[position]
        }.attach()
    }

    /**
     * set up toolbar
     */
    fun setToolbar(visible: Boolean = true){
        if (visible){
            binding.toolbar.setNavigationIcon(R.drawable.ic_back)
            binding.toolbar.setNavigationOnClickListener {
                this.finish()
            }
            setSupportActionBar(binding.toolbar)
        }
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchViewItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        configureSearchView(searchView)

        searchQueryOption(searchViewItem) { query ->
            launchSearchFragment(query)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favourite -> {
                Utils.addFragment(this, MovieFavouriteListFragment())
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * set search view color, text
     */
    private fun configureSearchView(searchView: SearchView) {
        //search button
        val icon: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_button)
        icon.drawable.setTint(ContextCompat.getColor(this, R.color.white))

        //input text
        val editText: EditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        editText.setTextColor(ContextCompat.getColor(this, R.color.black))
        editText.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

        //close button
        val iconClose: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        iconClose.drawable.setTint(ContextCompat.getColor(this, R.color.white))
    }
}