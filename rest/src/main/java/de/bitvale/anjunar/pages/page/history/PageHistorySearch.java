package de.bitvale.anjunar.pages.page.history;

import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;

import javax.ws.rs.QueryParam;
import java.util.UUID;

public class PageHistorySearch extends AbstractRestSearch {

    @QueryParam("id")
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
