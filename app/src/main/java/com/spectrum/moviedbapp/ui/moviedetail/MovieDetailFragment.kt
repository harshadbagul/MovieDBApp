package com.spectrum.moviedbapp.ui.moviedetail

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.spectrum.moviedbapp.MainActivity
import com.spectrum.moviedbapp.R
import com.spectrum.moviedbapp.data.network.model.MovieDetail
import com.spectrum.moviedbapp.data.network.model.Results
import com.spectrum.moviedbapp.data.network.service.MovieState
import com.spectrum.moviedbapp.data.utils.Constants
import com.spectrum.moviedbapp.data.utils.Constants.ARG_MOVIE_ID
import com.spectrum.moviedbapp.data.utils.Constants.FAVOURITE
import com.spectrum.moviedbapp.data.utils.Constants.NOT_FAVOURITE
import com.spectrum.moviedbapp.data.utils.Utils
import com.spectrum.moviedbapp.databinding.FragmentMovieDetailBinding
import com.spectrum.moviedbapp.ui.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso

class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private val viewModel by activityViewModels<MovieViewModel>()
    private val listData : MutableList<MovieDetail> = ArrayList()

    private var movieId: String = ""
    private var result : Results? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setToolbar(false)
        movieId = arguments?.getString(ARG_MOVIE_ID) ?: ""

        // fetch moive details using movieId
        lifecycleScope.launchWhenStarted {
            viewModel.fetchMovieDetail(movieId)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.movieDetailStateFlow
                .collect {
                    when (it) {
                        is MovieState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is MovieState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            result = it.response.movieDetail
                            setMovieDetails(result)
                        }
                        is MovieState.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
        }


        //set fav image
        lifecycleScope.launchWhenStarted {
            if (viewModel.isMovieFavourite(movieId)){
                binding.fabFav.tag = FAVOURITE
                binding.fabFav.setImageResource(R.drawable.ic_heart_red)
                setColorTint(FAVOURITE)
            }else{
                binding.fabFav.tag = NOT_FAVOURITE
                binding.fabFav.setImageResource(R.drawable.ic_heart)
                setColorTint(NOT_FAVOURITE)
            }
        }

        binding.fabFav.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                toggleFavourite()
            }
        }

    }

    /**
     * set movie icon, tint color
     */
    private suspend fun toggleFavourite() {
        if (binding.fabFav.tag == FAVOURITE) {
            binding.fabFav.tag = NOT_FAVOURITE
            viewModel.removeMovieFromFavourite(result)
            binding.fabFav.setImageResource(R.drawable.ic_heart)
            setColorTint(NOT_FAVOURITE)
        } else {
            binding.fabFav.tag = FAVOURITE
            viewModel.addToMovieToFavourite(result)
            binding.fabFav.setImageResource(R.drawable.ic_heart_red)
            setColorTint(FAVOURITE)
        }
    }

    private fun setColorTint(fav: Int){
        if (fav == FAVOURITE){
            val red = ContextCompat.getColor(requireContext(), R.color.red)
            binding.fabFav.imageTintList = ColorStateList.valueOf(red)
        }else{
            val black = ContextCompat.getColor(requireContext(), R.color.black)
            binding.fabFav.imageTintList = ColorStateList.valueOf(black)
        }
    }

    /**
     * set all movie details
     */
    private fun setMovieDetails(movieDetail: Results?) {
        //set poster image
        val url = Constants.IMAGE_BASE_PATH.plus(movieDetail?.backdropPath)
        Picasso.get()
            .load(url)
            .fit()
            .into(binding.imageViewPoster)

        //title
        binding.textviewTitle.text = movieDetail?.title

        //tagline
        val movieDetail9 = MovieDetail()
        movieDetail9.parentTitle = getString(R.string.vote)
        val vote = getString(R.string.vote_count)
            .plus(" : ")
            .plus(movieDetail?.voteCount)
            .plus("\n")
            .plus(getString(R.string.vote_avg))
            .plus(" : ")
            .plus(" ").plus(movieDetail?.voteAverage)

        movieDetail9.data = vote
        listData.add(movieDetail9)

        //tagline
        val movieDetail0 = MovieDetail()
        movieDetail0.parentTitle = getString(R.string.tagline)
        movieDetail0.data = movieDetail?.tagline ?: ""
        listData.add(movieDetail0)

        //genres
        val movieDetail1 = MovieDetail()
        movieDetail1.parentTitle = getString(R.string.genre)
        val genres = movieDetail?.genres?.map { it.name }?.joinToString(", ")
        movieDetail1.data = genres ?: ""
        listData.add(movieDetail1)

        //vote average
        val movieDetail2 = MovieDetail()
        movieDetail2.parentTitle = getString(R.string.vote_avg)
        movieDetail2.data = movieDetail?.voteAverage.toString()
        listData.add(movieDetail2)

        //vote count
        val movieDetail3 = MovieDetail()
        movieDetail3.parentTitle = getString(R.string.vote_count)
        movieDetail3.data = movieDetail?.voteCount.toString()
        listData.add(movieDetail3)

        //spoken languages
        val movieDetail4 = MovieDetail()
        movieDetail4.parentTitle = getString(R.string.spoken_languages)
        val languages = movieDetail?.spokenLanguages?.map { it.name }?.joinToString(", ")
        movieDetail4.data = languages ?: ""
        listData.add(movieDetail4)

        //status
        val movieDetail5 = MovieDetail()
        movieDetail5.parentTitle = getString(R.string.status)
        movieDetail5.data = movieDetail?.status ?: ""
        listData.add(movieDetail5)

        //release date
        val movieDetail10 = MovieDetail()
        movieDetail10.parentTitle = getString(R.string.released_on)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            movieDetail10.data = Utils.getDate(movieDetail?.releaseDate ?: "")
        }
        listData.add(movieDetail10)

        //overview
        val movieDetail7 = MovieDetail()
        movieDetail7.parentTitle = getString(R.string.overview)
        movieDetail7.data = movieDetail?.overview ?: ""
        listData.add(movieDetail7)

        //set adapter
        binding.recyclerviewMovieDetail.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewMovieDetail.adapter = RecycleAdapter(listData)
    }

}