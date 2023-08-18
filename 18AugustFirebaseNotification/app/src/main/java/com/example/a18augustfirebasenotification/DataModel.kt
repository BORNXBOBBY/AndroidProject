package com.example.a18augustfirebasenotification

data class NotificationModel(
    val canonical_ids: Int,
    val failure: Int,
    val multicast_id: Long,
    val results: List<Result>,
    val success: Int
)

data class Result(
    val message_id: String
)

data class NotificationData(
    val notification: Notification,
    val registration_ids: List<String>
)

data class Notification(
    val android_channel_id: String,
    val body: String,
    val sound: Boolean,
    val title: String
)
