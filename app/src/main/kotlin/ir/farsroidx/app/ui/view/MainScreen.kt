package ir.farsroidx.app.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.farsroidx.app.MainActivity
import ir.farsroidx.app.R
import ir.farsroidx.app.ui.theme.ComposablePreview
import ir.farsroidx.app.ui.theme.MyTestProjectThemePreview
import ir.farsroidx.overscroll.rememberHorizontalElasticOverscroll
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewState: MainViewState = MainActivity.FAKE_DATA) {

    val scope = rememberCoroutineScope()

    val lazyListState = rememberLazyListState()

    val pagerState = rememberPagerState(initialPage = 0) { viewState.tabs.size }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            ),
        topBar = {

            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {

                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.iconsax_verify),
                            contentDescription = null
                        )
                    }
                },
                navigationIcon = {

                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.iconsax_verify),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            LazyRow(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 10.dp
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                overscrollEffect = rememberHorizontalElasticOverscroll()
            ) {

                itemsIndexed(viewState.tabs) { index, tab ->

                    Box(
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(100)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    alpha = 0.075f,
                                )
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    alpha = 0.3F,
                                ),
                                shape = RoundedCornerShape(100)
                            )
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {

                        Text(
                            text = "Simple Tab",
                            fontWeight = if (pagerState.currentPage == index) {
                                FontWeight.Medium
                            } else {
                                FontWeight.Normal
                            },
                            color = if (pagerState.currentPage == index) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5F)
                            }
                        )
                    }
                }
            }



        }
    }
}

@Composable
@ComposablePreview
private fun AndroidStudioPreview() = MyTestProjectThemePreview { MainScreen() }