package com.spectrum.moviedbapp.ui.moviedetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spectrum.moviedbapp.R
import com.spectrum.moviedbapp.data.network.model.MovieDetail

class RecycleAdapter(private val list: MutableList<MovieDetail>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rowView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_parent, parent, false)

        return GroupViewHolder(rowView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val dataList = list[position]


        (holder as GroupViewHolder).apply {
            textViewTitle?.text = dataList.parentTitle
            textViewData?.text = dataList.data
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class GroupViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val textViewTitle = row.findViewById(R.id.textView_title) as TextView?
        val textViewData = row.findViewById(R.id.textview_data) as TextView?
    }

}
