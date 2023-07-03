
# Android Sinch SDK - Getting Started

  

This is documentation how to easily implement our SDK for chat and for push features.

  

# Requirements

  

- Android 5.0 Lollipop - min SDK 21

  

# Installation

  

### Maven

  

settings.gradle

```

maven {
	url "https://raw.githubusercontent.com/sinchlabs/sinch-chat-android-sdk/master/releases"
	credentials(HttpHeaderCredentials) {
		name = "Authorization"
		value = "Bearer {your github personal token}"
	}
	authentication {
		header(HttpHeaderAuthentication)
	}
}

```

  

build.gradle

```

implementation 'com.sinch.chat.sdk:sinch-sdk:{version}'

```

  

# First steps

  

1. Initialize SDK as soon as application is starting.

2. SetIdentity as soon as you can authenticate the user using your internal ****UserID**** and sign it using algorithm on ****the backend side.**** (You can sign it on mobile but it is ****NOT RECOMMENDED****) ← this method can be called multiple times.

3. Setup push notifications.

4. Show chat.

  

## Initialize

  

1. Add Activity to `AndroidManifest.xml`

  

```kotlin

<activity 
    android:name="com.sinch.chat.sdk.SinchChatActivity"
    android:configChanges="orientation|screenSize|screenLayout|keyboardHidden"
/>

```

  

Call method `initialize`

  

```swift

SinchSDK.initialize()

```

  

As soon as possible it means the best place to do that is ****MainActivity**** in:

  

```swift

override fun onCreate(savedInstanceState: Bundle?)

```

  

<aside>

💡 If you will want to use push notification look into Push notification section.

  

</aside>

  

Example:

  

```kotlin
SinchChatSDK.initialize(applicationContext)
```

  

### Config

  

We need to have configuration for chat which we’re providing to you it means you need:

  

- Region

- Project ID

- Client ID

- Config ID (provide "" if you don't have it)

- *_(Optional)_* Token secret

  

## Set identity

  

The second step is that we need to authorize user so we need you to call us this method as soon as you can authorize the user. You can call this method as many times as you want:

  

```kotlin

fun setIdentity(config: SinchConfig, identity: SinchIdentity, completion: (Result<Unit>) -> Unit)

```

  

Example:

```kotlin

val currentIdentity = SinchIdentity.SelfSigned({your user_id}, {signed token})
val currentEnvironment = SinchConfig({your client_id}, {your project_id}, {you config_id}, {your region}, {language})

SinchChatSDK.setIdentity(  
    currentEnvironment,  
    currentIdentityType  
) {  
  if (it.isSuccess) {  
        Toast.makeText(this, "Set Identity success", Toast.LENGTH_LONG).show()  
        Log.d(TAG, "Set Identity success")  
    } else {  
        Toast.makeText(this, "Set Identity failed", Toast.LENGTH_LONG).show()  
        Log.d(TAG, "Set Identity failed with message: ${it.exceptionOrNull()?.message}")  
    }  
}

```

#### What is Self Signed token?

We have to recognise your users somehow that's why we need to have signed token.
Write to us if you want to know algorithm how we're signing user_id. 
**It is NOT recommended to sign user_id on mobile app.**


And now you can use SDK functionalities like chat or push.

  

# Chat

  

## Check chat availability

  

To check if the chat is available for user you should use method:

  

```kotlin
// Possible errors:
// - sdk_not_initialized - means that you need to perform `initialize` method firstly and then `setIdentity`.
// - chat_already_started - means that you cannot have two chats opened.

fun isChatAvailable(): Result<Unit>

```

  

## Showing chat

  

****Before showing the chat make sure you have initialised this SDK and performed method setIdentity.****


To start the chat there is method in `SinchChatSDK.chat.*`

```kotlin
fun start(context: Context): Result<Unit>
```

  

<aside>
💡 Result is returning information if the chat was shown correctly.
</aside>

****Example:****

```kotlin

do {

val result = SinchChatSDK.chat.start(this)
if (result.isFailure) {
	// do somethings.
}
```

# Push

### Firebase token

  

To use ****push notifications**** with this SDK you have to provide ****Firebase token**** and upload it to our Sinch Panel.

  

<aside>
💡 Push notification works via Firebase service so you need to make sure we have your server token.
</aside>

#### Initialization SDK with push notification token

Example:
```kotlin
FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->  

	val options = SinchInitializationOptions(null)  
	if (task.isSuccessful) {  
		options.pushDeviceToken = task.result  
	}
    SinchChatSDK.initialize(applicationContext, options)  
}
```

### Setup Firebase Messaging service

1. Add those dependencies to your app in ****build.gradle****:

```kotlin
implementation platform('com.google.firebase:firebase-bom:{version}')
implementation 'com.google.firebase:firebase-messaging:{version}'
implementation 'com.google.firebase:firebase-analytics-ktx'
```

1. ****AndroidManifest.xml**** add new service for push notifications;

```kotlin
<service
	android:name=".AppFirebaseMessagingService"
	android:exported="false">
	<intent-filter>
		<action android:name="com.google.firebase.MESSAGING_EVENT" />
	</intent-filter>
</service>
```
1. Create file called: `AppFirebaseMessagingService` to match `android:name` and paste there this code:

```kotlin
class AppFirebaseMessagingService : FirebaseMessagingService() {

	override fun onNewToken(p0: String) {
		super.onNewToken(p0)
		SinchChatSDK.push.onNewToken(p0)
	}

	override fun onMessageReceived(p0: RemoteMessage) {
		super.onMessageReceived(p0)
		if (SinchChatSDK.push.onMessageReceived(p0)) {
			// This notification belongs to SinchChatSDK.
			return
		}
	}
}
```

### Start conversation when starting the Chat.

If you want to initialise conversation for you user, then you have to start chat with following option like in example below:

```
val options = SinchStartChatOptions(shouldInitializeConversation = true)
SinchChatSDK.chat.start(this, options)
```


#### Troubleshooting

You may found issue with building your application. It is related to dependencies which we're using which are related to Protobuf. 

Paste those lines in your android {} in build.gradle.


```
android {
    ... 

    configurations {
        implementation.exclude module:'protolite-well-known-types'
        implementation.exclude module:'protobuf-lite'
    }
}

```




  
