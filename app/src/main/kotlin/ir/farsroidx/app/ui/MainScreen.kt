package ir.farsroidx.app.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.farsroidx.app.R
import ir.farsroidx.app.ui.theme.ComposablePreview
import ir.farsroidx.app.ui.theme.MyTestProjectThemePreview
import ir.farsroidx.overscroll.ElasticOverscrollEdge
import ir.farsroidx.overscroll.rememberHorizontalElasticOverscroll
import ir.farsroidx.overscroll.rememberVerticalElasticOverscroll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {

    val tabs = remember { makeTabs() }

    val scope = rememberCoroutineScope()

    var index by rememberSaveable { mutableIntStateOf(value = 0) }

    val lazyListState = rememberLazyListState()

    val pagerState = rememberPagerState(initialPage = index) { tabs.size }

    var isLoading by remember { mutableStateOf(false) }

    var scrolledEdge by remember { mutableStateOf(ElasticOverscrollEdge.NONE) }

    var scrolledPercentage by remember { mutableFloatStateOf(0F) }

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
                overscrollEffect = rememberHorizontalElasticOverscroll(
                    maxStretchRatio = 20,
                    springDampingRatio = 0.65F
                )
            ) {

                itemsIndexed(tabs) { tabIndex, _ ->

                    Box(
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(100)
                            )
                            .then(
                                other = if (pagerState.currentPage == tabIndex) {
                                    Modifier.shadow(
                                        elevation = 2.dp,
                                        shape = RoundedCornerShape(100)
                                    )
                                } else Modifier
                            )
                            .background(
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    alpha = 0.05f,
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
                                index = tabIndex
                            }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {

                        Text(
                            text = "Simple Tab",
                            fontWeight = if (pagerState.currentPage == tabIndex) {
                                FontWeight.Medium
                            } else {
                                FontWeight.Normal
                            },
                            color = if (pagerState.currentPage == tabIndex) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5F)
                            }
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    overscrollEffect = rememberHorizontalElasticOverscroll(
                        maxStretchRatio = 30,
                        onProgress = { percentage, edge ->
                            if (isLoading) return@rememberHorizontalElasticOverscroll
                            scrolledEdge = edge
                            scrolledPercentage = pulledToRefresh(edge, percentage)
                            Log.i("ElasticOverscroll", "Edge: ${edge.name}, $percentage")
                        },
                        onReleased = { percentage, edge ->
                            scrolledPercentage = pulledToRefresh(edge, percentage)
                            isLoading = scrolledPercentage == 1f
                            Log.w("ElasticOverscroll", "Edge: ${edge.name}, $percentage")
                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) { pageIndex ->

                    VerticalDivider(color = DividerDefaults.color)

                    LazyColumn(
                        overscrollEffect = rememberVerticalElasticOverscroll(
                            springDampingRatio = 0.5f,
                            lockedEdge = null,
                            onProgress = { percentage, edge ->
                                if (isLoading) return@rememberVerticalElasticOverscroll
                                scrolledEdge = edge
                                scrolledPercentage = pulledToRefresh(edge, percentage)
                                Log.i("ElasticOverscroll", "Edge: ${edge.name}, $percentage")
                            },
                            onReleased = { percentage, edge ->
                                scrolledPercentage = pulledToRefresh(edge, percentage)
                                isLoading = scrolledPercentage == 1f
                                Log.w("ElasticOverscroll", "Edge: ${edge.name}, $percentage")
                            }
                        ),
                    ) {

                        items(20) { item ->

                            ListItem(
                                title = "Title ${item + 1}",
                                subtitle = "This is a sample text for item ${item + 1}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 16.dp, start = 16.dp, end = 16.dp,
                                        bottom = if (item == 19) 16.dp else 0.dp
                                    )
                                    .clip(
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        color = MaterialTheme.colorScheme.secondaryContainer
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.3F,
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        // index = tabIndex
                                    }
                            )
                        }
                    }

                    if (pageIndex == tabs.size - 1) {

                        VerticalDivider(color = DividerDefaults.color)
                    }
                }

                RefreshIcon(
                    progress  = scrolledPercentage,
                    alignment = when(scrolledEdge) {
                        ElasticOverscrollEdge.TOP -> Alignment.TopCenter
                        ElasticOverscrollEdge.BOTTOM -> Alignment.BottomCenter
                        ElasticOverscrollEdge.START -> Alignment.CenterStart
                        ElasticOverscrollEdge.END -> Alignment.CenterEnd
                        ElasticOverscrollEdge.NONE -> Alignment.Center
                    },
                    isLoading = isLoading,
                    modifier  = Modifier
                )
            }
        }
    }

    LaunchedEffect(key1 = isLoading) {
        scope.launch {
            if (isLoading) {
                delay(5000)
                isLoading = false
                scrolledPercentage = 0F
                scrolledEdge = ElasticOverscrollEdge.END
            }
        }
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        lazyListState.animateScrollToItem(pagerState.currentPage)
    }

    LaunchedEffect(key1 = index) {
        if (index >= 0) {
            pagerState.animateScrollToPage(index)
        }
    }
}

@Composable
private fun ListItem(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.iconsax_verify),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(
                        alpha = 0.075F
                    ),
                    shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.3F,
                    ),
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(
                    all = 8.dp
                ),
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {


            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun BoxScope.RefreshIcon(
    progress: Float,
    isLoading: Boolean,
    alignment: Alignment,
    modifier: Modifier = Modifier
) {

    val color = MaterialTheme.colorScheme.onPrimaryContainer

    if (isLoading) {

        CircularProgressIndicator(
            modifier = Modifier
                .align(alignment)
                .padding(all = 16.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape
                )
                .background(
                    color = color,
                    shape = CircleShape
                )
                .padding(4.dp)
                .then(modifier),
            strokeWidth = 4.dp,
            strokeCap = StrokeCap.Round,
            trackColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.25F
            ),
            color = MaterialTheme.colorScheme.primary,
            gapSize = 4.dp
        )

    } else {

        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .alpha(progress)
                .scale(progress)
                .align(alignment)
                .padding(all = 16.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape
                )
                .background(
                    color = color,
                    shape = CircleShape
                )
                .padding(4.dp)
                .then(modifier),
            strokeWidth = 4.dp,
            strokeCap = StrokeCap.Round,
            trackColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.25F
            ),
            color = MaterialTheme.colorScheme.primary,
            gapSize = 4.dp
        )
    }
}

private fun makeTabs(): List<Int> {
    val myTabs = mutableListOf<Int>()
    repeat(times = 5) { index -> myTabs.add(index) }
    return myTabs
}

private fun pulledToRefresh(edge: ElasticOverscrollEdge, progress: Float): Float {
    return if (edge == ElasticOverscrollEdge.TOP || edge == ElasticOverscrollEdge.BOTTOM) {
        (progress / 0.5f).coerceIn(0f, 1f)
    } else {
        (progress / 0.6f).coerceIn(0f, 1f)
    }
}

@Composable
@ComposablePreview
private fun AndroidStudioPreview() = MyTestProjectThemePreview { MainScreen() }