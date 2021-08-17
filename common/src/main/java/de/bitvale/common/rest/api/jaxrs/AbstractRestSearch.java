package de.bitvale.common.rest.api.jaxrs;

import de.bitvale.common.rest.api.Duration;
import de.bitvale.common.rest.api.DurationCreatedProvider;
import de.bitvale.common.rest.api.DurationModifiedProvider;

import javax.ws.rs.QueryParam;

public abstract class AbstractRestSearch {

    @QueryParam("index")
    private int index = 0;

    @QueryParam("limit")
    private int limit = Integer.MAX_VALUE;

    @RestPredicate(DurationCreatedProvider.class)
    private Duration created;

    @RestPredicate(DurationModifiedProvider.class)
    private Duration modified;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Duration getCreated() {
        return created;
    }

    public void setCreated(Duration created) {
        this.created = created;
    }

    public Duration getModified() {
        return modified;
    }

    public void setModified(Duration modified) {
        this.modified = modified;
    }
}
