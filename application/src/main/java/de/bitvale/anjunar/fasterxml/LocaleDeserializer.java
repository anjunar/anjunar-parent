package de.bitvale.anjunar.fasterxml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Locale;

public class LocaleDeserializer extends StdDeserializer<Locale> {

    public LocaleDeserializer() {
        super(Locale.class);
    }

    @Override
    public Locale deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Locale.forLanguageTag(p.getText());
    }
}
