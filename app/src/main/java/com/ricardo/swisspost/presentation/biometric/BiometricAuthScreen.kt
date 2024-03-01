package com.ricardo.swisspost.presentation.biometric

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ricardo.swisspost.presentation.articles_list.buildNewsListRoute

const val biometricAuthRoute = "biometricAuth"
fun NavGraphBuilder.biometricAuthScreen(navigate: (String) -> Unit) = composable(
    route = biometricAuthRoute,
) {
    val onAuthenticated = { navigate(buildNewsListRoute()) }

    BiometricAuthScreen(onAuthenticated)
}

@Composable
fun BiometricAuthScreen(onAuthenticated: () -> Unit) {
    val context = LocalContext.current
    val biometricManager = remember { BiometricManager.from(context) }
    val isBiometricAvailable = remember {
        biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    }
    when (isBiometricAvailable) {
        BiometricManager.BIOMETRIC_SUCCESS -> Authenticate(onAuthenticated)
        else -> onAuthenticated()
    }
}

@Composable
fun Authenticate(onAuthenticated: () -> Unit) {
    val context = LocalContext.current
    val executor = remember { ContextCompat.getMainExecutor(context) }
    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            @RequiresApi(Build.VERSION_CODES.R)
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onAuthenticated()
            }
        }
    )
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setDeviceCredentialAllowed(true)
        .setTitle("Biometric Authentication")
        .setSubtitle("Log in using your biometric credential")
        .build()

    biometricPrompt.authenticate(promptInfo)
}
