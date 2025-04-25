package org.example.broong.domain.menu.enums;

import lombok.Getter;

@Getter
public enum MenuState {
    AVAILABLE("판매중"),
    HOLDOUT("품절"),
    DELETED("삭제");

    private final String description;

    MenuState(String description) {
        this.description = description;
    }

}
