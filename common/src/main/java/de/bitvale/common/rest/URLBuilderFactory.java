package de.bitvale.common.rest;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;

/**
 * @author Patrick Bittner on 11.06.2015.
 */
@Dependent
public class URLBuilderFactory implements Serializable {

    private final Event<SecurityAction> securityActionEvent;

    @Inject
    public URLBuilderFactory(Event<SecurityAction> securityActionEvent) {
        this.securityActionEvent = securityActionEvent;
    }

    public URLBuilderFactory() {
        this(null);
    }

    public <B> URLBuilder<B> from(Class<B> aClass) {
        return new URLBuilder<>(aClass, securityActionEvent, UriBuilder.fromPath("/"));
    }

}
