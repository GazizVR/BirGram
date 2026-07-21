package org.gaziz.birgram.core.telegram.internal.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.AccentColor

fun TdApi.AccentColor.toAccentColor(): AccentColor {
    return AccentColor(
        id = this.id,
        lightThemeColors = this.lightThemeColors.toList(),
        darkThemeColors = this.darkThemeColors.toList()
    )
}