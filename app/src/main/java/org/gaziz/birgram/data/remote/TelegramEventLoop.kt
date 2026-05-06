package org.gaziz.birgram.data.remote

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.auth.AuthCodeInfo
import org.gaziz.birgram.domain.model.auth.AuthCodeType
import org.gaziz.birgram.domain.model.auth.AuthPasswordInfo
import org.gaziz.birgram.domain.model.auth.AuthState
import org.gaziz.birgram.domain.model.auth.CodeType
import org.gaziz.birgram.domain.model.chatList.ChatData
import org.gaziz.birgram.domain.model.chatList.ChatListType
import org.gaziz.birgram.domain.model.chatList.ChatPhoto
import org.gaziz.birgram.domain.model.chatList.ChatPosition
import org.gaziz.birgram.domain.model.chatList.MessageData
import org.gaziz.birgram.domain.repository.EventLoopRepository
import javax.inject.Inject

class TelegramEventLoop @Inject constructor(private val manager: TelegramManager): EventLoopRepository {

    private companion object {
        private const val DEFAULT_CODE_LENGTH = 5
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.WaitParams)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    override val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    override fun setErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }

    private val _chatList = MutableStateFlow(emptyMap<Long,ChatData>())
    override val chatList: StateFlow<Map<Long,ChatData>> = _chatList.asStateFlow()

    private val toChatListType: (TdApi.ChatList) -> ChatListType = {
        when(it){
            is TdApi.ChatListArchive -> ChatListType.Archive
            is TdApi.ChatListFolder -> ChatListType.Folder(it.chatFolderId)
            else -> ChatListType.Main
        }
    }

    private val toChatPositon: (TdApi.ChatPosition) -> ChatPosition = {
        ChatPosition(
            listType = toChatListType(it.list),
            order = it.order,
            isPinned = it.isPinned
        )
    }

    override fun createEventLoop() {
        manager.createClient(
            { event ->
                when (event) {
                    is TdApi.Error -> {
                        Log.e("TDLib", "${event.code}: ${event.message}")
                        setErrorMessage(event.message)
                    }

                    is TdApi.Ok -> {
                        setErrorMessage(null)
                    }

                    is TdApi.UpdateNewChat -> {
                        val chat = event.chat
                        val chatPhoto = ChatPhoto(
                            chat.photo?.minithumbnail?.data
                        )
                        val chatPositions = mutableListOf<ChatPosition>()
                            .apply {
                                chat.positions.forEach { add(toChatPositon(it)) }
                            }
                            .toList()
                        _chatList.update { map ->
                            map.toMutableMap().apply {
                                put(
                                    chat.id,
                                    ChatData(
                                        id = chat.id,
                                        title = chat.title,
                                        photo = chatPhoto,
                                        lastMessage = MessageData(""),
                                        positions = chatPositions,
                                        unreadCount = chat.unreadCount,
                                        mentionCount = chat.unreadMentionCount,
                                        reactionCount = chat.unreadReactionCount
                                    )
                                )
                            }.toMap()
                        }
                    }

                    is TdApi.UpdateChatPosition -> {
                        _chatList.update { map ->
                            val newMap = map.toMutableMap()
                            val chat = newMap[event.chatId]
                            if(chat != null) {
                                val position = toChatPositon(event.position)
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
                                    .apply { event.positions.forEach { add(toChatPositon(it)) } }
                                    .toList()
                                newMap[event.chatId] = chat.copy(positions = positions)
                            }
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateChatDraftMessage -> {
                        _chatList.update { map ->
                            val newMap = map.toMutableMap()
                            val chat = newMap[event.chatId]
                            if(chat != null){
                                val positions = mutableListOf<ChatPosition>()
                                    .apply { event.positions.forEach { add(toChatPositon(it)) } }
                                    .toList()
                                newMap[event.chatId] = chat.copy(positions = positions)
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
                                        is TdApi.AuthenticationCodeTypeCall -> AuthCodeType(CodeType.Call,it.length)
                                        is TdApi.AuthenticationCodeTypeTelegramMessage -> AuthCodeType(CodeType.Telegram,it.length)
                                        is TdApi.AuthenticationCodeTypeSms -> AuthCodeType(CodeType.SMS,it.length)
                                        is TdApi.AuthenticationCodeTypeFlashCall -> AuthCodeType(CodeType.FlashCall,DEFAULT_CODE_LENGTH)
                                        is TdApi.AuthenticationCodeTypeMissedCall -> AuthCodeType(CodeType.MissedCall,it.length)
                                        is TdApi.AuthenticationCodeTypeFragment -> AuthCodeType(CodeType.Fragment,it.length)
                                        is TdApi.AuthenticationCodeTypeFirebaseAndroid -> AuthCodeType(CodeType.FireBaseAndroid,it.length)
                                        is TdApi.AuthenticationCodeTypeFirebaseIos -> AuthCodeType(CodeType.FireBaseIos,it.length)
                                        else -> AuthCodeType(CodeType.Other,DEFAULT_CODE_LENGTH)
                                    }
                                }
                                AuthState.WaitCode(
                                    AuthCodeInfo(
                                        type = codeType(codeInfo.type),
                                        nextType = if(codeInfo.nextType != null) codeType(codeInfo.nextType!!) else null,
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
                            else -> AuthState.Other(event.authorizationState.toString())
                        }
                    }

                }
            },
            { throwable ->
                val message = throwable.localizedMessage ?: throwable.message ?: "unknown update handler exception"
                Log.e("TDLib", message)
                setErrorMessage(message)
            },
        )
    }

    override fun restartAuth() {
        _authState.value = AuthState.WaitPhoneNumber
    }
}