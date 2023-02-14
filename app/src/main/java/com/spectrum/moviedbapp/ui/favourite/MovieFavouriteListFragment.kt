package com.spectrum.moviedbapp.ui.favourite

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.spectrum.moviedbapp.R
import com.spectrum.moviedbapp.data.network.model.Results
import com.spectrum.moviedbapp.data.utils.Utils.launchMovieDetailFragment
import com.spectrum.moviedbapp.databinding.FragmentMovieFavouriteBinding
import com.spectrum.moviedbapp.ui.movie.MovieListAdapter
import com.spectrum.moviedbapp.ui.movie.OnItemClickListener
import com.spectrum.moviedbapp.ui.viewmodel.MovieViewModel


class MovieFavouriteListFragment : Fragment(), OnItemClickListener {

    private var favouriteMovies: List<Results>? = null
    private lateinit var binding: FragmentMovieFavouriteBinding
    private val viewModel by activityViewModels<MovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieFavouriteBinding.inflate(inflater, container, false)


        lifecycleScope.launchWhenStarted {
            favouriteMovies = viewModel.getAllFavouriteMovies()
            setAdapter()
        }

        return binding.root
    }


    private fun setAdapter() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewMovieFavourite.layoutManager = linearLayoutManager

        favouriteMovies?.let {
            binding.recyclerviewMovieFavourite.adapter = MovieListAdapter(
                it as ArrayList<Results>,
                this
            )
        }
    }

    override fun onItemClicked(results: Results) {
        results.id?.let { movieId ->
            requireActivity().launchMovieDetailFragment(movieId.toString())
        }
    }

}