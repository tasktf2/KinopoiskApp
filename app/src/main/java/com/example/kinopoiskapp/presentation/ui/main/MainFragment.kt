package com.example.kinopoiskapp.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.kinopoiskapp.R
import com.example.kinopoiskapp.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding()

    private val tabs = listOf(getString(R.string.popular), getString(R.string.favorite))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            vpMovies.adapter = PagerAdapter(parentFragmentManager, lifecycle)

            TabLayoutMediator(tlMovies, vpMovies) { tab, position ->
                tab.text = tabs[position]
            }.attach()
        }
        initQuerySender()
        initHideSearchClick()
    }

    private fun initHideSearchClick() {
        with(binding) {
            ivArrowHide.setOnClickListener {
                etSearch.isVisible = false
                ivArrowHide.isVisible = false
            }
        }
    }

    private fun initQuerySender() {
        binding.ivSearch.setOnClickListener {
            binding.etSearch.isVisible = true
            binding.ivArrowHide.isVisible = true
            val query = binding.etSearch.text?.toString()?.trim().orEmpty()
            parentFragmentManager.setFragmentResult(
                QUERY_REQUEST_KEY,
                bundleOf(QUERY_BUNDLE_KEY to query)
            )
        }
    }

    companion object {
        const val QUERY_BUNDLE_KEY: String = "QUERY_BUNDLE_KEY"
        const val QUERY_REQUEST_KEY: String = "QUERY_REQUEST_KEY"
    }
}