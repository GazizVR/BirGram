package org.gaziz.birgram.core.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val darkMode: ImageVector
  get() {
    if (_darkMode != null) {
      return _darkMode!!
    }
    _darkMode =
      ImageVector.Builder(
          name = "dark_mode",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(12f, 21f)
            quadTo(8.25f, 21f, 5.63f, 18.38f)
            reflectiveQuadTo(3f, 12f)
            reflectiveQuadTo(5.63f, 5.63f)
            reflectiveQuadTo(12f, 3f)
            quadToRelative(0.35f, 0f, 0.69f, 0.02f)
            quadToRelative(0.34f, 0.02f, 0.66f, 0.07f)
            quadTo(12.33f, 3.82f, 11.71f, 4.99f)
            reflectiveQuadTo(11.1f, 7.5f)
            quadToRelative(0f, 2.25f, 1.57f, 3.82f)
            reflectiveQuadTo(16.5f, 12.9f)
            quadToRelative(1.38f, 0f, 2.53f, -0.61f)
            reflectiveQuadTo(20.9f, 10.65f)
            quadToRelative(0.05f, 0.33f, 0.07f, 0.66f)
            reflectiveQuadTo(21f, 12f)
            quadToRelative(0f, 3.75f, -2.63f, 6.38f)
            reflectiveQuadTo(12f, 21f)
            close()
          }
        }
        .build()
    return _darkMode!!
  }

private var _darkMode: ImageVector? = null
