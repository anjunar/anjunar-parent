package de.bitvale.anjunar.google.details.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

public interface GooglePlacesDetails {

    @GET
    @Path("json")
    @Produces("application/json")
    PlaceDetailsResponse execute(@QueryParam("key") String key, @QueryParam("placeid") String placeId, @QueryParam("language") String language);

}
