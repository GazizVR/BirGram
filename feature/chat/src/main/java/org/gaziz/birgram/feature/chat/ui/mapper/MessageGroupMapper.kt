package org.gaziz.birgram.feature.chat.ui.mapper

import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.core.telegram.api.model.message.MessageProperties
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.feature.chat.ui.model.MessageUiState
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class MessageGroupMapper @Inject constructor(
    private val messageUiMapper: MessageUiMapper
) {
    private fun LocalDate.formatMonthDay(locale: Locale = Locale.getDefault()): String {
        val currentYear = Year.now().value

        val formatter = if (year == currentYear) {
            DateTimeFormatter.ofPattern("MMMM d", locale)
        } else {
            DateTimeFormatter.ofPattern("MMMM d, yyyy", locale)
        }

        return format(formatter)
    }
    fun map(
        messagesById: Map<LocalDate,List<Message>>,
        chatsById: Map<Long, Chat>,
        usersById: Map<Long, User>,
        propertiesById: Map<Long, MessageProperties>
    ): Map<String,List<MessageUiState>> {
        return messagesById.entries.associate { (key,value) ->
            val messages = value.mapIndexed { ind, msg ->
                messageUiMapper.map(
                    msg = msg,
                    prevMsg = value.getOrNull(ind-1),
                    nextMsg = value.getOrNull(ind+1),
                    chatsById = chatsById,
                    usersById = usersById,
                    propertiesById = propertiesById
                )
            }
            key.formatMonthDay() to messages
        }
    }
}