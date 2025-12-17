package com.ecommerce.project.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String field;
    Long fieldId;

    public ResourceNotFoundException(String message, String resourceName, String fieldName) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, message));
    }

    public ResourceNotFoundException(String message, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d", message, field, fieldId));
    }
}
