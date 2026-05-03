@file:Suppress("unused")

package ir.farsroidx.overscroll

import android.annotation.SuppressLint
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Remembers and configures an [ElasticOverscrollEffect] based on the provided parameters.
 * Automatically handles screen dimensions to calculate pixel values securely.
 *
 * @param orientation The scroll orientation.
 * @param maxStretchRatio The maximum stretch distance as a percentage of the screen size.
 * @param springDampingRatio Bounciness of the snap-back animation ($1f$ is critically damped).
 * @param springStiffness Speed/Stiffness of the snap-back animation.
 * @param snapBackForce Custom multiplier for stiffness when releasing the touch.
 * @param lockedEdge Edge to prevent from overScrolling.
 * @param onProgress Real-time progress callback ($0.0$ to $1.0$).
 * @param onReleased Callback triggered exactly once upon release.
 * @return An optimized [OverscrollEffect] ready to be used in scrollable modifiers.
 */
@Composable
fun rememberElasticOverscrollEffect(
    orientation: Orientation,
    @IntRange(from = 1, to = 100)
    maxStretchRatio: Int = 25,
    @FloatRange(from = 0.0, to = 1.0)
    springDampingRatio: Float = 1f,
    @FloatRange(from = 50.0, to = 10000.0)
    springStiffness: Float = Spring.StiffnessMedium,
    @FloatRange(from = 0.1, to = 2.0)
    snapBackForce: Float? = null,
    lockedEdge: ElasticOverscrollEdgeLock? = null,
    onProgress: ((percentage: Float) -> Unit)? = null,
    onReleased: ((percentage: Float) -> Unit)? = null
): OverscrollEffect {

    val density = LocalDensity.current

    val config = LocalConfiguration.current

    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val maxOverscroll = remember(key1 = orientation, key2 = maxStretchRatio, key3 = config) {

        @SuppressLint("ConfigurationScreenWidthHeight")
        val sizeDp = if (orientation == Orientation.Horizontal) {
            config.screenWidthDp
        } else {
            config.screenHeightDp
        }

        with(density) { sizeDp.dp.toPx() } * (maxStretchRatio / 100f)
    }

    val effect = remember {
        ElasticOverscrollEffect(
            springDampingRatio = springDampingRatio,
            springStiffness = springStiffness,
            maxStretchRatio = maxStretchRatio,
            maxOverscroll = maxOverscroll,
            snapBackForce = snapBackForce,
            orientation = orientation,
            lockedEdge = lockedEdge,
            onProgress = onProgress,
            onReleased = onReleased,
            isRtl = isRtl
        )
    }

    SideEffect {
        effect.springDampingRatio = springDampingRatio
        effect.springStiffness = springStiffness
        effect.maxStretchRatio = maxStretchRatio
        effect.maxOverscroll = maxOverscroll
        effect.snapBackForce = snapBackForce
        effect.orientation = orientation
        effect.lockedEdge = lockedEdge
        effect.onProgress = onProgress
        effect.onReleased = onReleased
        effect.isRtl = isRtl
    }

    return effect
}

/**
 * Convenience Composable to create a Horizontal Elastic Overscroll effect.
 *
 * Remembers and configures an [ElasticOverscrollEffect] based on the provided parameters.
 * Automatically handles screen dimensions to calculate pixel values securely.
 *
 * @param maxStretchRatio The maximum stretch distance as a percentage of the screen size.
 * @param springDampingRatio Bounciness of the snap-back animation ($1f$ is critically damped).
 * @param springStiffness Speed/Stiffness of the snap-back animation.
 * @param snapBackForce Custom multiplier for stiffness when releasing the touch.
 * @param lockedEdge Edge to prevent from overScrolling.
 * @param onProgress Real-time progress callback ($0.0$ to $1.0$).
 * @param onReleased Callback triggered exactly once upon release.
 * @return An optimized [OverscrollEffect] ready to be used in scrollable modifiers.
 */
@Composable
fun rememberHorizontalElasticOverscroll(
    @IntRange(from = 1, to = 100)
    maxStretchRatio: Int = 25,
    @FloatRange(from = 0.0, to = 1.0)
    springDampingRatio: Float = 1f,
    @FloatRange(from = 50.0, to = 10000.0)
    springStiffness: Float = Spring.StiffnessMedium,
    @FloatRange(from = 0.1, to = 2.0)
    snapBackForce: Float? = null,
    lockedEdge: ElasticOverscrollEdgeLock? = null,
    onProgress: ((percentage: Float) -> Unit)? = null,
    onReleased: ((percentage: Float) -> Unit)? = null
): OverscrollEffect {
    return rememberElasticOverscrollEffect(
        springDampingRatio = springDampingRatio,
        springStiffness = springStiffness,
        maxStretchRatio = maxStretchRatio,
        snapBackForce = snapBackForce,
        orientation = Orientation.Horizontal,
        lockedEdge = lockedEdge,
        onProgress = onProgress,
        onReleased = onReleased
    )
}

/**
 * Convenience Composable to create a Vertical Elastic Overscroll effect.
 *
 * Remembers and configures an [ElasticOverscrollEffect] based on the provided parameters.
 * Automatically handles screen dimensions to calculate pixel values securely.
 *
 * @param maxStretchRatio The maximum stretch distance as a percentage of the screen size.
 * @param springDampingRatio Bounciness of the snap-back animation ($1f$ is critically damped).
 * @param springStiffness Speed/Stiffness of the snap-back animation.
 * @param snapBackForce Custom multiplier for stiffness when releasing the touch.
 * @param lockedEdge Edge to prevent from overScrolling.
 * @param onProgress Real-time progress callback ($0.0$ to $1.0$).
 * @param onReleased Callback triggered exactly once upon release.
 * @return An optimized [OverscrollEffect] ready to be used in scrollable modifiers.
 */
@Composable
fun rememberVerticalElasticOverscroll(
    @IntRange(from = 1, to = 100)
    maxStretchRatio: Int = 25,
    @FloatRange(from = 0.0, to = 1.0)
    springDampingRatio: Float = 1f,
    @FloatRange(from = 50.0, to = 10000.0)
    springStiffness: Float = Spring.StiffnessMedium,
    @FloatRange(from = 0.1, to = 2.0)
    snapBackForce: Float? = null,
    lockedEdge: ElasticOverscrollEdgeLock? = null,
    onProgress: ((percentage: Float) -> Unit)? = null,
    onReleased: ((percentage: Float) -> Unit)? = null
): OverscrollEffect {
    return rememberElasticOverscrollEffect(
        orientation = Orientation.Vertical,
        springDampingRatio = springDampingRatio,
        springStiffness = springStiffness,
        maxStretchRatio = maxStretchRatio,
        snapBackForce = snapBackForce,
        lockedEdge = lockedEdge,
        onProgress = onProgress,
        onReleased = onReleased
    )
}