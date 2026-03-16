package com.example.scheduleapp.utils

import androidx.annotation.experimental.Experimental
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.scheduleapp.data.classes.ScheduleMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.contentOrNull
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object LocalTimeSerializer : KSerializer<LocalTime> {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.format(formatter))
    }
    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString(), formatter)
    }
}

object LocalDateSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    override val descriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(formatter.format(value))
    override fun deserialize(decoder: Decoder): LocalDate = decoder.decodeString().let {
        if(it.isEmpty()) {
            LocalDate.now()
        } else {
            LocalDate.parse(it, formatter)
        }
    }
}

object LocalDateListSerializer : JsonTransformingSerializer<List<LocalDate>>(
    ListSerializer(LocalDateSerializer)
) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return if (element is JsonPrimitive && element.contentOrNull == "") {
            JsonArray(emptyList())
        } else {
            element
        }
    }
}

object ScheduleMapSerializer : Serializer<ScheduleMap> {

    override val defaultValue: ScheduleMap = ScheduleMap()

    override suspend fun readFrom(input: InputStream): ScheduleMap {
        try {
            val json = input.readBytes().decodeToString()
            return Json.decodeFromString(ScheduleMap.serializer(), json)
        } catch (e: SerializationException) {
            throw CorruptionException("Cannot read ScheduleMap", e)
        }
    }

    override suspend fun writeTo(t: ScheduleMap, output: OutputStream) {
        val json = Json.encodeToString(ScheduleMap.serializer(), t)
        output.write(json.toByteArray())
    }
}