package org.gaziz.birgram.core.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val exitToApp: ImageVector
  get() {
    if (_exitToApp != null) {
      return _exitToApp!!
    }
    _exitToApp =
      ImageVector.Builder(
          name = "exit_to_app",
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
            moveTo(5f, 21f)
            quadTo(4.18f, 21f, 3.59f, 20.41f)
            reflectiveQuadTo(3f, 19f)
            verticalLineTo(15f)
            horizontalLineTo(5f)
            verticalLineToRelative(4f)
            horizontalLineTo(19f)
            verticalLineTo(5f)
            horizontalLineTo(5f)
            verticalLineTo(9f)
            horizontalLineTo(3f)
            verticalLineTo(5f)
            quadTo(3f, 4.17f, 3.59f, 3.59f)
            reflectiveQuadTo(5f, 3f)
            horizontalLineTo(19f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            reflectiveQuadTo(21f, 5f)
            verticalLineTo(19f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(19f, 21f)
            horizontalLineTo(5f)
            close()
            moveToRelative(5.5f, -4f)
            lineTo(9.1f, 15.55f)
            lineTo(11.65f, 13f)
            horizontalLineTo(3f)
            verticalLineTo(11f)
            horizontalLineToRelative(8.65f)
            lineTo(9.1f, 8.45f)
            lineTo(10.5f, 7f)
            lineToRelative(5f, 5f)
            lineToRelative(-5f, 5f)
            close()
          }
        }
        .build()
    return _exitToApp!!
  }

private var _exitToApp: ImageVector? = null
