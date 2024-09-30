package com.pbl.fashionstore.enums;

import lombok.Getter;

@Getter
public enum SortOption {
    NEWEST("Newest"),
    RATING("Rating");

    private final String label;

    SortOption(String label) {
        this.label = label;
    }
}
