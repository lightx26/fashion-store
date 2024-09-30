package com.pbl.fashionstore.utils;

import com.pbl.fashionstore.dtos.request.ProductFilterCriteriaParams;
import com.pbl.fashionstore.enums.SortOption;
import com.pbl.fashionstore.exceptions.IllegalRequestArgumentException;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class ProductFilterValidator {
    public static void validateFilter(ProductFilterCriteriaParams filterParams) {
        if (filterParams.getSortOption() == null) {
            if (filterParams.getCursorValue() != null) {
                throw new IllegalRequestArgumentException("Cursor value must be null when sort option is not provided");
            }
        }

        else {
            if (filterParams.getCursorValue() != null && filterParams.getCursorId() == null) {
                throw new IllegalRequestArgumentException("Cursor id must be provided when cursor value is provided");
            }

            if (filterParams.getCursorValue() == null && filterParams.getCursorId() != null) {
                throw new IllegalRequestArgumentException("Cursor id must be null when cursor value is null");
            }

            if (filterParams.getSortOption() == SortOption.NEWEST) {
                if (filterParams.getCursorValue() != null) {
                    // Check if cursor value is a valid date
                    Object cursorValue = filterParams.getCursorValue();
                    try {
                        Instant.parse((String) cursorValue);
                    } catch (DateTimeParseException e) {
                        throw new IllegalRequestArgumentException("Cursor value must be a valid date in ISO-8601 format");
                    }
                }
            }

            else if (filterParams.getSortOption() == SortOption.RATING) {
                if (filterParams.getCursorValue() != null) {
                    // Check if cursor value is a valid number
                    Object cursorValue = filterParams.getCursorValue();
                    try {
                        Double.parseDouble((String) cursorValue);
                    } catch (NumberFormatException e) {
                        throw new IllegalRequestArgumentException("Cursor value must be a valid number");
                    }
                }
            }
        }
    }
}
