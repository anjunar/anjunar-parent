package de.bitvale.anjunar.fasterxml;

import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import java.time.format.DateTimeFormatter;

public class UTCInstantSerializer extends InstantSerializer {

    public UTCInstantSerializer() {
        super(INSTANCE, false, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

}
