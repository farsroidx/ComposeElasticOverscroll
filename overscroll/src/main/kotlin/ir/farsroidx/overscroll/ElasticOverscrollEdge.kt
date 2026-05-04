@file:Suppress("unused")

package ir.farsroidx.overscroll

/**
 * Defines the specific edges where the elastic overscroll effect can be disabled (locked).
 * Supports RTL out of the box by using [START] and [END] instead of absolute left/right.
 */
enum class ElasticOverscrollEdge { TOP, BOTTOM, START, END, NONE }