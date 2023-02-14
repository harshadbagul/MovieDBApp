package com.spectrum.moviedbapp.ui.search

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
import com.spectrum.moviedbapp.data.utils.Utils.launchMovieDetailFragment
import com.spectrum.moviedbapp.databinding.FragmentSearchBinding
import com.spectrum.moviedbapp.ui.movie.MovieListAdapter
import com.spectrum.moviedbapp.ui.movie.OnItemClickListener
import com.spectrum.moviedbapp.ui.viewmodel.MovieViewModel


class MovieSearchFragment: Fragment(), OnItemClickListener {

    private var mSearchMovies: List<Results>? = null
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by activityViewModels<MovieViewModel>()
    private lateinit var mMovieListAdapter: MovieListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val query = arguments?.getString(Constants.ARG_SEARCH_QUERY) ?: ""
        lifecycleScope.launchWhenStarted {
            viewModel.fetchQueryResult(query)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.searchResultStateFlow
                .collect {
                    when (it) {
                        is MovieState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is MovieState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            mSearchMovies = viewModel.mapGenresToMovie(it.response.results)
                            updateAdapter()
                        }
                        is MovieState.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
        }
    }


    private fun updateAdapter() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewMovieSearch.layoutManager = linearLayoutManager

        mMovieListAdapter = MovieListAdapter(mSearchMovies as ArrayList<Results>, this)
        binding.recyclerviewMovieSearch.adapter = mMovieListAdapter
    }


    override fun onItemClicked(results: Results) {
        results.id?.let { movieId ->
            requireActivity().launchMovieDetailFragment(movieId.toString())
        }
    }
}