package de.bitvale.anjunar.fasterxml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Locale;

public class LocaleSerializer extends StdSerializer<Locale> {

    public LocaleSerializer() {
        super(Locale.class);
    }

    @Override
    public void serialize(Locale value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.toLanguageTag());
    }
}
