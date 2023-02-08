package com.spectrum.moviedbapp.ui.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.spectrum.moviedbapp.R
import com.spectrum.moviedbapp.data.network.model.Results
import com.spectrum.moviedbapp.data.network.service.MovieState
import com.spectrum.moviedbapp.databinding.FragmentMovieBinding
import com.spectrum.moviedbapp.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var mMovieListAdapter: MovieListAdapter

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

        lifecycleScope.launchWhenStarted {
            viewModel.fetchNowPlaying(1)
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenStarted {
            viewModel.nowPlayingStateFlow
                .collect {
                    when (it) {
                        is MovieState.Loading -> {
                            //binding.llProgressBar.linearLayout.visibility = View.VISIBLE
                        }
                        is MovieState.Success -> {
                           // binding.llProgressBar.linearLayout.visibility = View.GONE
                            updateAdapter(it.response.results)
                        }
                        is MovieState.Error -> {
                           // binding.llProgressBar.linearLayout.visibility = View.GONE
                            val errorMessage = it.error?.info
                            //requireContext().showErrorDialog(message = errorMessage)
                        }
                    }
                }
        }

    }

    private fun updateAdapter(results: ArrayList<Results>) {
        mMovieListAdapter = MovieListAdapter(results)
        binding.recyclerviewMovie.adapter = mMovieListAdapter
    }

}