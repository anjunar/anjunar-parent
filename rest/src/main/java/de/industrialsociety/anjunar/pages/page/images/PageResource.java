package de.industrialsociety.anjunar.pages.page.images;

import de.industrialsociety.common.rest.api.AbstractRestEntity;

public class PageResource extends AbstractRestEntity {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
