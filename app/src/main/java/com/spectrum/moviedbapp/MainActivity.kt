package com.spectrum.moviedbapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
            setSupportActionBar(binding.toolbar)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.action_search)
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

}