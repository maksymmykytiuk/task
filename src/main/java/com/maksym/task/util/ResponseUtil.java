package com.maksym.task.util;

import com.maksym.task.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtil {

    public static ResponseEntity<User> success(User user) {
        return ResponseEntity.ok(user);
    }

    public static ResponseEntity<Object> failure(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(message);
    }
}

