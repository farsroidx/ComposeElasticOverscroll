package ir.farsroidx.app.ui.view

import androidx.compose.runtime.Immutable

@Immutable
data class MainViewState(
    val isLoading: Boolean,
    val tabs: List<String>,
)