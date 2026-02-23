package com.example.scheduleapp.utils

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.scheduleapp.data.classes.Schedule
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
    private val listSerializer = ListSerializer(LocalDateSerializer)

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: List<LocalDate>) {
        listSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): List<LocalDate> {
        return listSerializer.deserialize(decoder)
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