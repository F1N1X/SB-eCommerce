package com.ecommerce.project.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException(String message, String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, message));
    }

    public ResourceNotFoundException(String message, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d", resourceName, message, fieldId));
    }
}
