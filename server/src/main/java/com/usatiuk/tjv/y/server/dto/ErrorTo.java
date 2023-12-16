package com.usatiuk.tjv.y.server.dto;

import java.util.Collection;
import java.util.stream.Stream;

public record ErrorTo(String[] errors, Integer code) {
    public ErrorTo(Collection<String> errors, Integer code) {
        this(errors.toArray(String[]::new), code);
    }

    public ErrorTo(Stream<String> errors, Integer code) {
        this(errors.toArray(String[]::new), code);
    }
}
