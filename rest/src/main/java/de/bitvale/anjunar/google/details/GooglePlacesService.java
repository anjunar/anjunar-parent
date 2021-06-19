package de.bitvale.anjunar.google.details;

import de.bitvale.anjunar.google.details.client.GooglePlacesDetails;
import de.bitvale.anjunar.google.details.client.PlaceDetailsForm;
import de.bitvale.anjunar.google.GoogleResource;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Locale;

@ApplicationScoped
public class GooglePlacesService {

    private final static Logger log = LoggerFactory.getLogger(GooglePlacesService.class);


    public PlaceDetailsForm findDetails(String id, Locale locale) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://maps.googleapis.com/maps/api/place/details");
        ResteasyWebTarget rtarget = (ResteasyWebTarget) target;

        GooglePlacesDetails service = rtarget.proxy(GooglePlacesDetails.class);
        return service.execute(GoogleResource.API_KEY, id, locale.getLanguage()).getResult();
    }
}
