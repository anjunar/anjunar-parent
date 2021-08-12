package de.bitvale.anjunar.shared.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.rest.api.meta.MetaForm;
import de.bitvale.common.security.Identity;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import java.util.Locale;

public class Language {

    @Input(type = "text", naming = true)
    private String language;

    @Input(type = "text", primaryKey = true)
    private Locale locale;

    @Input(ignore = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final MetaForm meta = new MetaForm(Language.class);

    public MetaForm getMeta() {
        return meta;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public static Language factory(Locale locale) {
        if (locale == null) {
            return null;
        }

        Language language = new Language();
        if (locale.toLanguageTag().equals("de-DE")) {
            language.setLanguage("Deutsch");
            language.setLocale(locale);
        }
        if (locale.toLanguageTag().equals("en-DE")) {
            language.setLanguage("English");
            language.setLocale(locale);
        }
        return language;
    }

    public static Locale updater(Language language) {
        return language.getLocale();
    }

}
