package org.gaziz.birgram.core.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val skull: ImageVector
  get() {
    if (_skull != null) {
      return _skull!!
    }
    _skull =
      ImageVector.Builder(
        name = "skull",
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
            moveTo(6f, 22f)
            verticalLineTo(17.75f)
            quadTo(5.03f, 17.33f, 4.29f, 16.61f)
            reflectiveQuadTo(3.04f, 15f)
            reflectiveQuadTo(2.26f, 13.08f)
            reflectiveQuadTo(2f, 11f)
            quadTo(2f, 7.05f, 4.8f, 4.52f)
            reflectiveQuadTo(12f, 2f)
            reflectiveQuadToRelative(7.2f, 2.52f)
            reflectiveQuadTo(22f, 11f)
            quadToRelative(0f, 1.05f, -0.26f, 2.07f)
            reflectiveQuadTo(20.96f, 15f)
            reflectiveQuadToRelative(-1.25f, 1.61f)
            reflectiveQuadTo(18f, 17.75f)
            verticalLineTo(22f)
            horizontalLineTo(6f)
            close()
            moveTo(8f, 20f)
            horizontalLineTo(9f)
            verticalLineTo(18f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(2f)
            verticalLineTo(18f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(1f)
            verticalLineTo(16.45f)
            quadToRelative(0.95f, -0.22f, 1.69f, -0.75f)
            quadToRelative(0.74f, -0.53f, 1.25f, -1.25f)
            reflectiveQuadToRelative(0.79f, -1.6f)
            reflectiveQuadTo(20f, 11f)
            quadTo(20f, 7.88f, 17.79f, 5.94f)
            reflectiveQuadTo(12f, 4f)
            quadTo(8.43f, 4f, 6.21f, 5.94f)
            reflectiveQuadTo(4f, 11f)
            quadToRelative(0f, 0.97f, 0.28f, 1.85f)
            reflectiveQuadToRelative(0.79f, 1.6f)
            reflectiveQuadTo(6.33f, 15.7f)
            reflectiveQuadTo(8f, 16.45f)
            verticalLineTo(20f)
            close()
            moveToRelative(2.5f, -5f)
            horizontalLineToRelative(3f)
            lineTo(12f, 12f)
            lineToRelative(-1.5f, 3f)
            close()
            moveToRelative(-2f, -2f)
            quadToRelative(0.83f, 0f, 1.41f, -0.59f)
            reflectiveQuadTo(10.5f, 11f)
            quadToRelative(0f, -0.83f, -0.59f, -1.41f)
            reflectiveQuadTo(8.5f, 9f)
            quadTo(7.68f, 9f, 7.09f, 9.59f)
            reflectiveQuadTo(6.5f, 11f)
            reflectiveQuadToRelative(0.59f, 1.41f)
            reflectiveQuadTo(8.5f, 13f)
            close()
            moveToRelative(7f, 0f)
            quadToRelative(0.82f, 0f, 1.41f, -0.59f)
            reflectiveQuadTo(17.5f, 11f)
            quadToRelative(0f, -0.83f, -0.59f, -1.41f)
            reflectiveQuadTo(15.5f, 9f)
            reflectiveQuadTo(14.09f, 9.59f)
            reflectiveQuadTo(13.5f, 11f)
            reflectiveQuadToRelative(0.59f, 1.41f)
            reflectiveQuadTo(15.5f, 13f)
            close()
            moveTo(12f, 20f)
            close()
          }
        }
        .build()
    return _skull!!
  }

private var _skull: ImageVector? = null
