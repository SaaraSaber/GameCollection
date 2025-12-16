package ir.developer.goalorpooch_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ir.developer.goalorpooch_compose.core.navigation.AppNavigation
import ir.developer.goalorpooch_compose.core.theme.GameCollection2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameCollection2Theme {
                AppNavigation()
            }
        }
    }
}