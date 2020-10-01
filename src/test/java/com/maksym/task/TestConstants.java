package com.maksym.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maksym.task.model.Gender;
import com.maksym.task.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                                                       .registerModule(new JavaTimeModule())
                                                                       .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static final String SUCCESS_REQUEST_FIXTURE_PATH = "/fixtures/request/success_request.json.template";

    public static final String LOGIN_OTHER = "other";
    public static final String LOGIN_ADMIN = "admin";
    public static final String LOGIN_USER = "user";

    public static final String NAME_PETRO = "Petrov Petro Petrovich";
    public static final String NAME_TEST = "TEST TEST TEST";
    public static final String NAME_IVAN = "Ivanov Ivan Ivanovich";
    public static final String NAME_LARISA = "Stepanova Larisa Ivanovna";

    public static final LocalDate DATE_1995_10_05 = LocalDate.of(1995, 10, 05);
    public static final LocalDate DATE_1993_07_12 = LocalDate.of(1995, 07, 12);

    public static final User EXPECTED_USER_WITH_ID_1 = new User(1L, LOGIN_ADMIN, NAME_IVAN, DATE_1995_10_05, Gender.MALE);
    public static final User EXPECTED_USER_WITH_ID_2 = new User(2L, LOGIN_USER, NAME_LARISA, DATE_1993_07_12, Gender.FEMALE);

}
