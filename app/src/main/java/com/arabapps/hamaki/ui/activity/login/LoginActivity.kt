package com.arabapps.hamaki.ui.activity.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arabapps.hamaki.R
import com.arabapps.hamaki.databinding.ActivityLoginBinding
import com.arabapps.hamaki.ui.activity.main.MainActivity
import com.sasco.user.helper.DialogHelper
import com.sasco.user.helper.TextValidationInput

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loading: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = DialogHelper.getLoading(this)
        loginViewModel = ViewModelProvider(this)
            .get(LoginViewModel::class.java)
        binding.apply {
            login.setOnClickListener {
                if (TextValidationInput.isPhoneValid(binding.phone) && TextValidationInput.isPasswordalid(
                        binding.password
                    )
                ) {
                    loading.show()
                    loginViewModel.login(
                        this@LoginActivity,
                        phone.text.toString(),
                        password.text.toString(),
                        Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID)
                    )
                }
            }
            loginViewModel.loginLiveData.observe(this@LoginActivity, Observer {
                loading.cancel()
                if (it != null) {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }
            })
            visible.setOnClickListener {
                if (password.inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                    visible.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24)
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    visible.setBackgroundResource(R.drawable.ic_baseline_visibility_24)

                }
            }

        }


    }

}

