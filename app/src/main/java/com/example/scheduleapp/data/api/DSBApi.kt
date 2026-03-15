package com.example.scheduleapp.data.api

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
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.DayOfWeek

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
                        val formValue = when (value) {
                            is JsonPrimitive -> value.content
                            else -> value.jsonArray.joinToString(";") { it.jsonPrimitive.content }
                        }
                        append(key, formValue)
                    }
                    append("plan_name", scheduleName)
                    append("day", dayOfWeek.ordinal.toString())
                    append("chat_id", chatId.toString())
                }
            )
            if (!response.status.isSuccess()) {
                errorString = response.body()
            }
        } catch(e: Exception) {
            errorString = connectionError
        }
        return errorString
    }

    suspend fun removeLesson(lessonId: String, dayOfWeek: DayOfWeek, scheduleName: String): String? {
        var errorString: String? = null
        try {
            val response = client.submitForm(
                "${baseUrl}/remove_lesson",
                formParameters = parameters {
                    append("id", lessonId)
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

    suspend fun removeSchedule(scheduleName: String): String? {
        var errorString: String? = null
        try {
            val response = client.submitForm(
                "${baseUrl}/remove_schedule",
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

    suspend fun editSchedule(oldScheduleName: String, newScheduleName: String): String? {
        var errorString: String? = null
        try {
            val response = client.submitForm(
                "${baseUrl}/edit_schedule",
                formParameters = parameters {
                    append("schedule_name", oldScheduleName)
                    append("new_name", newScheduleName)
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

        try {
            val response = client.get("${baseUrl}/get_plan?group_id=${chatId}&plan_name=${scheduleName}")
            if (!response.status.isSuccess()) {
                errorString = response.body()
            } else {
                val lessons: List<List<Lesson>> = Json.decodeFromString(response.body())
                schedule = Schedule(
                    lessons = lessons.withIndex().associate { (index, lessons) ->
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

    suspend fun getSchedules(chatId: Long): Pair<List<String>?, String?> {
        var errorString: String? = null
        var schedules: List<String>? = null

        try {
            val response = client.get("${baseUrl}/plans?chat_id=${chatId}")
            if (!response.status.isSuccess()) {
                errorString = response.body()
            } else {
                schedules = Json.decodeFromString(response.body())
            }
        } catch (e: Exception) {
            errorString = connectionError
        }

        return schedules to errorString
    }
}