package com.example.TaskManagement;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String fileNotFound) {
        super(fileNotFound);
    }

}
