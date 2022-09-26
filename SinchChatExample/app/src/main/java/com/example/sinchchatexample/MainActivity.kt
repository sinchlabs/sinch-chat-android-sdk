package com.example.sinchchatexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import com.sinch.chat.sdk.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                initializeSinchChatSDK(task.result)
//            } else {
                initializeSinchChatSDK(null)
//            }
                setIdentity()
//        }

    }

    private fun initializeSinchChatSDK(deviceToken: String?) {
        val options = SinchInitializationOptions(deviceToken)
        SinchChatSDK.initialize(applicationContext, options)
    }

    private fun setIdentity() {
        // self signed
        val secret = "01GBSYFFG7QYV67KYYNHTZH5XZ"
        val clientID = "01GBSYKGQTQVQYS7009CBGH4TZ"
        val projectID = "d36083fc-1323-47b0-82e3-e317c4605030"
        val configID = "{{config_id}}"

        val internalUserID = "piotr"
        val signedUserID = HmacSha512.generate(internalUserID, secret)
        val signedIdentity = SinchIdentity.SelfSigned(internalUserID, signedUserID)

        val config = SinchConfig(clientID, projectID, configID, SinchRegion.EU1, "")


        SinchChatSDK.setIdentity(config, signedIdentity) { result ->
            if (result.isSuccess) {
                showChat()
            } else {
                print(result.exceptionOrNull())
            }
        }

    }

    private fun showChat() {
        val options = prepareOptionsForEnd2EndChat(
            "order_id1",
            "piotr",
            "Piotr",
            "https://s3-symbol-logo.tradingview.com/sinch--600.png"
        )

        SinchChatSDK.chat.start(this, options)
    }

    private fun prepareOptionsForEnd2EndChat(
        orderID: String,
        toWhoIWantToSpeakID: String,
        myName: String?,
        myPictureUrl: String?
    ): SinchStartChatOptions {
        var metadata = mutableListOf<SinchMetadata>()

        metadata.add(SinchMetadata.Custom("addressee", toWhoIWantToSpeakID, SinchMetadataMode.EachMessage))

        myName?.let {
            metadata.add(SinchMetadata.Custom("senderDisplayName", it, SinchMetadataMode.EachMessage))
        }

        myPictureUrl?.let {
            metadata.add(SinchMetadata.Custom("senderPictureUrl", it, SinchMetadataMode.EachMessage))
        }

        return SinchStartChatOptions(orderID, metadata)
    }
}

object HmacSha512 {

    fun generate(message: String, key: String): String {
        val mac: Mac = Mac.getInstance("HmacSHA512")
        mac.init(SecretKeySpec(key.encodeToByteArray(), mac.algorithm))
        return mac.doFinal(message.encodeToByteArray()).joinToString(separator = "") { "%02x".format(it) }
    }
}
