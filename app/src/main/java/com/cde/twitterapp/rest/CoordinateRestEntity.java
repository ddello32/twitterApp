package com.cde.twitterapp.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Model for a coordinate entity from Twitter's rest api.
 * Created by dello on 12/01/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoordinateRestEntity implements Serializable {
    @JsonProperty("type")
    String type;
    @JsonProperty("coordinates")
    List<Double> coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
