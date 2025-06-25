
----------

## `SinchAdvancedChatHandler` Documentation

The `SinchAdvancedChatHandler` class provides a set of functionalities to customize and control the behavior of the Sinch chat interface, including handling location messages and managing message sending capabilities.

### Class Overview

This class allows you to:

-   **Handle Location Messages**: Intercept and customize the behavior when a location message button is tapped.
-   **Enable/Disable Message Sending**: Control whether users can send messages within the chat.

----------

### Properties

-   `locationMessageHandler`:
    -   **Type**: `((context: Context, model: SinchLocationMessageHandlerModel, callback: (SinchAdvancedLocationMessageResult) -> Unit) -> Unit)?`
    -   A callback that is invoked when a location message is received. You can set a custom handler to process location data and decide whether to stop or continue the default propagation (e.g., opening a map application).
-   `onViewResumeHandler`:
    -   **Type**: `((context: Context, chatOptions: SinchStartChatOptions?) -> Unit)?`
    -   A callback that is invoked when the chat view is resumed. This can be used to perform actions or update the UI when the chat comes to the foreground.
-   `enableSendingMessagesHandler`:
    -   **Type**: `(() -> Unit)?`
    -   An internal callback that is invoked when `enableSendingMessages()` is called.
-   `disableSendingMessagesHandler`:
    -   **Type**: `(() -> Unit)?`
    -   An internal callback that is invoked when `disableSendingMessages()` is called.
-   `isSendingMessagesEnabled`:
    -   **Type**: `Boolean`
    -   **Default**: `true`
    -   A flag indicating whether sending messages is currently enabled.

----------

### Methods

#### `setLocationMessageHandler(handler: ((context: Context, model: SinchLocationMessageHandlerModel, callback: (SinchAdvancedLocationMessageResult) -> Unit) -> Unit)?)`

Sets a custom handler for location messages.

-   **`handler`**: A lambda function that takes the `Context`, `SinchLocationMessageHandlerModel` (containing latitude and longitude), and a callback function. The callback function expects a `SinchAdvancedLocationMessageResult` to determine the propagation behavior. If stopPropagation SDK will not redirect user to Google Maps.

#### `enableSendingMessages()`

Enables the possibility for users to send messages in the chat. This will set `isSendingMessagesEnabled` to `true` and trigger the `enableSendingMessagesHandler` if it's set.

#### `disableSendingMessages()`

Disables the possibility for users to send messages in the chat. This will set `isSendingMessagesEnabled` to `false` and trigger the `disableSendingMessagesHandler` if it's set.

----------

### Data Classes & Sealed Classes

#### `SinchLocationMessageHandlerModel`

A data class representing the model for handling location messages.

-   `latitude`: `Float` - The latitude of the location.
-   `longitude`: `Float` - The longitude of the location.
-   `chatOptions`: `SinchStartChatOptions?` - Optional chat options associated with the message.

#### `SinchAdvancedLocationMessageResult`

A sealed class representing the result of handling a location message, controlling its propagation.

-   `object StopPropagation : SinchAdvancedLocationMessageResult()`: Stops the normal propagation of the location message. This will prevent the application from performing its default action, such as opening Google Maps.
-   `object NormalPropagation : SinchAdvancedLocationMessageResult()`: Allows the normal propagation of the location message. The application will behave as usual, typically opening a map application with the provided coordinates.

----------

### Implementation Examples

#### 1. Setting a Custom Location Message Handler

This example demonstrates how to intercept location messages. Here, we'll log the coordinates and then prevent the default map application from opening.

Kotlin

```
class MyChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_chat)

        SinchChatSDK.chat.advanced.setLocationMessageHandler { context, model, callback ->
            val latitude = model.latitude
            val longitude = model.longitude
            Log.d("LocationMessage", "Received location: Latitude=$latitude, Longitude=$longitude")

            // You can perform custom actions here, e.g., display the location on an in-app map.

            // To stop the default propagation (e.g., opening Google Maps):
            callback(SinchAdvancedLocationMessageResult.StopPropagation)

            // To allow normal propagation (e.g., opening Google Maps):
            // callback(SinchAdvancedLocationMessageResult.NormalPropagation)
        }
    }
}

```
