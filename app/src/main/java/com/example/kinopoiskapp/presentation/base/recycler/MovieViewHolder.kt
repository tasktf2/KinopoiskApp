package com.example.kinopoiskapp.presentation.base.recycler

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.kinopoiskapp.databinding.ItemMovieBinding
import com.example.kinopoiskapp.presentation.ui.main.MovieItemUI
import com.example.kinopoiskapp.util.requestListener
import java.util.*

class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val binding: ItemMovieBinding by viewBinding()

    var onMovieClick: ((movieId: Int) -> Unit)? = null
    var onMovieLongClick: ((movie: MovieItemUI) -> Unit)? = null

    fun bind(item: MovieItemUI) {
        with(binding) {
            tvMovieName.text = item.movieName
            tvMovieGenresYear.text = buildGenreAndYear(item.movieGenres, item.movieYear)
            Glide.with(view).load(item.moviePreviewUrl).listener(requestListener(progressBar))
                .centerCrop().into(ivMoviePoster)
            ivFavorite.isVisible = item.isFavorite

            movieContainer.setOnClickListener {
                onMovieClick?.invoke(item.movieId)
            }
            movieContainer.setOnLongClickListener {
                onMovieLongClick?.invoke(item)
                ivFavorite.isVisible = !ivFavorite.isVisible
                true
            }
        }
    }

    private fun buildGenreAndYear(genre: String, year: String): String =
        StringBuilder().append(genre.replaceFirstChar {
            if (it.isLowerCase()) {
                it.titlecase(Locale.getDefault())
            } else {
                it.toString()
            }
        }).append(" ($year)").toString()
}