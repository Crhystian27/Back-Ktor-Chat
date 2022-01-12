package com.zoan.plugins

import com.zoan.room.RoomController
import com.zoan.routes.chatSocket
import com.zoan.routes.getAllMessage
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val roomController by inject<RoomController>()
    install(Routing){
        chatSocket(roomController)
        getAllMessage(roomController)
    }
}
