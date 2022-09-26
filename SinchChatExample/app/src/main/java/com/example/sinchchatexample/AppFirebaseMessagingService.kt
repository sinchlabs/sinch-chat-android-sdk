package com.example.sinchchatexample

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sinch.chat.sdk.SinchChatSDK

class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        SinchChatSDK.push.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        if (SinchChatSDK.push.onMessageReceived(p0)) {
            // push handled by SinchChatSDK
        }
    }
}
