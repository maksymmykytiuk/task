package com.maksym.task.exception;

import static java.lang.String.format;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(long userId) {
        super(format("User %s Not Found", userId));
    }
}
