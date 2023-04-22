package com.mry.userstory.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mry.userstory.data.CustomResult
import com.mry.userstory.data.response.ListStoryItem
import com.mry.userstory.databinding.FragmentHomeBinding
import com.mry.userstory.utils.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.stories.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is CustomResult.Loading -> showLoading(true)
                    is CustomResult.Success -> {
                        showLoading(false)
                        val storyData = result.data.listStory
                        @Suppress("UNCHECKED_CAST")
                        setStoriesData(storyData as List<ListStoryItem>)
                    }

                    is CustomResult.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            requireContext(),
                            "Error happened: " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setStoriesData(listStories: List<ListStoryItem>) {
        val storiesAdapter = StoriesAdapter(listStories)
        binding.rvStories.adapter = storiesAdapter
    }

    private fun showLoading(state: Boolean) {
        if (state) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }
}