package com.example.repository;

public class DuplicateIDException extends RepositoryException {

    public DuplicateIDException(String message) {
        super(message);
    }
}
