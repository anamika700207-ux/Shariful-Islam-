package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ui.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val profile by viewModel.profileState.collectAsState()
            MyApplicationTheme(selectedThemeKey = profile.selectedThemeKey) {
                ProfileScreen(viewModel = viewModel)
            }
        }
    }
}
