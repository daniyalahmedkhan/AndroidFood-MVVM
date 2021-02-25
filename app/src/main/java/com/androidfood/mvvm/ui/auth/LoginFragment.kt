package com.androidfood.mvvm.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.androidfood.mvvm.R
import com.androidfood.mvvm.base.BaseFragment
import com.androidfood.mvvm.callback.ActionCallback
import com.androidfood.mvvm.constants.AppConstants_Java
import com.androidfood.mvvm.data.model.auth.Register.UserRegistrationModel
import com.androidfood.mvvm.data.model.auth.SocialLogin
import com.androidfood.mvvm.data.remote.GeneralApiResponse
import com.androidfood.mvvm.databinding.FragmentLoginBinding
import com.androidfood.mvvm.helper.GeneralHelper
import com.androidfood.mvvm.helper.PrefsHelper
import com.androidfood.mvvm.ui.home.HomeActivity
import com.androidfood.mvvm.vm.auth.LoginViewModel
import org.jetbrains.anko.support.v4.startActivity

class LoginFragment : BaseFragment(), ActionCallback {

    private lateinit var binding: FragmentLoginBinding
    val loginViewModel: LoginViewModel by viewModels()

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            binding<FragmentLoginBinding>(inflater, R.layout.fragment_login, container).apply {
                lifecycleOwner = viewLifecycleOwner
                callback = this@LoginFragment
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activateHideKeyboardUponTouchingScreen(view)

        loginViewModel.loginState.observe(viewLifecycleOwner, Observer {

            when (it) {

                is GeneralApiResponse.Loading -> {
                    binding.loadingView.visibility = View.VISIBLE
                }

                is GeneralApiResponse.Failure -> {
                    binding.loadingView.visibility = View.GONE
                    GeneralHelper.showDialog(requireActivity(), it.error, object : ActionCallback {
                        override fun onBindingClick(view: View) {
                        }

                        override fun onBindingClick(id: Int) {
                        }

                    })
                }

                is GeneralApiResponse.Success<UserRegistrationModel> -> {
                    binding.loadingView.visibility = View.GONE
                    GeneralHelper.updateUserInfo(it.data)
                    PrefsHelper.putBoolean(AppConstants_Java.isLogin, true)
                    startActivity<HomeActivity>()
                    requireActivity().finish()

                }

            }

        })

        // Google sign in
        // Configure Google Sign In inside onCreate mentod
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_id))
            .requestEmail()
            .build()
// getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
// initialize the firebaseAuth variable
        firebaseAuth = FirebaseAuth.getInstance()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun onBindingClick(view: View) {
        when (view.id) {
            R.id.login_signup_link -> {
                replaceWithNewFragment(SignupFragment.newInstance())
            }
            R.id.login_btn -> {

                loginViewModel.handleUserInput(
                    binding.loginEmail.editText?.text.toString(),
                    binding.loginPassword.editText?.text.toString()
                )

            }
            R.id.TV_forgotPassword -> {
                replaceWithNewFragment(ForgotPassword.newInstance())
            }
            R.id.googleLogin -> {
                signInGoogle()
            }
        }
    }

    override fun onBindingClick(id: Int) {
    }

    private fun replaceWithNewFragment(fragment: Fragment) {
        replaceFragment(
            fragment,
            R.id.auth_container,
            addBackStack = false,
            clearBackStack = true
        )
    }

    private fun signInGoogle() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    // onActivityResult() function : this is where we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginViewModel.userSocialLogin(
                    SocialLogin(
                        "Google",
                        account.id,
                        account.idToken,
                        account.displayName,
                        account.email,
                        account.photoUrl.toString(),
                        "abc",
                        "android"
                    )
                )
                Log.d("GMAILLOGIN", account.email.toString())
            } else {
                Log.d("GMAILLOGIN", account.toString())
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//        if (GoogleSignIn.getLastSignedInAccount(requireActivity()) != null) {
//
//        }
//    }
}