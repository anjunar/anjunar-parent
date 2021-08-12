package de.bitvale.anjunar.fasterxml;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class UTCInstantDeserializer extends InstantDeserializer<Instant> {

    public UTCInstantDeserializer() {
        super(INSTANT, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
}
