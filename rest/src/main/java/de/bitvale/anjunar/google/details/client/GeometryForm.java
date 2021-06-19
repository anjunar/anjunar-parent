package de.bitvale.anjunar.google.details.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeometryForm {

    private LocationForm location;

    public LocationForm getLocation() {
        return location;
    }

    public void setLocation(LocationForm location) {
        this.location = location;
    }
}
