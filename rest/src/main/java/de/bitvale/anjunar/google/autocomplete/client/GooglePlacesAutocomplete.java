package de.bitvale.anjunar.google.autocomplete.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

public interface GooglePlacesAutocomplete {

    @GET
    @Path("json")
    @Produces("application/json")
    PlacePredictions execute(@QueryParam("key") String apiKey, @QueryParam("input") String input, @QueryParam("language") String language);
}
