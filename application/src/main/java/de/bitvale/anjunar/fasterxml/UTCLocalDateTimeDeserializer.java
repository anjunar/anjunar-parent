package de.bitvale.anjunar.fasterxml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class UTCLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

    public UTCLocalDateTimeDeserializer() {
        super(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            long value = parser.getValueAsLong();
            Instant instant = Instant.ofEpochMilli(value);

            return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        }

        return super.deserialize(parser, context);
    }

}