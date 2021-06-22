package de.bitvale.anjunar.pages;

import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.meta.Input;

public class PagesResource extends AbstractRestEntity<PagesResource> {

    @Input(placeholder = "Title", type = "text")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static PagesResource factory(Page page) {
        PagesResource resource = new PagesResource();

        resource.setId(page.getId());
        resource.setTitle(page.getTitle());

        return resource;
    }
}
