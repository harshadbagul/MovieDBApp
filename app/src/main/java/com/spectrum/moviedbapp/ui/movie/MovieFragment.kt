package com.spectrum.moviedbapp.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spectrum.moviedbapp.data.network.model.Results
import com.spectrum.moviedbapp.data.network.service.MovieState
import com.spectrum.moviedbapp.databinding.FragmentMovieBinding
import com.spectrum.moviedbapp.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var mMovieListAdapter: MovieListAdapter
    var mList : ArrayList<Results> = arrayListOf()

    private val viewModel by activityViewModels<MovieViewModel>()

    var isScrolling = false
    var currentItems = 0
    var totalItems:Int = 0
    var scrollOutItems:Int = 0
    val itemSize = 20
    lateinit var linearLayoutManager: LinearLayoutManager
    var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initScrollListener()

        lifecycleScope.launchWhenStarted {
            viewModel.fetchGenres()
            loadMovieList()
        }

        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewMovie.layoutManager = linearLayoutManager
    }

    private fun loadMovieList(){
        lifecycleScope.launchWhenStarted {
            viewModel.fetchNowPlaying(page)
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenStarted {
            viewModel.nowPlayingStateFlow
                .collect {
                    when (it) {
                        is MovieState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is MovieState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val results = viewModel.mapGenresToMovie(it.response.results)
                            mList.addAll(results)
                            println("results size : ${mList.size}")
                            println("page : ${page}")
                            updateAdapter()
                        }
                        is MovieState.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
        }

    }

    private fun addNullItemEnd(){
        //Remove previous null element if any
        mList = mList.filterNot { it.id == null } as ArrayList<Results>
        // adding null element to handle load more feature
        mList.add(Results(id = null))
    }


    private fun initScrollListener() {
        binding.recyclerviewMovie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = linearLayoutManager.childCount
                totalItems = linearLayoutManager.itemCount
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition()

                if(isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false
                    page++
                    loadMovieList()
                }
            }
        })
    }


    private fun updateAdapter() {
        mMovieListAdapter = MovieListAdapter(mList)
        binding.recyclerviewMovie.adapter = mMovieListAdapter
        linearLayoutManager.scrollToPosition(mList.size - itemSize)
    }

}