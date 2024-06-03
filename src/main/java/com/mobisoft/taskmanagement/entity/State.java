package com.mobisoft.taskmanagement.entity;

public enum State {
    EN_COURS ("En cours"),
    EN_ATTENTE ("En attente"),
    TERMINER ("Terminer");

    private final String label;

    State(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
