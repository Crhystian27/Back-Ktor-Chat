package com.zoan.data

import com.zoan.data.model.Message

interface MessageDataSource {

    suspend fun getAllMessage(): List<Message>

    suspend fun insertMessage(message: Message)
}