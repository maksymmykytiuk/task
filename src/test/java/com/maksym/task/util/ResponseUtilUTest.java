package com.maksym.task.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.maksym.task.TestConstants.EXPECTED_USER_WITH_ID_1;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class ResponseUtilUTest {

    @Test
    void failure() {
        ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("test");
        assertEquals(expected, ResponseUtil.failure(HttpStatus.BAD_REQUEST, "test"));
    }

    @Test
    void success() {
        ResponseEntity<Object> expected = ResponseEntity.ok(EXPECTED_USER_WITH_ID_1);
        assertEquals(expected, ResponseUtil.success(EXPECTED_USER_WITH_ID_1));
    }
}