package com.mry.userstory.ui.welcome.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mry.userstory.R
import com.mry.userstory.data.CustomResult
import com.mry.userstory.data.response.RegisterResponse
import com.mry.userstory.databinding.FragmentRegisterBinding
import com.mry.userstory.ui.welcome.login.LoginFragment
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

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

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
                binding.etName.error = resources.getString(R.string.field_required)
            }
        }
    }

    private fun registerProcess(data: RegisterResponse) {
        if (data.error!!) {
            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show()
            val loginFragment = LoginFragment()
            val fragmentManager = parentFragmentManager

            loginFragment.isFromRegister = true

            fragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, loginFragment, LoginFragment::class.java.simpleName)
                commit()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.btnSubmit.visibility = if (state) View.GONE else View.VISIBLE
    }
}