package de.bitvale.anjunar.jaxrs;

import javax.ws.rs.ext.ParamConverter;
import java.util.Locale;

public class LocalParamConverter implements ParamConverter<Locale> {

    @Override
    public Locale fromString(String value) {
        return Locale.forLanguageTag(value);
    }

    @Override
    public String toString(Locale value) {
        return value.toLanguageTag();
    }

}
