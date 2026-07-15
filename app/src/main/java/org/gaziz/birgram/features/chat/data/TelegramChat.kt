package org.gaziz.birgram.features.chat.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.core.telegram.mapper.toFileData
import org.gaziz.birgram.core.telegram.mapper.toPhotoInfo
import org.gaziz.birgram.core.telegram.mapper.toStatus
import org.gaziz.birgram.core.telegram.model.ChatPhoto
import org.gaziz.birgram.core.telegram.model.ChatType
import org.gaziz.birgram.features.chat.data.mapper.toChatData
import org.gaziz.birgram.features.chat.data.mapper.toMessageData
import org.gaziz.birgram.features.chat.domain.model.ChatData
import org.gaziz.birgram.features.chat.domain.model.MessageData
import org.gaziz.birgram.features.chat.domain.repository.ChatRepository
import javax.inject.Inject

class TelegramChat @Inject constructor(
    private val manager: TelegramManager
): ChatRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            collectUpdates()
        }
    }

    private suspend fun collectUpdates(){
        manager.update.collect { u ->
            when(u){
                is TdApi.UpdateMessageSendSucceeded -> {
                    _messages.update { map ->
                        val newMap = map.toMutableMap()
                        newMap[u.message.id] = u.message.toMessageData()
                        newMap.toMap()
                    }
                }

                is TdApi.UpdateNewMessage -> {
                    if(!u.message.isOutgoing) {
                        _messages.update { map ->
                            val newMap = map.toMutableMap()
                            newMap[u.message.id] = u.message.toMessageData()
                            newMap.toMap()
                        }
                    }
                }

                is TdApi.UpdateNewChat -> {
                    val chat = u.chat.toChatData()
                    _chats.update { map ->
                        map.toMutableMap().apply { put(chat.id, chat) }.toMap()
                    }
                }

                is TdApi.UpdateChatPhoto -> {
                    _chats.update {
                        val newMap = it.toMutableMap()
                        val chat = newMap[u.chatId]
                        if(chat != null) {
                            newMap[u.chatId] = chat.copy(photo = u.photo.toPhotoInfo())
                        }
                        newMap.toMap()
                    }
                }

                is TdApi.UpdateFile -> {
                    if(u.file.local.isDownloadingCompleted) {
                        _chats.update { map ->
                            val newMap = map.toMutableMap()
                            for(chat in newMap.map { it.value }){
                                if(chat.photo != null) {
                                    if(chat.photo.small.id == u.file.id) {
                                        newMap[chat.id] = chat.copy(
                                            photo = ChatPhoto(
                                                chat.photo.miniThumbnail,
                                                u.file.toFileData()
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

                is TdApi.UpdateBasicGroup -> {
                    manager.sendRequest(
                        TdApi.CreateBasicGroupChat().apply {
                            this.basicGroupId = u.basicGroup.id
                            this.force = false
                        },
                        {}
                    ) {
                        if(it is TdApi.Chat){
                            val respChat = it.toChatData(it.permissions.canSendBasicMessages)
                            _chats.update {
                                val newMap = it.toMutableMap()
                                val chat = newMap[respChat.id]
                                if(chat != null) {
                                    newMap[respChat.id] = chat.copy(
                                        type = ChatType.BasicGroup(u.basicGroup.id,u.basicGroup.memberCount)
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
                            this.supergroupId = u.supergroup.id
                            this.force = false
                        },
                        {}
                    ) {
                        if(it is TdApi.Chat){
                            val respChat = it.toChatData(it.permissions.canSendBasicMessages)
                            _chats.update {
                                val newMap = it.toMutableMap()
                                val chat = newMap[respChat.id]
                                val canSend = when (u.supergroup.status) {
                                    is TdApi.ChatMemberStatusCreator -> true
                                    is TdApi.ChatMemberStatusAdministrator -> {
                                        (u.supergroup.status as TdApi.ChatMemberStatusAdministrator).rights.canPostMessages
                                    }
                                    else -> false
                                }
                                if(chat != null) {
                                    newMap[respChat.id] = chat.copy(
                                        type = ChatType.SuperGroup(
                                            u.supergroup.id,
                                            u.supergroup.isChannel,
                                            u.supergroup.memberCount,
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
                            this.userId = u.userId
                            this.force = false
                        },
                        {}
                    ) { obj ->
                        if(obj is TdApi.Chat){
                            val respChat = obj.toChatData(obj.permissions.canSendBasicMessages)
                            _chats.update {
                                val newMap = it.toMutableMap()
                                val chat = newMap[respChat.id]
                                if(chat != null) {
                                    newMap[respChat.id] = chat.copy(type =
                                        ChatType.Private(
                                            userId = u.userId,
                                            userStatus = u.status.toStatus()
                                        )
                                    )
                                }
                                newMap.toMap()
                            }
                        }
                    }
                }
            }
        }
    }

    private val _chats = MutableStateFlow(emptyMap<Long, ChatData>())
    override val chats: StateFlow<Map<Long, ChatData>> = _chats.asStateFlow()

    private val _messages = MutableStateFlow(emptyMap<Long, MessageData>())
    override val messages: StateFlow<Map<Long, MessageData>> = _messages.asStateFlow()

    override fun setMessages(
        updFun: (Map<Long, MessageData>) -> Map<Long, MessageData>
    ) {
        _messages.update(updFun)
    }

    override fun openChat(
        chatId: Long,
        onOK: () -> Unit
    ) {
        manager.sendRequest(
            TdApi.OpenChat().apply { this.chatId = chatId },
        ) {
            if(it is TdApi.Ok) { onOK() }
        }
    }

    override fun closeChat(chatId: Long) {
        manager.sendRequest(
            TdApi.CloseChat().apply { this.chatId = chatId },
        )
    }

    override fun getChatMessages(
        chatId: Long,
        fromMessage: Long,
        onMessages: (List<MessageData>) -> Unit
    ) {
        manager.sendRequest(
            TdApi.GetChatHistory().apply {
                this.chatId = chatId
                this.fromMessageId = fromMessage
                this.offset = 0
                this.limit = 50
                this.onlyLocal = false
            },
            { onMessages(emptyList()) }
        ) { resp ->
            if(resp is TdApi.Messages) {
                val msgs = mutableListOf<MessageData>()
                resp.messages.forEach { msgs.add(it.toMessageData()) }
                onMessages(msgs.toList())
            } else {
                onMessages(emptyList())
            }
        }
    }

    override fun sendMessage(
        chatId: Long,
        content: String,
        onMessage: (MessageData?) -> Unit
    ) {
       manager.sendRequest(
           TdApi.SendMessage().apply {
               this.chatId = chatId
               this.topicId = null
               this.replyTo = null
               this.options = null
               this.replyMarkup = null
               this.inputMessageContent = TdApi.InputMessageText().apply {
                   this.text = TdApi.FormattedText().apply { this.text = content }
                   this.linkPreviewOptions = null
                   this.clearDraft = true
               }
           },
           { onMessage(null) }
       ){
           if(it is TdApi.Message){
               onMessage(it.toMessageData())
           } else {
               onMessage(null)
           }
       }
    }
}