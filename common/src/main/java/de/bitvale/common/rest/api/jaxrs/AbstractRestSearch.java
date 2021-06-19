package de.bitvale.common.rest.api.jaxrs;

import javax.ws.rs.QueryParam;

public abstract class AbstractRestSearch {

    @QueryParam("index")
    private int index = 0;

    @QueryParam("limit")
    private int limit = Integer.MAX_VALUE;

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
}
