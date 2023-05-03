package com.mry.userstory.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mry.userstory.R
import com.mry.userstory.data.CustomResult
import com.mry.userstory.data.response.ListStoryItem
import com.mry.userstory.databinding.FragmentHomeBinding
import com.mry.userstory.ui.addStory.AddStoryActivity
import com.mry.userstory.ui.detail.DetailFragment
import com.mry.userstory.utils.ViewModelFactory

class HomeFragment : Fragment(), StoriesAdapter.OnItemClickListener {
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

        if (savedInstanceState == null) {
            homeViewModel.getAllStories()
        }

        homeViewModel.stories.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is CustomResult.Loading -> showLoading(true)
                    is CustomResult.Success -> {
                        showLoading(false)
                        @Suppress("UNCHECKED_CAST")
                        setStoriesData(result.data.listStory as List<ListStoryItem>)
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

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setStoriesData(listStories: List<ListStoryItem>) {
        val storiesAdapter = StoriesAdapter(listStories, this)
        binding.rvStories.adapter = storiesAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onItemClick(id: String) {
        val detailFragment = DetailFragment()
        val fragmentManager = parentFragmentManager

        val bundle = Bundle()
        bundle.putString(DetailFragment.EXTRA_ID, id)

        detailFragment.arguments = bundle

        fragmentManager.beginTransaction().apply {
            replace(R.id.container, detailFragment, DetailFragment::class.java.simpleName)
            addToBackStack(null)
            commit()
        }
    }
}