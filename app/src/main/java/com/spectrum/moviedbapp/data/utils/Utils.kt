package com.spectrum.moviedbapp.data.utils

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.AbsListView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spectrum.moviedbapp.R
import com.spectrum.moviedbapp.ui.moviedetail.MovieDetailFragment
import com.spectrum.moviedbapp.ui.search.MovieSearchFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val INPUT_DATE_FORMAT = "yyyy-mm-dd"
    private const val DISPLAY_DATE_FORMAT = "dd-MMM-yyyy"

    /**
     * get display date from input format
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate(date: String): String {
        val formatter: DateFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
        val formatterOp: DateFormat = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault())

        val date1: Date = date.let { formatter.parse(it) } as Date
        val sdfDate = formatterOp.format(date1)
        return sdfDate ?: ""
    }


    /**
     * generic method to add frament
     * handles backstack logic
     */
    fun addFragment(
        activity: FragmentActivity,
        fragment: Fragment
    ) {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, fragment).addToBackStack(fragment.javaClass.name)
        fragmentTransaction.commit()
    }

    fun FragmentActivity.launchMovieDetailFragment(id: String) {
        val fragment = MovieDetailFragment()
        val args = Bundle()
        args.putString(Constants.ARG_MOVIE_ID, id)
        fragment.arguments = args
        addFragment(this, fragment)
    }

    fun FragmentActivity.launchSearchFragment(query: String) {
        val fragment = MovieSearchFragment()
        val args = Bundle()
        args.putString(Constants.ARG_SEARCH_QUERY, query)
        fragment.arguments = args
        addFragment(this, fragment)
    }

    /**
     *  method to extract the input query & pass to UI layer
     */
    fun searchQueryOption(
        searchViewItem: MenuItem,
        onQuery: (String) -> Unit
    ) {
        val searchView: SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    onQuery.invoke(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    /**
     *  extension method to handle load more logic
     *  when loadMore invokes -> the call fetchMovies with updated page count
     */
    fun RecyclerView.loadMoreItems(
        layoutManager: LinearLayoutManager,
        loadMore: (Int) -> Unit
    ) {
        var isScroll = false
        var scrolledItems: Int

        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScroll = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentItems = layoutManager.childCount
                val totalItems = layoutManager.itemCount
                scrolledItems = layoutManager.findFirstVisibleItemPosition()

                if (isScroll && (currentItems + scrolledItems == totalItems)) {
                    isScroll = false
                    loadMore.invoke(scrolledItems)
                }
            }
        })
    }


}