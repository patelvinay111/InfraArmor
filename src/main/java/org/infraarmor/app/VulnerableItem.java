package org.infraarmor.app;

import com.google.gson.Gson;

public class VulnerableItem {
    private String OWASP_top_ten_concern;
    private String line_numbers;
    private String suggestion;
    private String recommendation;

    // Constructor
    public VulnerableItem(String owaspVulnerability, String lines, String suggestion, String isActionable) {
        this.OWASP_top_ten_concern = owaspVulnerability;
        this.line_numbers = lines;
        this.suggestion = suggestion;
        this.recommendation = isActionable;
    }

    // Getter and Setter methods
    public String getOWASP_top_ten_concern() {
        return OWASP_top_ten_concern;
    }

    public void setOWASP_top_ten_concern(String OWASP_top_ten_concern) {
        this.OWASP_top_ten_concern = OWASP_top_ten_concern;
    }

    public String getLine_numbers() {
        return line_numbers;
    }

    public void setLine_numbers(String line_numbers) {
        this.line_numbers = line_numbers;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String isRecommendation() {
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
