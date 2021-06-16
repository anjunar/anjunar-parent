package de.industrialsociety.anjunar.pages;

import de.industrialsociety.common.rest.api.AbstractRestEntity;
import de.industrialsociety.common.rest.api.meta.Input;

public class PagesResource extends AbstractRestEntity {

    @Input(placeholder = "Title", type = "text")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
