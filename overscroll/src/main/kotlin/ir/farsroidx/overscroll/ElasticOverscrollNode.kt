@file:Suppress("unused")

package ir.farsroidx.overscroll

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode

/**
 * A highly optimized [DrawModifierNode] responsible for applying the visual translation offset
 * to the canvas during the overscroll effect.
 *
 * By using Modifier.Node architecture, it avoids unnecessary recompositions and
 * directly performs translation in the draw phase, ensuring maximum performance.
 *
 * @property overscrollEffect The underlying core logic handling the physics state.
 */
class ElasticOverscrollNode(
    val overscrollEffect: ElasticOverscrollEffect
) : Modifier.Node(), DrawModifierNode {

    override fun ContentDrawScope.draw() {

        val value = overscrollEffect.overscrollValue

        if (value != 0f) {

            if (overscrollEffect.orientation == Orientation.Vertical) {

                drawContext.transform.translate(top = value)

            } else {

                drawContext.transform.translate(left = value)

            }
        }

        drawContent()
    }
}