/*
 * Copyright (c) 2018. Software Engineering Slayers
 *
 * Azel Daniel (816002285)
 * Amanda Seenath (816002935)
 * Christopher Joseph (814000605)
 * Michael Bristol (816003612)
 * Maya Bannis (816000144)
 *
 * COMP 3613
 * Software Engineering II
 *
 * GPA Calculator Project
 */

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
