package org.infraarmor.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

public class ReportItem {
    @JsonProperty("OWASP_top_ten_concern")
    private String owaspCategory;
    @JsonProperty("line_numbers")
    private String lineNumbers;
    private String suggestion;
    private String recommendation;

    // Constructor
    public ReportItem() {

    }
    public ReportItem(String owaspCategory, String lineNumbers, String suggestion, String isActionable) {
        this.owaspCategory = owaspCategory;
        this.lineNumbers = lineNumbers;
        this.suggestion = suggestion;
        this.recommendation = isActionable;
    }

    // Getter and Setter methods
    public String getOwaspCategory() {
        return owaspCategory;
    }

    public void setOwaspCategory(String owaspCategory) {
        this.owaspCategory = owaspCategory;
    }

    public String getLineNumbers() {
        return lineNumbers;
    }

    public void setLineNumbers(String lineNumbers) {
        this.lineNumbers = lineNumbers;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
