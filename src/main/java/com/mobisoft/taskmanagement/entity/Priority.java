package com.mobisoft.taskmanagement.entity;

public enum Priority {

    ELEVEE ("Elevee"),
    MOYENNE ("Moyenne"),
    FAIBLE ("Faible");

    private final String label;

    Priority(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
