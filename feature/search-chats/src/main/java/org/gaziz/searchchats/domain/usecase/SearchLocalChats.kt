package org.gaziz.searchchats.domain.usecase

import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.GroupService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.ui.model.ChatTypeUiState
import org.gaziz.birgram.core.telegram.ui.provider.ChatAvatarProvider
import org.gaziz.searchchats.domain.model.SearchedItem
import org.gaziz.searchchats.domain.repository.ChatSearchRepository
import javax.inject.Inject

class SearchLocalChats @Inject constructor(
    private val chatService: ChatService,
    private val chatSearchRepository: ChatSearchRepository,
    private val userService: UserService,
    private val groupService: GroupService,
    private val chatAvatarProvider: ChatAvatarProvider
) {
    operator fun invoke(
        query: String,
        limit: Int
    ) {
        chatService.searchChatsLocal(
            query,
            limit
        ) { chatsById ->
            val result = chatsById.mapValues { e ->
                val chat = e.value
                val typeInfo: ChatTypeUiState? = when(val type = chat.type) {
                    is ChatType.BasicGroup -> {
                        val group = groupService.basicGroups.value[type.groupId]
                        if(group != null) {
                            ChatTypeUiState.BasicGroup(
                                memberCount = group.memberCount,
                            )
                        } else {
                            null
                        }
                    }
                    is ChatType.Private -> {
                        val user = userService.users.value[type.userId]
                        if(user != null) {
                            ChatTypeUiState.User(
                                status = user.status,
                                isBot = user.type is UserType.Bot
                            )
                        } else {
                            null
                        }
                    }
                    is ChatType.Secret -> {
                        val user = userService.users.value[type.userId]
                        if(user != null) {
                            ChatTypeUiState.User(
                                status = user.status,
                                isBot = user.type is UserType.Bot
                            )
                        } else {
                            null
                        }
                    }
                    is ChatType.SuperGroup -> {
                        val group = groupService.superGroups.value[type.groupId]
                        if(group != null) {
                            ChatTypeUiState.SuperGroup(
                                memberCount = group.memberCount,
                                isChannel = type.isChannel
                            )
                        } else {
                            null
                        }
                    }
                    else -> null
                }
                val avatar = chatAvatarProvider(chat,userService.users.value)
                SearchedItem(
                    title = chat.title,
                    avatar = avatar,
                    typeInfo = typeInfo
                )
            }
            chatSearchRepository.replace(result)
        }
    }
}