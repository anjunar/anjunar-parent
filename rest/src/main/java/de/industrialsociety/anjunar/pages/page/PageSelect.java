package de.industrialsociety.anjunar.pages.page;

import de.industrialsociety.common.rest.api.AbstractRestEntity;

import java.util.Locale;

public class PageSelect extends AbstractRestEntity {

    private String title;

    private Locale language;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }
}
