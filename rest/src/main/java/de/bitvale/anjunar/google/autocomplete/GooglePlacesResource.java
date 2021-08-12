package de.bitvale.anjunar.google.autocomplete;

import de.bitvale.anjunar.google.autocomplete.client.PlacePredictionForm;
import de.bitvale.anjunar.google.autocomplete.client.PlacePredictions;
import de.bitvale.common.rest.api.Container;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("generic/google")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GooglePlacesResource {

    private final GooglePlacesService service;

    @Inject
    public GooglePlacesResource(GooglePlacesService service) {
        this.service = service;
    }

    public GooglePlacesResource() {
        this(null);
    }

    @POST
    @Path("place/autocomplete")
    public Container<LocationForm> geoCoding(@Context HttpServletRequest request, LocationForm address) {
        PlacePredictions placePredictions = service.find(address.getName(), request.getLocale());
        List<LocationForm> forms = new ArrayList<>();
        if (placePredictions == null) {
            return new Container<>(forms, forms.size());
        }

        for (PlacePredictionForm placePrediction : placePredictions.getPredictions()) {
            LocationForm form = new LocationForm();
            form.setName(placePrediction.getDescription());
            form.setId(placePrediction.getPlace_id());
            forms.add(form);
        }

        return new Container<>(forms, forms.size());
    }

}
