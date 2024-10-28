package com.mobisoft.taskmanagement.entity;

public enum Gender {
    HOMME("Homme"),
    FEMME("Femme");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

