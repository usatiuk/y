package com.usatiuk.tjv.y.server.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super();
    }
    
    public UserNotFoundException(String message) {
        super(message);
    }
}
