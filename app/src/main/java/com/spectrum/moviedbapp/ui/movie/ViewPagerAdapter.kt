package com.spectrum.moviedbapp.ui.movie

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spectrum.moviedbapp.data.utils.Constants

private const val NUM_TABS = 4

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return MovieFragment(Constants.MovieListType.NOW_PLAYING.toString())
            1 -> return MovieFragment(Constants.MovieListType.POPULAR.toString())
            2 -> return MovieFragment(Constants.MovieListType.TOP_RATED.toString())
            3 -> return MovieFragment(Constants.MovieListType.UPCOMING.toString())
        }
        return MovieFragment(Constants.MovieListType.NOW_PLAYING.toString())
    }
}