package de.bitvale.common.i18n;

import de.bitvale.common.security.Identity;
import de.bitvale.common.security.enterprise.LoggedInEvent;
import org.apache.deltaspike.core.api.common.DeltaSpike;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Locale;

@RequestScoped
public class i18nResolver implements Serializable {

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    private final Identity identity;

    @Inject
    public i18nResolver(HttpServletRequest request, @DeltaSpike HttpServletResponse response, Identity identity) {
        this.request = request;
        this.response = response;
        this.identity = identity;
    }

    public i18nResolver() {
        this(null, null, null);
    }

    @Transactional
    public void setLocale(Locale locale) {
        response.setLocale(locale);
        if (identity.getUser() != null) {
            identity.getUser().setLanguage(locale);
        }
    }

    public Locale getLocale() {
        return response.getLocale();
    }

    public void onEvent(@Observes LoggedInEvent event) {
        response.setLocale(event.getUser().getLanguage());
    }

    public void onResponse(@Observes ServletResponse response) {
        if (identity.getUser() == null) {
            response.setLocale(Locale.forLanguageTag("en-DE"));
        } else {
            response.setLocale(identity.getUser().getLanguage());
        }
    }

}
