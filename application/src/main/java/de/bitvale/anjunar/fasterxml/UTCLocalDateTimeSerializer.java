package de.bitvale.anjunar.fasterxml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class UTCLocalDateTimeSerializer extends LocalDateTimeSerializer {

    public UTCLocalDateTimeSerializer() {
        super(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider) throws IOException {

        long aLong = value.getLong(ChronoField.ERA);

        g.writeString(String.valueOf(aLong));

        super.serialize(value, g, provider);
    }
}