package com.example.core.constants

object AppConstants {

    const val EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION"
    const val EXTRA_TITLE = "EXTRA_TITLE"
    const val EXTRA_ID = "EXTRA_ID"

    const val XML_ALERTS_URL = "https://lynxapp.com/localalerts.php"
    const val HTTP_METHOD_GET = "GET"
    const val NETWORK_TIMEOUT_MS = 5000

    const val CHANNEL_ID = "local_alerts_channel"

    // XML Tag Constants
    const val XML_TAG_NOTIFICATION = "notification"
    const val XML_TAG_ITEM = "item"
    const val XML_TAG_ID = "id"
    const val XML_TAG_TITLE = "title"
    const val XML_TAG_TIME_IN_SECONDS = "timeInSeconds"

    const val MENU_CANCEL_ALL_ID = 101
    const val MENU_CANCEL_SINGLE_ID = 201

    const val WORK_TAG_NOTIFICATION = "notification_work"
    const val WORK_UNIQUE_PREFIX = "notification_work_"
}