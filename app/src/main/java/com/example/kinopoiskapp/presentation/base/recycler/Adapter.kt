package com.example.kinopoiskapp.presentation.base.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoiskapp.R
import com.example.kinopoiskapp.presentation.ui.main.MovieItemUI

class Adapter(
    private val onMovieClick: (movieId: Int) -> Unit,
    private val onMovieLongClick: (movie: MovieItemUI) -> Unit
) :
    RecyclerView.Adapter<MovieViewHolder>() {


    private val differ = AsyncListDiffer(this, DiffCallback())

    var items: List<MovieItemUI>
        get() = differ.currentList
        set(newItems) = differ.submitList(newItems)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view).apply {
            onMovieClick = this@Adapter.onMovieClick
            onMovieLongClick = this@Adapter.onMovieLongClick
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}