package com.mry.userstory.ui.welcome.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mry.userstory.data.CustomResult
import com.mry.userstory.data.response.LoginResponse
import com.mry.userstory.databinding.FragmentLoginBinding
import com.mry.userstory.ui.home.HomeActivity
import com.mry.userstory.utils.UserPreferences
import com.mry.userstory.utils.ViewModelFactory

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    var isFromRegister: Boolean? = null
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    companion object {
        const val EXTRA_IS_FROM_REGISTER = "extra_is_from_register"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val isFromRegisterData = savedInstanceState.getBoolean(EXTRA_IS_FROM_REGISTER)
            isFromRegister = isFromRegisterData
        }

        setEnableButton()

        binding.etCustomPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) < 8) {
                    setEnableButton()
                } else {
                    setEnableButton()
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.btnSubmit.setOnClickListener {
            val email = binding.etCustomEmail.text.toString()
            val password = binding.etCustomPassword.text.toString()

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            loginViewModel.login(email, password).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is CustomResult.Loading -> showLoading(true)
                        is CustomResult.Success -> {
                            showLoading(false)
                            loginProcess(result.data)
                        }

                        is CustomResult.Error -> {
                            showLoading(false)
                            Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        if (isFromRegister != null && isFromRegister as Boolean) {
            onBackPressed()
        }
    }

    private fun setEnableButton() {
        val result = binding.etCustomPassword.text
        binding.btnSubmit.isEnabled = result != null && result.toString().isNotEmpty() && result.length >= 8
    }

    private fun loginProcess(data: LoginResponse) {
        if (data.error!!) {
            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
        } else {
            saveUserToken(data.loginResult?.token.toString())
            Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun saveUserToken(token: String) {
        val userPreferences = UserPreferences(requireContext())
        userPreferences.saveUserToken(token)
    }

    private fun showLoading(state: Boolean) {
        binding.btnSubmit.visibility = if (state) View.GONE else View.VISIBLE
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }
}