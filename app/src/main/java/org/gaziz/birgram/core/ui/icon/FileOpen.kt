package org.gaziz.birgram.core.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val fileOpen: ImageVector
  get() {
    if (_fileOpen != null) {
      return _fileOpen!!
    }
    _fileOpen =
      ImageVector.Builder(
          name = "file_open",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(21.95f, 22.38f)
            lineTo(19f, 19.43f)
            verticalLineToRelative(2.22f)
            horizontalLineTo(17f)
            verticalLineTo(16f)
            horizontalLineToRelative(5.65f)
            verticalLineToRelative(2f)
            horizontalLineTo(20.4f)
            lineToRelative(2.95f, 2.95f)
            lineToRelative(-1.4f, 1.43f)
            close()
            moveTo(13f, 9f)
            horizontalLineToRelative(5f)
            lineTo(13f, 4f)
            verticalLineTo(9f)
            close()
            moveTo(6f, 22f)
            quadTo(5.18f, 22f, 4.59f, 21.41f)
            reflectiveQuadTo(4f, 20f)
            verticalLineTo(4f)
            quadTo(4f, 3.17f, 4.59f, 2.59f)
            reflectiveQuadTo(6f, 2f)
            horizontalLineToRelative(8f)
            lineToRelative(6f, 6f)
            verticalLineToRelative(6f)
            horizontalLineTo(15f)
            verticalLineToRelative(8f)
            horizontalLineTo(6f)
            close()
          }
        }
        .build()
    return _fileOpen!!
  }

private var _fileOpen: ImageVector? = null
