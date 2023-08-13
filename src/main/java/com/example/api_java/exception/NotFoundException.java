package com.example.api_java.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Class<?> clazz, Long id) {
        super(clazz.getSimpleName() + " has id=" + id + " not found!");
    }

    public NotFoundException(Class<?> clazz, String id) {
        super(clazz.getSimpleName() + " has id=" + id + " not found!");
    }

    public NotFoundException(String msg) {
        super(msg);
    }

}
