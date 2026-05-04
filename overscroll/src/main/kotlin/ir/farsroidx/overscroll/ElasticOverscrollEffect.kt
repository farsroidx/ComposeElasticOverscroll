@file:Suppress("unused")

package ir.farsroidx.overscroll

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * The core implementation of the Elastic Overscroll Effect.
 * Provides advanced stretch physics with dynamic friction, exponential resistance ($$ (1-x)^2 $$),
 * customizable spring animation for snap-back, and selective edge locking.
 *
 * @property springDampingRatio Controls the bounciness of the snap-back animation.
 * @property springStiffness Controls the speed/stiffness of the snap-back animation.
 * @property maxStretchRatio The maximum screen percentage the view can stretch ($1$ to $100$).
 * @property maxOverscroll The absolute maximum distance in pixels the view is allowed to stretch.
 * @property snapBackForce A multiplier to optionally increase the stiffness during release.
 * @property orientation The scroll orientation (Vertical or Horizontal).
 * @property lockedEdge An optional edge to restrict overScrolling on a specific side.
 * @property onProgress Callback invoked repeatedly with the stretch progress percentage ($0.0$ to $1.0$).
 * @property onReleased Callback invoked exactly once upon user release, providing the release percentage.
 * @property isRtl Indicates whether the current layout direction is Right-to-Left.
 */
