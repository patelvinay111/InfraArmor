package org.infraarmor.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class VulnerableItem {
    private String owaspVulnerability;
    private String lines;
    private String suggestion;
    private boolean isActionable;

    // Constructor
    public VulnerableItem(String owaspVulnerability, String lines, String suggestion, boolean isActionable) {
        this.owaspVulnerability = owaspVulnerability;
        this.lines = lines;
        this.suggestion = suggestion;
        this.isActionable = isActionable;
    }

    // Getter and Setter methods
    public String getOwaspVulnerability() {
        return owaspVulnerability;
    }

    public void setOwaspVulnerability(String owaspVulnerability) {
        this.owaspVulnerability = owaspVulnerability;
    }

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public boolean isActionable() {
        return isActionable;
    }

    public void setActionable(boolean isActionable) {
        this.isActionable = isActionable;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    private static String printJsonSchema(Object object) {
        Map<String, Object> schema = getSchema(object.getClass());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(schema);
    }

    private static Map<String, Object> getSchema(Class<?> clazz) {
        Map<String, Object> schema = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            // Exclude static and transient fields
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                // If it's a complex type, recursively get its schema
                if (!field.getType().isPrimitive()) {
                    schema.put(field.getName(), getSchema(field.getType()));
                } else {
                    // If it's a primitive type, just use the type name
                    schema.put(field.getName(), field.getType().getSimpleName());
                }
            }
        }

        return schema;
    }

    public static void main(String[] args) {
        VulnerableItem report = new VulnerableItem("SQL Injection", "Line 42", "Use prepared statements", true);
        String json = report.toString();
        //System.out.println(json);

        System.out.println(printJsonSchema(report));
    }
}
