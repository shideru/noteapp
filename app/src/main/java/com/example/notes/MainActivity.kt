package com.example.notes

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var cancellationSignal: CancellationSignal?= null
    private val authenticationCallback:BiometricPrompt.AuthenticationCallback
        get() =
        @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser("Authentication error: $errString")
            }
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                notifyUser("Authentication successful")
                startActivity(Intent(this@MainActivity, SecretNoteActivity::class.java))
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkBiometricSupport()
        btButton.setOnClickListener {
            val biometricPrompt = BiometricPrompt.Builder( this)
                .setTitle("Authentication")
                .setSubtitle("We need you to scan your fingerprint.")
                .setDescription("This application requires authentication with a fingerprint scan, it will tak only a second.")
                .setNegativeButton("Cancel", this.mainExecutor, DialogInterface.OnClickListener{ dialog, which ->
                    notifyUser("Authentication cancelled")
                }).build()
            biometricPrompt.authenticate(getCancellationSignal(),mainExecutor, authenticationCallback)

        }

    }
    private fun notifyUser(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun getCancellationSignal():CancellationSignal{
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication has been cancelled")
        }
        return cancellationSignal as CancellationSignal
    }
    private fun checkBiometricSupport() : Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure){
            notifyUser("Fingerprint authentication has not been enabled on this device")
            return false
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC)!=PackageManager.PERMISSION_GRANTED){
            notifyUser("Permission not enabled")
            return false
        }
        return if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        }else true


    }
}