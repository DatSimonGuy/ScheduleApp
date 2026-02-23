package com.example.scheduleapp.utils

import androidx.datastore.core.Serializer
import com.example.scheduleapp.data.classes.Schedule
import com.example.scheduleapp.data.classes.ScheduleMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDate
import java.time.LocalTime

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalTime) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): LocalTime = LocalTime.parse(decoder.decodeString())
}

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): LocalDate = LocalDate.parse(decoder.decodeString())
}

object LocalDateListSerializer : KSerializer<List<LocalDate>> {
    override val descriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: List<LocalDate>) = encoder.encodeString(value.joinToString(separator = ";") { it.toString() })
    override fun deserialize(decoder: Decoder): List<LocalDate> = decoder.decodeString().split(";").map { LocalDate.parse(it) }.toList()
}


object ScheduleMapSerializer : Serializer<ScheduleMap> {
    override val defaultValue = ScheduleMap()

    override suspend fun readFrom(input: InputStream): ScheduleMap {
        return try {
            Json.decodeFromString(ScheduleMap.serializer(), input.readBytes().decodeToString())
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: ScheduleMap, output: OutputStream) {
        output.write(Json.encodeToString(ScheduleMap.serializer(), t).toByteArray())
    }
}