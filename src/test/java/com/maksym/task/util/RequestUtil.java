package com.maksym.task.util;

import com.jayway.restassured.response.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.jayway.restassured.RestAssured.given;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestUtil {

    public static Response makeGetRequestAndGetResponse(String endpoint, HttpStatus httpStatus) {
        return given()
                .expect()
                .log().all()
                .statusCode(httpStatus.value())
                .when()
                .log().all()
                .get(endpoint);
    }

    public static Response makePostRequestAndGetResponse(String endpoint, String body, HttpStatus httpStatus) {
        return given()
                .body(body)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .expect()
                .statusCode(httpStatus.value())
                .when()
                .log().all()
                .post(endpoint);
    }

    public static Response makePutRequestAndGetResponse(String endpoint, String body, HttpStatus httpStatus) {
        return given()
                .body(body)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .expect()
                .statusCode(httpStatus.value())
                .when()
                .log().all()
                .put(endpoint);
    }
}
