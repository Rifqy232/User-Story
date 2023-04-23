package com.mry.userstory.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.mry.userstory.R
import com.mry.userstory.data.CustomResult
import com.mry.userstory.databinding.FragmentDetailBinding
import com.mry.userstory.ui.home.HomeViewModel
import com.mry.userstory.utils.ViewModelFactory

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var id: String
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    companion object {
        var EXTRA_ID = "extra_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            id = arguments?.getString(EXTRA_ID)!!
        }

        if (savedInstanceState == null) {
            detailViewModel.getDetailStory(id)
        }

        detailViewModel.detailStory.observe(viewLifecycleOwner) {result ->
            if (result != null) {
                when (result) {
                    is CustomResult.Loading -> showLoading(true)
                    is CustomResult.Success -> {
                        showLoading(false)
                        binding.tvName.text = result.data.story?.name
                        binding.tvDescription.text = result.data.story?.description
                        Glide.with(requireContext())
                            .load(result.data.story?.photoUrl)
                            .into(binding.ivStory)
                        binding.ivStory.contentDescription = result.data.story?.description
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
    }

    private fun showLoading(state: Boolean) {
        if (state) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }
}