package de.bitvale.anjunar.pages.page.images;

import de.bitvale.common.rest.api.AbstractRestEntity;

public class PageResource extends AbstractRestEntity {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
