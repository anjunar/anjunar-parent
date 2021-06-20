package de.bitvale.anjunar.pages.page.images.image;

import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.anjunar.pages.page.images.PageResource;

import java.time.LocalDateTime;

public class ImageResource extends AbstractRestEntity<ImageResource> {

    private String name;

    private LocalDateTime lastModified;

    private String data;

    private PageResource page;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public PageResource getPage() {
        return page;
    }

    public void setPage(PageResource page) {
        this.page = page;
    }
}
