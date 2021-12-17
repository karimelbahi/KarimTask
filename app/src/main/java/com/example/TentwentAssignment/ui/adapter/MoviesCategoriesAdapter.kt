package com.example.TentwentAssignment.ui.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.TentwentAssignment.data.remote.response.movie.detail.Genre
import com.example.TentwentAssignment.databinding.RowMoviesCategoriesLayoutBinding

class MoviesCategoriesAdapter(
    private val context: Context,
    private val iMovie: IMovie,
    private val list: List<Genre>
) : RecyclerView.Adapter<MoviesCategoriesAdapter.ViewHolder>() {

    private val TAG = "MovieAdapter"

    private var wordListFiltered = mutableListOf<Genre>()

    init {
        wordListFiltered = ArrayList(list)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding = RowMoviesCategoriesLayoutBinding.inflate(LayoutInflater.from(context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(viewHolder,i)
    }

    override fun getItemCount(): Int {
        return wordListFiltered.size
    }

    inner class ViewHolder(val binding: RowMoviesCategoriesLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.rowMovie.setOnClickListener{
                iMovie.onMovieClick(list[absoluteAdapterPosition],absoluteAdapterPosition)
            }

        }

        fun bind(viewHolder: ViewHolder, position: Int){
            with(viewHolder.binding){
                with(wordListFiltered[position]) {
                    rowTitle.text = name

                }
            }
        }
    }

    fun search(query: String?) {
        val newList =
            if (query.isNullOrEmpty()) {
                list
            } else list.filter { item ->
                item.name.contains(query, true)
            }
        wordListFiltered.clear()
        wordListFiltered.addAll(newList)
        notifyDataSetChanged()
    }

    interface IMovie {
        fun onMovieClick(item: Genre, position: Int)
    }
}