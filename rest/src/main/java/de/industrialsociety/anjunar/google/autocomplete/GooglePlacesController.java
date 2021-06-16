package de.industrialsociety.anjunar.google.autocomplete;

import de.industrialsociety.common.rest.Secured;
import de.industrialsociety.common.rest.URLBuilder;
import de.industrialsociety.common.rest.URLBuilderFactory;
import de.industrialsociety.common.rest.api.Container;
import de.industrialsociety.anjunar.google.autocomplete.client.PlacePredictionForm;
import de.industrialsociety.anjunar.google.autocomplete.client.PlacePredictions;

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
public class GooglePlacesController {

    private final GooglePlacesService service;

    @Inject
    public GooglePlacesController(GooglePlacesService service) {
        this.service = service;
    }

    public GooglePlacesController() {
        this(null);
    }

    @POST
    @Path("place/autocomplete")
    @Secured
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

    public static URLBuilder<GooglePlacesController> linkGeoCoding(URLBuilderFactory builderFactory) {
        return builderFactory
                .from(GooglePlacesController.class)
                .record((method) -> method.geoCoding(null, null))
                .rel("geoCoding");
    }

}
