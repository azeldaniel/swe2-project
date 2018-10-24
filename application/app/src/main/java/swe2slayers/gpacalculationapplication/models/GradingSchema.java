package swe2slayers.gpacalculationapplication.models;

import java.io.Serializable;
import java.util.HashMap;

public class GradingSchema implements Serializable {

    private HashMap<String, Grade> scheme;

    private String gradingSchemaId;

    public GradingSchema(){}

    public HashMap<String, Grade> getScheme() {
        return scheme;
    }

    public void setScheme(HashMap<String, Grade> scheme) {
        this.scheme = scheme;
    }

    public String getGradingSchemaId() {
        return gradingSchemaId;
    }

    public void setGradingSchemaId(String gradingSchemaId) {
        this.gradingSchemaId = gradingSchemaId;
    }
}
