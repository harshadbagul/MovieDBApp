package com.spectrum.moviedbapp.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.spectrum.moviedbapp.R
import com.spectrum.moviedbapp.data.network.model.Results
import com.spectrum.moviedbapp.data.utils.Constants.IMAGE_BASE_PATH
import com.spectrum.moviedbapp.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso


class MovieListAdapter(

	private val mList: ArrayList<Results>,
	) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

	lateinit var binding : ItemMovieBinding

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		 binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		 return ViewHolder(binding.root)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val model = mList[position]

		holder.textViewTitle.text = model.title
		holder.textViewGenere.text = model.title
		holder.textViewReleaseDate.text = holder.itemView.context.getString(R.string.released_on).plus(" ").plus(model.releaseDate)

		val url = IMAGE_BASE_PATH.plus(model.posterPath)
		Picasso.get()
			.load(url)
			.fit()
			.into(holder.imageViewPoster)

	}

	override fun getItemCount(): Int {
		return mList.size
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val textViewTitle: AppCompatTextView = itemView.findViewById(R.id.textview_title)
		val textViewGenere: AppCompatTextView = itemView.findViewById(R.id.textview_genere)
		val textViewReleaseDate: AppCompatTextView = itemView.findViewById(R.id.textview_release_date)
		val imageViewPoster: ImageView = itemView.findViewById(R.id.imageview_poster)
	}
}
