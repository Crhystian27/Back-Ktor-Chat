package com.zoan.routes

import com.zoan.room.MemberAlreadyExistsException
import com.zoan.room.RoomController
import com.zoan.session.ChatSession
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.chatSocket(roomController: RoomController){
    webSocket("/chat-socket") {
        val session = call.sessions.get<ChatSession>()
        if(session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
        }
        try{
            roomController.onJoin(
                username =  session.username,
                sessionId = session.sessionId,
                socket = this
            )
            incoming.consumeEach { frame ->
                if(frame is Frame.Text){
                    roomController.sendMessage(
                        senderUsername = session.username,
                        message = frame.readText()
                    )
                }
            }
        }catch (e: MemberAlreadyExistsException){
            call.respond(HttpStatusCode.Conflict)
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            roomController.tryDisconnet(session.username)
        }

    }
}

fun Route.getAllMessage(roomController: RoomController){
    get("/messages"){
        call.respond(
            HttpStatusCode.OK,
            roomController.getAllMessages()
        )
    }
}