package com.example.voteapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class FingerprintHandler(private val appContext: Context) : FingerprintManager.AuthenticationCallback() {

        private var cancellationSignal: CancellationSignal? = null
        public var voteText: String = "test"

        fun startAuth(manager: FingerprintManager,
                      cryptoObject: FingerprintManager.CryptoObject){
            cancellationSignal = CancellationSignal()

            if (ActivityCompat.checkSelfPermission(appContext,
                    Manifest.permission.USE_FINGERPRINT) !=
                PackageManager.PERMISSION_GRANTED) {
                return
            }
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
            super.onAuthenticationError(errorCode, errString)
            Toast.makeText(appContext,
                "Authentication error\n" + errString,
                Toast.LENGTH_LONG).show()

        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(appContext,
                "Authentication failed.",
                Toast.LENGTH_LONG).show()
        }

        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
            super.onAuthenticationHelp(helpCode, helpString)
            Toast.makeText(appContext,
                "Authentication help\n" + helpString,
                Toast.LENGTH_LONG).show()
        }

        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)

            if(voteText=="I support it") {
                val queue= Volley.newRequestQueue( this.appContext)
                val stringRequest = StringRequest(
                    Request.Method.GET,
                    "https://web-production-eac9.up.railway.app/voteup",
                    { response ->
                        try {
                            Toast.makeText(
                                this.appContext,
                                "Block Hash " + response,
                                Toast.LENGTH_LONG
                            ).show()

                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                    },
                    { error ->
                        Toast.makeText(
                            this.appContext,
                            error.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
                queue.add(stringRequest)
                Toast.makeText(
                    appContext,
                    voteText,
                    Toast.LENGTH_LONG
                ).show()
            }
            else if(voteText=="I don't support it"){
                val queue= Volley.newRequestQueue( this.appContext)
                val stringRequest = StringRequest(
                    Request.Method.GET,
                    "https://web-production-eac9.up.railway.app/votedown",
                    { response ->
                        try {
                            Toast.makeText(
                                this.appContext,
                                "Block Hash " + response,
                                Toast.LENGTH_LONG
                            ).show()

                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                    },
                    { error ->
                        Toast.makeText(
                            this.appContext,
                            error.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
                queue.add(stringRequest)
                Toast.makeText(
                    appContext,
                    voteText,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }