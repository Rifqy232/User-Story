package com.mry.userstory.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mry.userstory.R
import com.mry.userstory.databinding.FragmentWelcomeBinding
import com.mry.userstory.ui.welcome.login.LoginFragment
import com.mry.userstory.ui.welcome.register.RegisterFragment

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager = parentFragmentManager

        binding.btnLogin.setOnClickListener {
            val loginFragment = LoginFragment()
            fragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, loginFragment, LoginFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

        binding.btnRegister.setOnClickListener {
            val registerFragment = RegisterFragment()
            fragmentManager.beginTransaction().apply {
                replace(
                    R.id.frame_container,
                    registerFragment,
                    RegisterFragment::class.java.simpleName
                )
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}