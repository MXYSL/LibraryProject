package com.Team3.LibraryProject.Exception;

public class LibraryException extends RuntimeException {
    public LibraryException(String message) {
        super(message);
    }
    
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
