package ir.farsroidx.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ir.farsroidx.app.ui.theme.MyTestProjectTheme
import ir.farsroidx.app.ui.MainScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent { MyTestProjectTheme { MainScreen() } }
    }
}