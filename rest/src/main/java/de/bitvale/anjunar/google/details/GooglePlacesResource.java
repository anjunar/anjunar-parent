package de.bitvale.anjunar.google.details;

import de.bitvale.anjunar.google.details.client.PlaceDetailForm;
import de.bitvale.anjunar.google.details.client.PlaceDetailsForm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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


    @GET
    @Path("place/{id}/details")
    public LocationDetailForm details(@Context HttpServletRequest request, @PathParam("id") String id) {
        PlaceDetailsForm details = service.findDetails(id, request.getLocale());

        String street_number = findWithType("street_number", details.getAddressComponents());
        String street = findWithType("route", details.getAddressComponents());
        String zipCode = findWithType("postal_code", details.getAddressComponents());
        String state = findWithType("locality", details.getAddressComponents());
        String country = findWithType("country", details.getAddressComponents());

        LocationDetailForm form = new LocationDetailForm();

        form.setId(details.getPlaceId());
        form.setName(details.getName());
        form.setStreet(street);
        form.setStreetNumber(street_number);
        form.setZipCode(zipCode);
        form.setState(state);
        form.setCountry(country);

        form.setLat(details.getGeometry().getLocation().getLat());
        form.setLng(details.getGeometry().getLocation().getLng());

        return form;
    }

    private String findWithType(String type, List<PlaceDetailForm> details) {
        for (PlaceDetailForm form : details) {
            if (form.getTypes().contains(type)) {
                return form.getLong_name();
            }
        }
        return null;
    }


}
