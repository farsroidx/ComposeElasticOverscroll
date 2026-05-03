package ir.farsroidx.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ir.farsroidx.app.ui.theme.MyTestProjectTheme
import ir.farsroidx.app.ui.view.MainScreen
import ir.farsroidx.app.ui.view.MainViewState

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MyTestProjectTheme { MainScreen() } }
    }

    companion object {

        private fun makeTabs(): List<String> {

            val myTabs = mutableListOf<String>()

            repeat(times = 5) { index ->

                myTabs.add("Tab $index")
            }

            return myTabs
        }

        val FAKE_DATA = MainViewState(
            isLoading = false,
            tabs      = makeTabs()
        )
    }
}