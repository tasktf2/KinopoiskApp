package com.example.kinopoiskapp.presentation.base.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.kinopoiskapp.presentation.ui.main.MovieItemUI

class DiffCallback : DiffUtil.ItemCallback<MovieItemUI>() {
    override fun areItemsTheSame(oldItem: MovieItemUI, newItem: MovieItemUI): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: MovieItemUI, newItem: MovieItemUI): Boolean {
        return oldItem == newItem
    }
}