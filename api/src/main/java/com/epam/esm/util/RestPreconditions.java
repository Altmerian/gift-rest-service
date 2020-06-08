package com.epam.esm.util;

import java.util.NoSuchElementException;

public class RestPreconditions {
    public static <T> T checkFound(T resource) {
        if (resource == null) {
            throw new NoSuchElementException();
        }
        return resource;
    }
}
