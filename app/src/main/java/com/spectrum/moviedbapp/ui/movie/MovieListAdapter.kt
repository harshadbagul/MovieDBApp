package com.spectrum.moviedbapp.ui.movie

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.spectrum.moviedbapp.R
import com.spectrum.moviedbapp.data.network.model.Results
import com.spectrum.moviedbapp.data.utils.Constants.IMAGE_BASE_PATH
import com.spectrum.moviedbapp.databinding.ItemMovieBinding
import com.spectrum.moviedbapp.databinding.ItemProgressBinding
import com.squareup.picasso.Picasso


class MovieListAdapter(private val mList: ArrayList<Results>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	 val VIEW_TYPE_ITEM_ROW = 1
	 val VIEW_TYPE_ITEM_LOADING = 2


	lateinit var binding : Any

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
		if (viewType == VIEW_TYPE_ITEM_ROW){
			binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
			return ItemViewHolder((binding as ItemMovieBinding).root)
		}
		binding = ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ItemViewHolder((binding as ItemProgressBinding).root)
	}

	@RequiresApi(Build.VERSION_CODES.O)
	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is ItemProgressHolder) {
			holder.progressBar.visibility = View.VISIBLE

		}else if(holder is ItemViewHolder){
			val model = mList[position]

			holder.textViewTitle.text = model.title
			holder.textViewGenere.text = holder.itemView.context.getString(R.string.genres).plus(model.genreNames.joinToString(", "))
			holder.textViewReleaseDate.text = holder.itemView.context.getString(R.string.released_on).plus(" ").plus(model.releaseDate)
			holder.textViewVoteAvg.text = model.voteAverage?.toString()
			holder.textViewVoteCount.text = model.voteCount?.toString()

			val url = IMAGE_BASE_PATH.plus(model.posterPath)
			Picasso.get()
				.load(url)
				.fit()
				.into(holder.imageViewPoster)
		}

	}


	override fun getItemViewType(position: Int): Int {
		return if (mList[position].id == null) VIEW_TYPE_ITEM_LOADING else VIEW_TYPE_ITEM_ROW
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

	class ItemProgressHolder(view: View) : RecyclerView.ViewHolder(view) {
		val progressBar: ProgressBar = itemView.findViewById(R.id.progressBarItem)
	}
}
