package org.gaziz.birgram.core.telegram.data

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.core.telegram.data.mapper.fromUnixTimeStamp
import org.gaziz.birgram.core.telegram.data.mapper.toChatData
import org.gaziz.birgram.core.telegram.data.mapper.toChatPosition
import org.gaziz.birgram.core.telegram.data.mapper.toFileData
import org.gaziz.birgram.core.telegram.data.mapper.toLastMsgData
import org.gaziz.birgram.core.telegram.data.mapper.toMessageData
import org.gaziz.birgram.core.telegram.data.mapper.toPhotoInfo
import org.gaziz.birgram.core.telegram.data.mapper.toStatus
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatData
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatPhoto
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatPosition
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatType
import org.gaziz.birgram.core.telegram.domain.model.message.MessageContent
import org.gaziz.birgram.core.telegram.domain.model.message.MessageData
import org.gaziz.birgram.core.telegram.domain.repository.EventLoopRepository
import org.gaziz.birgram.features.auth.domain.model.AuthCodeInfo
import org.gaziz.birgram.features.auth.domain.model.AuthCodeType
import org.gaziz.birgram.features.auth.domain.model.AuthPasswordInfo
import org.gaziz.birgram.features.auth.domain.model.AuthState
import org.gaziz.birgram.features.auth.domain.model.CodeType
import javax.inject.Inject

