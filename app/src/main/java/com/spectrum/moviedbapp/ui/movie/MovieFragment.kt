package com.spectrum.moviedbapp.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.spectrum.moviedbapp.data.network.model.Results
import com.spectrum.moviedbapp.data.network.service.MovieState
import com.spectrum.moviedbapp.data.utils.Constants
import com.spectrum.moviedbapp.data.utils.Utils.addFragment
import com.spectrum.moviedbapp.data.utils.Utils.launchMovieDetailFragment
import com.spectrum.moviedbapp.data.utils.Utils.loadMoreItems
import com.spectrum.moviedbapp.databinding.FragmentMovieBinding
import com.spectrum.moviedbapp.ui.moviedetail.MovieDetailFragment
import com.spectrum.moviedbapp.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieFragment(private val movieType: String) : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var mMovieListAdapter: MovieListAdapter
    var mList : ArrayList<Results> = arrayListOf()
    var scrollOutItems:Int = 0
    lateinit var linearLayoutManager: LinearLayoutManager
    var page = 1

    private val viewModel by activityViewModels<MovieViewModel>()

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
        setLayoutManager()

        //load more if last item visible in window
        binding.recyclerviewMovie.loadMoreItems(
            linearLayoutManager
        ) {
            page++
            scrollOutItems = it
            loadMovieList()
        }

        //call Genres & movie list for selected tab
        lifecycleScope.launchWhenStarted {
            viewModel.fetchGenres()
            loadMovieList()
        }

    }

    private fun loadMovieList(){
        // call api for selected tab
        lifecycleScope.launchWhenStarted {
            when(movieType){
                Constants.MovieListType.NOW_PLAYING.toString() -> {  viewModel.fetchNowPlaying(page) }
                Constants.MovieListType.POPULAR.toString() -> {  viewModel.fetchPopular(page) }
                Constants.MovieListType.TOP_RATED.toString() -> {  viewModel.fetchTopRated(page) }
                Constants.MovieListType.UPCOMING.toString() -> {  viewModel.fetchUpcoming(page) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenStarted {
            when(movieType){
                Constants.MovieListType.NOW_PLAYING.toString() -> {  collectNowPlayingFlow() }
                Constants.MovieListType.POPULAR.toString() -> {  collectPopularFlow() }
                Constants.MovieListType.TOP_RATED.toString() -> {  collectTopRatedFlow() }
                Constants.MovieListType.UPCOMING.toString() -> {  collectUpcomingFlow() }
            }
        }
    }

    /**
     *  flow too collect emitted result for NowPlaying
     */
    private suspend fun collectNowPlayingFlow() {
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
                        updateAdapter()
                    }
                    is MovieState.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
    }

    /**
     *  flow too collect emitted result for Popular
     */
    private suspend fun collectPopularFlow() {
        viewModel.popularStateFlow
            .collect {
                when (it) {
                    is MovieState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MovieState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val results = viewModel.mapGenresToMovie(it.response.results)
                        mList.addAll(results)
                        updateAdapter()
                    }
                    is MovieState.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
    }

    /**
     *  flow too collect emitted result for TopRated
     */
    private suspend fun collectTopRatedFlow() {
        viewModel.topRatedStateFlow
            .collect {
                when (it) {
                    is MovieState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MovieState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val results = viewModel.mapGenresToMovie(it.response.results)
                        mList.addAll(results)
                        updateAdapter()
                    }
                    is MovieState.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
    }

    /**
     *  flow too collect emitted result for Upcoming
     */
    private suspend fun collectUpcomingFlow() {
        viewModel.upcomingStateFlow
            .collect {
                when (it) {
                    is MovieState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MovieState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val results = viewModel.mapGenresToMovie(it.response.results)
                        mList.addAll(results)
                        updateAdapter()
                    }
                    is MovieState.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
    }

    /**
     *  movie item click listener event
     */
    override fun onItemClicked(results: Results) {
        results.id?.let { movieId ->
            requireActivity().launchMovieDetailFragment(movieId.toString())
        }
    }


    /**
     * set adapter with list data
     */
    private fun updateAdapter() {
        mMovieListAdapter = MovieListAdapter(mList, this)
        binding.recyclerviewMovie.adapter = mMovieListAdapter
        linearLayoutManager.scrollToPosition(scrollOutItems)
    }

    /**
     * init layout manager
     */
    private fun setLayoutManager() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewMovie.layoutManager = linearLayoutManager
    }

}