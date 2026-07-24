package org.gaziz.birgram.core.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val archive: ImageVector
  get() {
    if (_archive != null) {
      return _archive!!
    }
    _archive =
      ImageVector.Builder(
          name = "archive",
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
            moveTo(12f, 18f)
            lineToRelative(4f, -4f)
            lineTo(14.6f, 12.6f)
            lineTo(13f, 14.2f)
            verticalLineTo(10f)
            horizontalLineTo(11f)
            verticalLineToRelative(4.2f)
            lineTo(9.4f, 12.6f)
            lineTo(8f, 14f)
            lineToRelative(4f, 4f)
            close()
            moveTo(5f, 21f)
            quadTo(4.18f, 21f, 3.59f, 20.41f)
            reflectiveQuadTo(3f, 19f)
            verticalLineTo(6.52f)
            quadTo(3f, 6.18f, 3.11f, 5.85f)
            reflectiveQuadTo(3.45f, 5.25f)
            lineTo(4.7f, 3.72f)
            quadTo(4.98f, 3.38f, 5.39f, 3.19f)
            reflectiveQuadTo(6.25f, 3f)
            horizontalLineToRelative(11.5f)
            quadToRelative(0.45f, 0f, 0.86f, 0.19f)
            reflectiveQuadTo(19.3f, 3.72f)
            lineToRelative(1.25f, 1.53f)
            quadToRelative(0.23f, 0.27f, 0.34f, 0.6f)
            quadTo(21f, 6.18f, 21f, 6.52f)
            verticalLineTo(19f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(19f, 21f)
            horizontalLineTo(5f)
            close()
            moveTo(5.4f, 6f)
            horizontalLineTo(18.6f)
            lineTo(17.75f, 5f)
            horizontalLineTo(6.25f)
            lineTo(5.4f, 6f)
            close()
          }
        }
        .build()
    return _archive!!
  }

private var _archive: ImageVector? = null
