package com.mry.userstory.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mry.userstory.R
import com.mry.userstory.databinding.FragmentHomeBinding
import com.mry.userstory.ui.addStory.AddStoryActivity
import com.mry.userstory.ui.detail.DetailFragment
import com.mry.userstory.utils.ViewModelFactory

class HomeFragment : Fragment(), StoriesAdapter.OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(requireContext())
    }
    private lateinit var storiesAdapter: StoriesAdapter

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
            homeViewModel.story
        }

        val storiesAdapter = StoriesAdapter(this)
        binding.rvStories.adapter = storiesAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storiesAdapter.retry()
            }
        )
        homeViewModel.story.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                storiesAdapter.submitData(viewLifecycleOwner.lifecycle, data)
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