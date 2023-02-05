package com.example.kinopoiskapp.presentation.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.kinopoiskapp.R
import com.example.kinopoiskapp.databinding.FragmentMoviesBinding
import com.example.kinopoiskapp.di.GlobalDI
import com.example.kinopoiskapp.presentation.base.recycler.Adapter
import com.example.kinopoiskapp.presentation.ui.description.DescriptionFragment
import com.example.kinopoiskapp.util.hideKeyboard
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MoviesFragment : Fragment(R.layout.fragment_movies) {


    private val binding: FragmentMoviesBinding by viewBinding()

    private val adapter: Adapter by lazy { Adapter(this::onMovieClick, this::onMovieLongClick) }

    private val moviesViewModelFactory by lazy { GlobalDI.moviesViewModelFactory }

    private val moviesViewModel: MoviesViewModel by viewModels { moviesViewModelFactory }

    private val page by lazy { arguments?.get(ARG_PAGE) as Page }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMoviesList.adapter = adapter
        binding.rvMoviesList.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            moviesViewModel.observableStates.collect(::renderState)
        }
        moviesViewModel.observableStates.onEach(::renderState)
            .launchIn(viewLifecycleOwner.lifecycleScope)
        moviesViewModel.observableSideEffects.onEach(::renderEffect)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        loadMovies()
        subscribeToSearch()

        initErrorButton()

    }

    private fun initErrorButton() {
        binding.errorLayout.tvButtonRetry.setOnClickListener {
            binding.rvMoviesList.isVisible = true
            binding.errorLayout.errorContainer.isVisible = false
            when (page) {
                Page.POPULAR -> moviesViewModel.dispatch(MoviesAction.LoadPopularMovies)
                Page.FAVORITE -> moviesViewModel.dispatch(MoviesAction.LoadFavoriteMovies)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadMovies()
    }

    private fun renderState(state: MoviesState) {
        if (state.visibleMovies != null && state.visibleMovies.isNotEmpty()) {
            adapter.items= listOf()
            adapter.items = state.visibleMovies
        }
        with(binding.errorLayout) {
            if (state.movies != null) {
                adapter.items = state.movies
            }
            if (state.error != null) {
                tvError.text = state.error.message
                if (page == Page.FAVORITE && state.error.message == getString(R.string.no_favorite_movies)) {
                    adapter.items = listOf()
                    ivNoInternet.isInvisible = true
                    tvButtonRetry.isInvisible = true
                    errorContainer.isVisible = true
                } else {
                    ivNoInternet.isVisible = true
                    tvButtonRetry.isVisible = true
                    binding.rvMoviesList.isVisible = false

                }
            } else {
                binding.rvMoviesList.isVisible = true
            }
            errorContainer.isVisible = !binding.rvMoviesList.isVisible
        }
    }

    private fun renderEffect(effect: MoviesEffect) {
        when (effect) {
            is MoviesEffect.ShowDescription -> {
                parentFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.main_container, DescriptionFragment.newInstance(effect.movieId))
                    .commit()
            }
        }
    }

    private fun loadMovies() {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork


        if (network != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            ) {
                // The device is connected to a cellular network with internet access
                when (page) {
                    Page.POPULAR -> moviesViewModel.dispatch(MoviesAction.LoadPopularMovies)

                    Page.FAVORITE -> moviesViewModel.dispatch(MoviesAction.LoadFavoriteMovies)
                }
            } else {
                // The device is either not connected to a cellular network, or it is connected to a cellular network without internet access
                moviesViewModel.dispatch(MoviesAction.ShowError(Exception(getString(R.string.no_internet_connection))))
                if (page == Page.FAVORITE) {
                    moviesViewModel.dispatch(MoviesAction.LoadFavoriteMovies)
                }
            }
        } else {
            // The device is not connected to any network
            moviesViewModel.dispatch(MoviesAction.ShowError(Exception(getString(R.string.you_have_no_internet_connection))))
            if (page == Page.FAVORITE) {
                moviesViewModel.dispatch(MoviesAction.LoadFavoriteMovies)
            }

        }
    }

    private fun subscribeToSearch() {
        parentFragmentManager.setFragmentResultListener(
            MainFragment.QUERY_REQUEST_KEY,
            this
        ) { _, bundle ->
            val query = bundle.getString(MainFragment.QUERY_BUNDLE_KEY).orEmpty()
            moviesViewModel.dispatch(MoviesAction.Search(query))
            hideKeyboard()
        }
    }


    private fun onMovieClick(movieId: Int) {
        moviesViewModel.dispatch(MoviesAction.LoadDescription(movieId))
    }

    private fun onMovieLongClick(movie: MovieItemUI) {
        adapter.items = adapter.items.map { movieItemUI ->
            when (movieItemUI.movieId) {
                movie.movieId -> movieItemUI.copy(isFavorite = !movieItemUI.isFavorite)
                else -> movieItemUI
            }
        }

        if (movie.isFavorite) {
            moviesViewModel.dispatch(MoviesAction.DeleteFavoriteMovie(movie = movie))
        } else {
            moviesViewModel.dispatch(MoviesAction.InsertFavoriteMovie(movie.copy(isFavorite = true)))
        }
    }

    companion object {
        private const val ARG_PAGE = "ARG_PAGE"

        fun newInstance(page: Page): Fragment = MoviesFragment().apply {
            arguments = bundleOf(ARG_PAGE to page)
        }
    }
}