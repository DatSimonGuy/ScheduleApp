package com.example.scheduleapp.data.api

import android.util.Log
import com.example.scheduleapp.BuildConfig
import com.example.scheduleapp.data.classes.Lesson
import com.example.scheduleapp.data.classes.SaveLocation
import com.example.scheduleapp.data.classes.Schedule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import java.time.DayOfWeek
import java.util.UUID

class DSBApi(
    val chatId: Long,
    val userKey: String
) {
    val client = HttpClient()
    val baseUrl = BuildConfig.API_SERVER_URL

    val connectionError = "Error while connecting to the api"

    suspend fun addLesson(lesson: Lesson, dayOfWeek: DayOfWeek, scheduleName: String): String? {
        val lessonJson = Json.encodeToJsonElement(lesson).jsonObject
        var errorString: String? = null
        try {
            val response = client.submitForm(
                "${baseUrl}/add_lesson",
                formParameters = parameters {
                    lessonJson.forEach { (key, value) ->
                        append(key, value.toString())
                    }
                    append("plan_name", scheduleName)
                    append("day", dayOfWeek.ordinal.toString())
                    append("chat_id", chatId.toString())
                }
            )
            if (!response.status.isSuccess()) {
                errorString = response.body()
            }
        } catch(_: Exception) {
            errorString = connectionError
        }
        return errorString
    }

    suspend fun createSchedule(scheduleName: String): String? {
        var errorString: String? = null
        try {
            val response = client.submitForm(
                "${baseUrl}/create_schedule",
                formParameters = parameters {
                    append("schedule_name", scheduleName)
                    append("chat_id", chatId.toString())
                }
            )
            if (!response.status.isSuccess()) {
                errorString = response.body()
            }
        } catch (_: Exception) {
            errorString = connectionError
        }
        return errorString
    }

    suspend fun getSchedule(scheduleName: String): Pair<Schedule?, String?> {
        var errorString: String? = null
        var schedule: Schedule? = null

        Log.e("", "Getting schedule")

        try {
            val response = client.get("${baseUrl}/get_plan?group_id=${chatId}&plan_name=${scheduleName}")
            if (!response.status.isSuccess()) {
                errorString = response.body()
            } else {
                val lessons: List<List<Lesson>> = Json.decodeFromString(response.body())
                schedule = Schedule(
                    lessons = lessons.withIndex().associate { (index, lessons) ->
                        lessons.forEach {
                            it.id = UUID.randomUUID().toString()
                        }
                        DayOfWeek.of(index+1) to lessons
                    },
                    chatId = chatId,
                    saveLocation = SaveLocation.DSB
                )
            }
        } catch (e: Exception) {
            errorString = connectionError
        }

        return schedule to errorString
    }
}