class TelegramEventLoop @Inject constructor(
    private val manager: TelegramManager
): EventLoopRepository {

    private val _authState = MutableStateFlow<AuthState>(AuthState.WaitParams)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    override val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    override fun setErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }

    private val _chatList = MutableStateFlow(emptyMap<Long, ChatData>())
    override val chatList: StateFlow<Map<Long, ChatData>> = _chatList.asStateFlow()

    private val _messages = MutableStateFlow(emptyMap<Long, MessageData>())
    override val messages: StateFlow<Map<Long, MessageData>> = _messages.asStateFlow()

    override fun setMessages(
        updFun: (Map<Long, MessageData>) -> Map<Long, MessageData>
    ) {
        _messages.update(updFun)
    }

    override fun createEventLoop() {
        manager.createClient(
            { event ->
                when (event) {
                    is TdApi.Error -> {
                        Log.e(manager.getLogTag(), "${event.code}: ${event.message}")
                        setErrorMessage(event.message)
                    }

                    is TdApi.Ok -> {
                        setErrorMessage(null)
                    }

                    is TdApi.UpdateNewChat -> {
                        val chat = event.chat.toChatData()
                        _chatList.update { map ->
                            map.toMutableMap().apply { put(chat.id, chat) }.toMap()
                        }
                    }

                    is TdApi.UpdateChatPosition -> {
                        _chatList.update { map ->
                            val newMap = map.toMutableMap()
                            val chat = newMap[event.chatId]
                            if(chat != null) {
                                val position = event.position.toChatPosition()
                                if(position.order == 0L) {
                                   newMap.remove(event.chatId)
                                } else {
                                   newMap[event.chatId] = chat.copy(
                                       positions = chat.positions.filter {
                                           it.listType != position.listType
                                       } + position
                                   )
                                }
                            }
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateChatLastMessage -> {
                        _chatList.update { map ->
                            val newMap = map.toMutableMap()
                            val chat = newMap[event.chatId]
                            if(chat != null){
                                val positions = mutableListOf<ChatPosition>()
                                    .apply { event.positions.forEach { add(it.toChatPosition()) } }
                                    .toList()
                                val lastMsg = event.lastMessage.toLastMsgData()
                                newMap[event.chatId] = chat.copy(
                                    positions = positions,
                                    lastMessage = lastMsg ?: chat.lastMessage
                                )
                            }
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateMessageSendSucceeded -> {
                        _messages.update { map ->
                            val newMap = map.toMutableMap()
                            newMap[event.message.id] = event.message.toMessageData()
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateNewMessage -> {
                        if(!event.message.isOutgoing) {
                            _messages.update { map ->
                                val newMap = map.toMutableMap()
                                newMap[event.message.id] = event.message.toMessageData()
                                newMap.toMap()
                            }
                        }
                    }

                    is TdApi.UpdateChatDraftMessage -> {
                        _chatList.update { map ->
                            val newMap = map.toMutableMap()
                            var chat = newMap[event.chatId]
                            if(chat != null){
                                val positions = mutableListOf<ChatPosition>()
                                    .apply { event.positions.forEach { add(it.toChatPosition()) } }
                                    .toList()
                                chat = chat.copy(positions = positions)
                                val lastMessage = event.draftMessage
                                if(lastMessage != null) {
                                    chat = chat.copy(
                                        lastMessage = chat.lastMessage?.copy(
                                            date = lastMessage.date.fromUnixTimeStamp(),
                                            content = MessageContent.Draft
                                        )
                                    )
                                }
                                newMap[chat.id] = chat
                            }
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateChatPhoto -> {
                        _chatList.update {
                            val newMap = it.toMutableMap()
                            val chat = newMap[event.chatId]
                            if(chat != null) {
                                newMap[event.chatId] = chat.copy(photo = event.photo.toPhotoInfo())
                            }
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateFile -> {
                        if(event.file.local.isDownloadingCompleted) {
                            _chatList.update { map ->
                                val newMap = map.toMutableMap()
                                for(chat in newMap.map { it.value }){
                                    if(chat.photo != null) {
                                        if(chat.photo.small.id == event.file.id) {
                                            newMap[chat.id] = chat.copy(
                                                photo = ChatPhoto(
                                                    chat.photo.miniThumbnail,
                                                    event.file.toFileData()
                                                )
                                            )
                                            break
                                        }
                                    }
                                }
                                newMap.toMap()
                            }
                        }
                    }

                    is TdApi.UpdateChatReadInbox -> {
                        _chatList.update {
                            val newMap = it.toMutableMap()
                            newMap[event.chatId]?.let { chat ->
                                newMap[event.chatId] = chat.copy(unreadCount = event.unreadCount)
                            }
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateChatUnreadMentionCount -> {
                        _chatList.update {
                            val newMap = it.toMutableMap()
                            newMap[event.chatId]?.let { chat ->
                                newMap[event.chatId] = chat.copy(unreadCount = event.unreadMentionCount)
                            }
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateChatUnreadReactionCount -> {
                        _chatList.update {
                            val newMap = it.toMutableMap()
                            newMap[event.chatId]?.let { chat ->
                                newMap[event.chatId] = chat.copy(unreadCount = event.unreadReactionCount)
                            }
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateBasicGroup -> {
                        manager.sendRequest(
                            TdApi.CreateBasicGroupChat().apply {
                                this.basicGroupId = event.basicGroup.id
                                this.force = false
                            },
                            {}
                        ) {
                            if(it is TdApi.Chat){
                                val respChat = it.toChatData()
                                _chatList.update {
                                    val newMap = it.toMutableMap()
                                    val chat = newMap[respChat.id]
                                    if(chat != null) {
                                        newMap[respChat.id] = chat.copy(
                                            type = ChatType.BasicGroup(event.basicGroup.id,event.basicGroup.memberCount)
                                        )
                                    }
                                    newMap.toMap()
                                }
                            }
                        }
                    }

                    is TdApi.UpdateSupergroup -> {
                        manager.sendRequest(
                            TdApi.CreateSupergroupChat().apply {
                                this.supergroupId = event.supergroup.id
                                this.force = false
                            },
                            {}
                        ) {
                            if(it is TdApi.Chat){
                                val respChat = it.toChatData()
                                _chatList.update {
                                    val newMap = it.toMutableMap()
                                    val chat = newMap[respChat.id]
                                    val canSend = when (event.supergroup.status) {
                                        is TdApi.ChatMemberStatusCreator -> true
                                        is TdApi.ChatMemberStatusAdministrator -> {
                                            (event.supergroup.status as TdApi.ChatMemberStatusAdministrator).rights.canPostMessages
                                        }
                                        else -> false
                                    }
                                    if(chat != null) {
                                        newMap[respChat.id] = chat.copy(
                                            type = ChatType.SuperGroup(
                                                event.supergroup.id,
                                                event.supergroup.isChannel,
                                                event.supergroup.memberCount,
                                                canSend
                                            )
                                        )
                                    }
                                    newMap.toMap()
                                }
                            }
                        }
                    }

                    is TdApi.UpdateUserStatus -> {
                        manager.sendRequest(
                            TdApi.CreatePrivateChat().apply {
                                this.userId = event.userId
                                this.force = false
                            },
                            {}
                        ) {
                            if(it is TdApi.Chat){
                                val respChat = it.toChatData()
                                _chatList.update {
                                    val newMap = it.toMutableMap()
                                    val chat = newMap[respChat.id]
                                    if(chat != null) {
                                        newMap[respChat.id] = chat.copy(type =
                                            ChatType.Private(
                                                userId = event.userId,
                                                userStatus = event.status.toStatus()
                                            )
                                        )
                                    }
                                    newMap.toMap()
                                }
                            }
                        }

                        _chatList.update { map ->
                            val newMap = map.toMutableMap()
                            for(chat in newMap) {
                                val type = chat.value.type
                                if(type is ChatType.Secret) {
                                    if(type.userId == event.userId) {
                                        newMap[chat.key] = chat.value.copy(
                                            type = type.copy(userStatus = event.status.toStatus())
                                        )
                                        break
                                    }
                                }
                            }
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateAuthorizationState -> {
                        _authState.value = when (event.authorizationState) {
                            is TdApi.AuthorizationStateWaitTdlibParameters -> AuthState.WaitParams
                            is TdApi.AuthorizationStateWaitPhoneNumber -> AuthState.WaitPhoneNumber
                            is TdApi.AuthorizationStateWaitCode -> {
                                val codeInfo = (event.authorizationState as TdApi.AuthorizationStateWaitCode).codeInfo
                                val codeType: (TdApi.AuthenticationCodeType) -> AuthCodeType = {
                                    when(it) {
                                        is TdApi.AuthenticationCodeTypeCall -> AuthCodeType(
                                            CodeType.Call,
                                            it.length
                                        )
                                        is TdApi.AuthenticationCodeTypeTelegramMessage -> AuthCodeType(
                                            CodeType.Telegram,
                                            it.length
                                        )
                                        is TdApi.AuthenticationCodeTypeSms -> AuthCodeType(
                                            CodeType.SMS,
                                            it.length
                                        )
                                        is TdApi.AuthenticationCodeTypeFlashCall -> AuthCodeType(
                                            CodeType.FlashCall,
                                            manager.getDefaultCodeLength()
                                        )
                                        is TdApi.AuthenticationCodeTypeMissedCall -> AuthCodeType(
                                            CodeType.MissedCall,
                                            it.length
                                        )
                                        is TdApi.AuthenticationCodeTypeFragment -> AuthCodeType(
                                            CodeType.Fragment,
                                            it.length
                                        )
                                        is TdApi.AuthenticationCodeTypeFirebaseAndroid -> AuthCodeType(
                                            CodeType.FireBaseAndroid,
                                            it.length
                                        )
                                        is TdApi.AuthenticationCodeTypeFirebaseIos -> AuthCodeType(
                                            CodeType.FireBaseIos,
                                            it.length
                                        )
                                        else -> AuthCodeType(
                                            CodeType.Other,
                                            manager.getDefaultCodeLength()
                                        )
                                    }
                                }
                                AuthState.WaitCode(
                                    AuthCodeInfo(
                                        type = codeType(codeInfo.type),
                                        nextType = if (codeInfo.nextType != null) codeType(codeInfo.nextType!!) else null,
                                        timeout = codeInfo.timeout
                                    )
                                )
                            }
                            is TdApi.AuthorizationStateWaitPassword -> AuthState.WaitPassword(
                                AuthPasswordInfo(
                                    (event.authorizationState as TdApi.AuthorizationStateWaitPassword).passwordHint
                                )
                            )
                            is TdApi.AuthorizationStateReady -> AuthState.Ready

                            is TdApi.AuthorizationStateLoggingOut -> AuthState.LoggingOut
                            is TdApi.AuthorizationStateClosing -> AuthState.LoggingOut
                            is TdApi.AuthorizationStateClosed -> AuthState.Closed

                            else -> AuthState.Other(event.authorizationState.toString())
                        }
                    }

                }
            },
            { throwable ->
                val message = throwable.localizedMessage ?: throwable.message ?: "unknown update handler exception"
                Log.e(manager.getLogTag(), message)
                setErrorMessage(message)
            },
        )
    }

    override fun restartAuth() {
        _authState.value = AuthState.WaitPhoneNumber
    }

    override fun logOut(onOk: () -> Unit) {
        manager.sendRequest(
            TdApi.LogOut(),
            {},
        ) {
            if(it is TdApi.Ok) {
                _chatList.value = emptyMap()
                _messages.value = emptyMap()
                onOk()
            }
        }
    }
}