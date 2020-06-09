package com.epam.esm.util;

import com.epam.esm.exception.ResourceNotFoundException;

public class RestPreconditions {
    public static <T> T checkFound(T resource) {
        if (resource == null) {
            throw new ResourceNotFoundException(
                    resource.getClass().getSimpleName() + " not found");
        }
        return resource;
    }

    public static <T> T checkDuplicate(T resource) {
        if (resource == null) {
            throw new ResourceNotFoundException(
                    resource.getClass().getSimpleName() + " not found");
        }
        return resource;
    }
}
