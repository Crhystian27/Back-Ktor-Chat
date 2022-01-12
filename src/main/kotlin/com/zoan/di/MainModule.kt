package com.zoan.di

import com.zoan.data.MessageDataSource
import com.zoan.data.MessageDataSourceImpl
import com.zoan.room.RoomController
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("message_db_yt")
    }
    single<MessageDataSource>{
        MessageDataSourceImpl(get())
    }
    single{
        RoomController(get())
    }
}