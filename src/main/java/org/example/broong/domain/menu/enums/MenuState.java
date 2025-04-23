package org.example.broong.domain.menu.enums;

public enum MenuState {
    AVAILABLE("판매중"),
    DELETED("품절");

    private final String description;

    MenuState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
