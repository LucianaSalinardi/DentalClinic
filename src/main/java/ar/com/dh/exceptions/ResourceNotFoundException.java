package ar.com.dh.exceptions;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{
    private Integer statusCode;
    public ResourceNotFoundException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
