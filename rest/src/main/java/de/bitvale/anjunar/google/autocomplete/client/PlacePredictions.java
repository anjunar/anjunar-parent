package de.bitvale.anjunar.google.autocomplete.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacePredictions {

    private List<PlacePredictionForm> predictions;

    public List<PlacePredictionForm> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<PlacePredictionForm> predictions) {
        this.predictions = predictions;
    }
}
