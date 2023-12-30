package com.usatiuk.tjv.y.server.service.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Requested data not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
