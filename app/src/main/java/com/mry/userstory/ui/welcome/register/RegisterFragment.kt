package com.mry.userstory.ui.welcome.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import com.mry.userstory.R
import com.mry.userstory.data.CustomResult
import com.mry.userstory.data.response.RegisterResponse
import com.mry.userstory.databinding.FragmentRegisterBinding
import com.mry.userstory.utils.ViewModelFactory

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etCustomEmail.text.toString()
            val password = binding.etCustomPassword.text.toString()
            if (name.isNotEmpty()) {
                registerViewModel.register(name, email, password)
                    .observe(viewLifecycleOwner) { result ->
                        if (result != null) {
                            when (result) {
                                is CustomResult.Loading -> showLoading(true)
                                is CustomResult.Success -> {
                                    showLoading(false)
                                    registerProcess(result.data)
                                }

                                is CustomResult.Error -> {
                                    showLoading(false)
                                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
            } else {
                binding.etName.error = "This field is required"
            }
        }
    }

    private fun registerProcess(data: RegisterResponse) {
        if (data.error!!) {
            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.btnSubmit.visibility = if (state) View.GONE else View.VISIBLE
    }
}