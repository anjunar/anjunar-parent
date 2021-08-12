package de.bitvale.common.rest.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Patrick Bittner on 13.06.2015.
 */
public class Link {

    private final String url;

    private final String method;

    private final String rel;

    @JsonCreator
    public Link(@JsonProperty("url") String url,
                @JsonProperty("method") String method,
                @JsonProperty("rel") String rel) {
        this.url = url;
        this.method = method;
        this.rel = rel;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getRel() {
        return rel;
    }
}
