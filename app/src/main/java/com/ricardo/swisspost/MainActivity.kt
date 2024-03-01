package com.ricardo.swisspost

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ricardo.swisspost.presentation.article_details.articlesDetailsScreen
import com.ricardo.swisspost.presentation.articles_list.articlesListScreen
import com.ricardo.swisspost.presentation.biometric.biometricAuthRoute
import com.ricardo.swisspost.presentation.biometric.biometricAuthScreen
import com.ricardo.swisspost.repository.NewsRepository
import com.ricardo.swisspost.ui.theme.SwissPostTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var repository: NewsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@Composable
private fun Content() {
    val navController = rememberNavController()
    SwissPostTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = biometricAuthRoute
            ) {
                biometricAuthScreen {
                    navController.navigate(it) {
                        popUpTo(biometricAuthRoute) {
                            inclusive = true
                        }
                    }
                }
                articlesListScreen(navController::navigate)
                articlesDetailsScreen()
            }
        }
    }
}
