package com.example.kinopoiskapp.presentation.ui.description

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.kinopoiskapp.R
import com.example.kinopoiskapp.databinding.FragmentDescriptionBinding
import com.example.kinopoiskapp.di.GlobalDI
import com.example.kinopoiskapp.util.requestListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DescriptionFragment : Fragment(R.layout.fragment_description) {

    private val binding: FragmentDescriptionBinding by viewBinding()

    private val descriptionViewModelFactory: DescriptionViewModelFactory by lazy { GlobalDI.descriptionViewModelFactory }

    private val descriptionViewModel: DescriptionViewModel by viewModels { descriptionViewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getInt(DESCRIPTION_KEY)
        viewLifecycleOwner.lifecycleScope.launch {
            descriptionViewModel.observableStates.collect(::renderState)
        }
        descriptionViewModel.observableStates.onEach(::renderState)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.errorLayout.tvButtonRetry.setOnClickListener {
            binding.groupDescription.isVisible = true
            binding.errorLayout.errorContainer.isVisible = false
            descriptionViewModel.dispatch(DescriptionAction.LoadDescription(movieId!!))
        }

        if (movieId != null) {
            descriptionViewModel.dispatch(DescriptionAction.LoadDescription(movieId))
        } else {
            descriptionViewModel.dispatch(DescriptionAction.ShowError(Exception(getString(R.string.error_movie_id))))
        }
    }

    private fun renderState(state: DescriptionState) {
        if (state.movieDescriptionUI != null) {
            bindDescription(state.movieDescriptionUI)
        }
        with(binding.errorLayout) {
            if (state.error != null) {
                tvError.text = state.error.message
                ivNoInternet.isVisible = true
                tvButtonRetry.isVisible = true
                binding.groupDescription.isVisible = false
            } else {
                binding.groupDescription.isVisible = true
                errorContainer.isVisible = false
            }
        }
    }

    private fun bindDescription(movie: MovieDescriptionUI) {
        with(binding) {
            Glide.with(requireView()).load(movie.moviePosterUrl)
                .listener(requestListener(progressBar)).into(ivMoviePoster)
            tvMovieName.text = movie.movieName
            tvMovieDescription.text = movie.movieDescription
            tvMovieGenres.text = movie.movieGenres.joinToString(", ")
            tvMovieCountries.text = movie.movieCountries.joinToString(", ")
        }
    }

    companion object {
        const val DESCRIPTION_KEY: String = "DESCRIPTION_KEY"
        fun newInstance(movieId: Int): DescriptionFragment = DescriptionFragment().apply {
            arguments = bundleOf(DESCRIPTION_KEY to movieId)
        }
    }
}