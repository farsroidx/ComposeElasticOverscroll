@file:Suppress("unused")

package ir.farsroidx.overscroll

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds

/**
 * Standard modifier for applying horizontal elastic overscroll capability to a component.
 * Attaches the custom scrollable state and physics handling natively.
 */
@Composable
fun Modifier.horizontalElasticScrollable(
    @IntRange(from = 1, to = 100)
    maxStretchRatio: Int = 25,
    state: ScrollState = rememberScrollState(),
    @FloatRange(from = 0.0, to = 1.0)
    springDampingRatio: Float = 1f,
    @FloatRange(from = 50.0, to = 10000.0)
    springStiffness: Float = Spring.StiffnessMedium,
    @FloatRange(from = 0.1, to = 2.0)
    snapBackForce: Float? = null,
    lockedEdge: ElasticOverscrollEdge? = null,
    onProgress: ((percentage: Float, edge: ElasticOverscrollEdge) -> Unit)? = null,
    onReleased: ((percentage: Float, edge: ElasticOverscrollEdge) -> Unit)? = null
): Modifier = scrollable(
    state = state,
    orientation = Orientation.Horizontal,
    overscrollEffect = rememberHorizontalElasticOverscroll(
        springDampingRatio = springDampingRatio,
        springStiffness = springStiffness,
        maxStretchRatio = maxStretchRatio,
        snapBackForce = snapBackForce,
        lockedEdge = lockedEdge,
        onProgress = onProgress,
        onReleased = onReleased
    )
)

/**
 * Standard modifier for applying vertical elastic overscroll capability to a component.
 * Attaches the custom scrollable state and physics handling natively.
 */
@Composable
fun Modifier.verticalElasticScrollable(
    @IntRange(from = 1, to = 100)
    maxStretchRatio: Int = 25,
    state: ScrollState = rememberScrollState(),
    @FloatRange(from = 0.0, to = 1.0)
    springDampingRatio: Float = 1f,
    @FloatRange(from = 50.0, to = 10000.0)
    springStiffness: Float = Spring.StiffnessMedium,
    @FloatRange(from = 0.1, to = 2.0)
    snapBackForce: Float? = null,
    lockedEdge: ElasticOverscrollEdge? = null,
    onProgress: ((percentage: Float, edge: ElasticOverscrollEdge) -> Unit)? = null,
    onReleased: ((percentage: Float, edge: ElasticOverscrollEdge) -> Unit)? = null
): Modifier = scrollable(
    state = state,
    orientation = Orientation.Vertical,
    overscrollEffect = rememberVerticalElasticOverscroll(
        springDampingRatio = springDampingRatio,
        springStiffness = springStiffness,
        maxStretchRatio = maxStretchRatio,
        snapBackForce = snapBackForce,
        lockedEdge = lockedEdge,
        onProgress = onProgress,
        onReleased = onReleased
    )
)

/**
 * A utility modifier to ensure child contents do not draw outside the bounds
 * during horizontal elastic stretches.
 */
@Composable
fun Modifier.horizontalElasticScrollableContainer(): Modifier = clipToBounds()

/**
 * A utility modifier to ensure child contents do not draw outside the bounds
 * during vertical elastic stretches.
 */
@Composable
fun Modifier.verticalElasticScrollableContainer(): Modifier = clipToBounds()