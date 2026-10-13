package com.example.network

import android.content.Context
import com.example.core.constants.AppConstants
import com.example.data.model.NotificationItemDto
import com.example.presentation.R
import dagger.hilt.android.qualifiers.ApplicationContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XmlParser @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun fetchAndParseXml(
        urlString: String = AppConstants.XML_ALERTS_URL
    ): List<NotificationItemDto> {
        return try {
            val url = URL(urlString)
            val conn = url.openConnection() as HttpURLConnection

            try {
                conn.connectTimeout = AppConstants.NETWORK_TIMEOUT_MS
                conn.readTimeout = AppConstants.NETWORK_TIMEOUT_MS
                conn.requestMethod = AppConstants.HTTP_METHOD_GET
                conn.connect()

                conn.inputStream.use { inputStream ->
                    parseXmlStream(inputStream)
                }
            } finally {
                conn.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            getFallbackData()
        }
    }

    private fun parseXmlStream(
        inputStream: InputStream
    ): List<NotificationItemDto> {
        val items = mutableListOf<NotificationItemDto>()

        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(inputStream, null)

        var eventType = parser.eventType

        var currentId = 0
        var currentTitle = ""
        var currentDelay = 0L

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        AppConstants.XML_TAG_NOTIFICATION, AppConstants.XML_TAG_ITEM -> {
                            currentId = 0
                            currentTitle = ""
                            currentDelay = 0L
                        }

                        AppConstants.XML_TAG_ID -> {
                            currentId = parser.nextText().trim().toIntOrNull() ?: 0
                        }

                        AppConstants.XML_TAG_TITLE -> {
                            currentTitle = parser.nextText().trim()
                        }

                        AppConstants.XML_TAG_TIME_IN_SECONDS -> {
                            currentDelay = parser.nextText().trim().toLongOrNull() ?: 0L
                        }
                    }
                }

                XmlPullParser.END_TAG -> {
                    if (parser.name == AppConstants.XML_TAG_NOTIFICATION || parser.name == AppConstants.XML_TAG_ITEM) {
                        items.add(
                            NotificationItemDto(
                                id = currentId,
                                title = currentTitle,
                                delayInSeconds = currentDelay
                            )
                        )
                    }
                }
            }

            eventType = parser.next()
        }
        return items.ifEmpty {
            getFallbackData()
        }
    }

    private fun getFallbackData(): List<NotificationItemDto> {
        return listOf(
            NotificationItemDto(1, context.getString(R.string.fallback_title_10s), 10),
            NotificationItemDto(2, context.getString(R.string.fallback_title_20s), 20),
            NotificationItemDto(3, context.getString(R.string.fallback_title_30s), 30),
            NotificationItemDto(4, context.getString(R.string.fallback_title_40s), 40),
            NotificationItemDto(5, context.getString(R.string.fallback_title_50s), 50),
            NotificationItemDto(6, context.getString(R.string.fallback_title_60s), 60),
            NotificationItemDto(7, context.getString(R.string.fallback_title_3min), 180)
        )
    }
}