class ElasticOverscrollEffect(
    @FloatRange(from = 0.0, to = 1.0)
    var springDampingRatio: Float = 1f,
    @FloatRange(from = 50.0, to = 10000.0)
    var springStiffness: Float = Spring.StiffnessMedium,
    @IntRange(from = 1, to = 100)
    var maxStretchRatio: Int = 25,
    var maxOverscroll: Float = 100F,
    @FloatRange(from = 0.1, to = 2.0)
    var snapBackForce: Float? = null,
    var orientation: Orientation = Orientation.Vertical,
    var lockedEdge: ElasticOverscrollEdge? = null,
    var onProgress: ((percentage: Float, edge: ElasticOverscrollEdge) -> Unit)? = null,
    var onReleased: ((percentage: Float, edge: ElasticOverscrollEdge) -> Unit)? = null,
    var isRtl: Boolean = false
) : OverscrollEffect {

    /** The current translation offset value in pixels. */
    var mOverscrollValue by mutableFloatStateOf(0f)

    private val mDrawNode = ElasticOverscrollNode(this)

    override val node: DelegatableNode
        get() = mDrawNode

    override val isInProgress: Boolean
        get() = mOverscrollValue != 0f

    private var mActiveEdge: ElasticOverscrollEdge = ElasticOverscrollEdge.NONE

    private var mLastReportedProgress: Float = -1f

    override fun applyToScroll(
        delta: Offset,
        source: NestedScrollSource,
        performScroll: (Offset) -> Offset
    ): Offset {

        val delta1D = if (orientation == Orientation.Vertical) delta.y else delta.x

        var consumed = 0f

        // Dynamic friction based on user configuration ($maxStretchRatio$)
        val dynamicFriction = (maxStretchRatio * 2.5f).coerceIn(0.2f, 1f)

        // 1. Handle Pull-back logic (if we are already overScrolled and pulling in opposite direction)
        if (mOverscrollValue != 0f) {

            val isPullingBack = sign(delta1D) != sign(mOverscrollValue)

            if (isPullingBack) {

                val newValue = mOverscrollValue + (delta1D * dynamicFriction)

                if (sign(newValue) != sign(mOverscrollValue)) {

                    val consumedToZero = mOverscrollValue / dynamicFriction

                    consumed = -consumedToZero

                    mOverscrollValue = 0f

                } else {

                    consumed = delta1D

                    mOverscrollValue = newValue
                }
            }
        }

        // 2. Perform regular scroll for the remaining delta
        val remainingForScroll1D = delta1D - consumed

        val remainingForScrollOffset = if (orientation == Orientation.Vertical) {
            Offset(0f, remainingForScroll1D)
        } else {
            Offset(remainingForScroll1D, 0f)
        }

        val consumedByScrollOffset = performScroll(remainingForScrollOffset)

        val consumedByScroll = if (orientation == Orientation.Vertical) {
            consumedByScrollOffset.y
        } else {
            consumedByScrollOffset.x
        }

        consumed += consumedByScroll

        val remainingDelta1D = delta1D - consumed

        val isDrag = source == NestedScrollSource.UserInput

        if (mActiveEdge == ElasticOverscrollEdge.NONE && mOverscrollValue != 0F) {

            mActiveEdge = when (orientation) {

                Orientation.Vertical -> {

                    if (mOverscrollValue > 0f) {

                        ElasticOverscrollEdge.TOP

                    } else {

                        ElasticOverscrollEdge.BOTTOM

                    }
                }

                Orientation.Horizontal -> {

                    if (mOverscrollValue > 0f) {

                        if (isRtl) ElasticOverscrollEdge.END else ElasticOverscrollEdge.START

                    } else {

                        if (isRtl) ElasticOverscrollEdge.START else ElasticOverscrollEdge.END

                    }
                }
            }
        }

        // 3. Overscroll Edge Lock Evaluation
        val isLocked = when (orientation) {

            Orientation.Vertical -> {
                (remainingDelta1D > 0f && lockedEdge == ElasticOverscrollEdge.TOP) ||
                    (remainingDelta1D < 0f && lockedEdge == ElasticOverscrollEdge.BOTTOM)
            }

            Orientation.Horizontal -> {

                if (remainingDelta1D > 0f) { // Pulling Right

                    if (isRtl) {
                        lockedEdge == ElasticOverscrollEdge.END
                    } else {
                        lockedEdge == ElasticOverscrollEdge.START
                    }

                } else if (remainingDelta1D < 0f) { // Pulling Left

                    if (isRtl) {
                        lockedEdge == ElasticOverscrollEdge.START
                    } else {
                        lockedEdge == ElasticOverscrollEdge.END
                    }

                } else false
            }
        }

        // 4. Apply Advanced Stretch Physics (if not locked)
        if (remainingDelta1D != 0f && isDrag && !isLocked) {

            val progress = (abs(mOverscrollValue) / maxOverscroll).coerceIn(0f, 1f)

            // Exponential resistance ($$ (1-x)^2 $$) acting as an invisible wall
            val resistance = (1f - progress) * (1f - progress)

            val deltaWithResistance = remainingDelta1D * dynamicFriction * resistance

            val newValue = mOverscrollValue + deltaWithResistance

            mOverscrollValue = newValue.coerceIn(-maxOverscroll, maxOverscroll)

            consumed += remainingDelta1D
        }

        if (onProgress != null) {

            val progress = (abs(mOverscrollValue) / maxOverscroll).coerceIn(0f, 1f)

            var roundedProgress = (progress * 10000f).roundToInt() / 10000f

            if (roundedProgress < 0.001f) {
                roundedProgress = 0f
            }

            if (roundedProgress != mLastReportedProgress) {

                mLastReportedProgress = roundedProgress

                onProgress?.invoke(roundedProgress, mActiveEdge)

                if (roundedProgress == 0f) {
                    mActiveEdge = ElasticOverscrollEdge.NONE
                }
            }
        }

        return if (orientation == Orientation.Vertical) {
            Offset(0f, consumed)
        } else {
            Offset(consumed, 0f)
        }
    }

    override suspend fun applyToFling(
        velocity: Velocity,
        performFling: suspend (Velocity) -> Velocity
    ) {

        if (mOverscrollValue != 0f) {
            val percentage = (abs(mOverscrollValue) / maxOverscroll).coerceIn(0f, 1f)
            if (percentage > 0f) {
                onReleased?.invoke(percentage, mActiveEdge)
            }
        }

        performFling(velocity)

        if (mOverscrollValue != 0f) {

            val stiffness = if (snapBackForce != null) {

                springStiffness * snapBackForce!!

            } else {

                val distance = abs(mOverscrollValue)

                when {
                    distance < 50f -> 1200f
                    distance < 150f -> 1000f
                    else -> 850f
                }
            }

            animate(
                initialValue = mOverscrollValue,
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = springDampingRatio,
                    stiffness = stiffness
                )
            ) { value, _ ->

                mOverscrollValue = value

                if (onProgress != null) {

                    val progress = (abs(value) / maxOverscroll).coerceIn(0f, 1f)

                    var roundedProgress = (progress * 10000f).roundToInt() / 10000f

                    if (roundedProgress < 0.001f) {
                        roundedProgress = 0f
                    }

                    if (roundedProgress < mLastReportedProgress) {

                        mLastReportedProgress = roundedProgress

                        onProgress?.invoke(roundedProgress, mActiveEdge)

                        if (roundedProgress == 0f) {
                            mActiveEdge = ElasticOverscrollEdge.NONE
                        }
                    }
                }
            }
        }
    }
}