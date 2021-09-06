package com.example.biometricauthentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.biometricauthentication.databinding.ActivityMainBinding
import es.dmoral.toasty.Toasty
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

                // init biometric
                executor = ContextCompat.getMainExecutor(this@MainActivity)

            biometricPrompt = BiometricPrompt(this@MainActivity,
                executor, object: BiometricPrompt.AuthenticationCallback(){

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        binding.authStatusTV.text = getString(R.string.authentication_error) + "$errString"
                        Toasty.error(this@MainActivity, "Authentication Error: $errString", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        binding.authStatusTV.text = getString(R.string.auth_succeeded)
                        Toasty.success(this@MainActivity, "Authentication Successful...", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        binding.authStatusTV.text = getString(R.string.auth_failed)
                        Toasty.warning(this@MainActivity, "Authentication Failed!", Toast.LENGTH_SHORT).show()
                    }
                })

            // Dialogue Builder
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint authentication")
                .setDeviceCredentialAllowed(true)
                .build()

        binding.authBtn.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

    }
}