package com.spectrum.moviedbapp.ui.movie

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.spectrum.moviedbapp.R
import com.spectrum.moviedbapp.data.network.model.Results
import com.spectrum.moviedbapp.data.utils.Constants.IMAGE_BASE_PATH
import com.spectrum.moviedbapp.data.utils.Utils.getDate
import com.spectrum.moviedbapp.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso


class MovieListAdapter(private val mList: ArrayList<Results>,
					   private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
		val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ItemViewHolder(binding.root)
	}

	@RequiresApi(Build.VERSION_CODES.O)
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		val model = mList[position]
		holder as ItemViewHolder

		holder.textViewTitle.text = model.title
		holder.textViewGenere.text = holder.itemView.context.getString(R.string.genres).plus(model.genreNames.joinToString(", "))
		if (!model.releaseDate.isNullOrEmpty()){
			holder.textViewReleaseDate.text = holder.itemView.context.getString(R.string.released_on).plus(" ").plus(getDate(
				model.releaseDate ?: ""))
		}
		holder.textViewVoteAvg.text = model.voteAverage?.toString()
		holder.textViewVoteCount.text = model.voteCount?.toString()

		val url = IMAGE_BASE_PATH.plus(model.posterPath)
		Picasso.get()
			.load(url)
			.fit()
			.placeholder(R.mipmap.ic_launcher_round)
			.into(holder.imageViewPoster)

		holder.itemView.setOnClickListener {
			onItemClickListener.onItemClicked(model)
		}

	}


	override fun getItemCount(): Int {
		return mList.size
	}

	class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		val textViewTitle: AppCompatTextView = itemView.findViewById(R.id.textview_title)
		val textViewGenere: AppCompatTextView = itemView.findViewById(R.id.textview_genere)
		val textViewReleaseDate: AppCompatTextView = itemView.findViewById(R.id.textview_release_date)
		val imageViewPoster: ImageView = itemView.findViewById(R.id.imageview_poster)
		val textViewVoteCount: AppCompatTextView = itemView.findViewById(R.id.textview_vote_count)
		val textViewVoteAvg: AppCompatTextView = itemView.findViewById(R.id.textview_vote_avg)
	}

}



interface OnItemClickListener{
	fun onItemClicked(results: Results)
}
