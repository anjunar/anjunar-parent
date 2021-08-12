package de.bitvale.anjunar.pages.page;

import de.bitvale.common.rest.api.AbstractRestEntity;

import java.time.LocalDateTime;

public class ImageResource extends AbstractRestEntity {

    private String name;

    private LocalDateTime lastModified;

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

}
