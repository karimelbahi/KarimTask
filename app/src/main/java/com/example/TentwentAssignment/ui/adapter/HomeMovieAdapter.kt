package com.example.TentwentAssignment.ui.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.TentwentAssignment.R
import com.example.TentwentAssignment.data.remote.response.movie.Result
import com.example.TentwentAssignment.databinding.RowMovieLayoutBinding
import com.example.TentwentAssignment.util.Constants

class HomeMovieAdapter(
    private val context: Context,
    private val iMovie: IMovie,
    private val list: List<Result>
) : RecyclerView.Adapter<HomeMovieAdapter.ViewHolder>() {

    private val TAG = "MovieAdapter"

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding = RowMovieLayoutBinding.inflate(LayoutInflater.from(context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(viewHolder,i)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: RowMovieLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.rowMovie.setOnClickListener{
                iMovie.onMovieClick(list[absoluteAdapterPosition],absoluteAdapterPosition)
            }

        }

        fun bind(viewHolder: ViewHolder, position: Int){
            with(viewHolder.binding){
                with(list[position]) {
                    rowTitle.text = title

                    Glide.with(context)
                        .load(Constants.IMAGE_URL + poster_path)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(rowMovieImage)
                }
            }
        }
    }

    interface IMovie {
        fun onMovieClick(item: Result, position: Int)
    }
}