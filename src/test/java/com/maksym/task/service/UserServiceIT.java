package com.maksym.task.service;

import com.maksym.task.model.Gender;
import com.maksym.task.model.User;
import com.maksym.task.repository.UserRepository;
import com.maksym.task.util.MustacheFixturesUtil;
import com.maksym.task.util.RequestUtil;
import com.sun.tools.javac.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.maksym.task.TestConstants.DATE_1995_10_05;
import static com.maksym.task.TestConstants.EXPECTED_USER_WITH_ID_1;
import static com.maksym.task.TestConstants.EXPECTED_USER_WITH_ID_2;
import static com.maksym.task.TestConstants.LOGIN_ADMIN;
import static com.maksym.task.TestConstants.LOGIN_OTHER;
import static com.maksym.task.TestConstants.NAME_PETRO;
import static com.maksym.task.TestConstants.NAME_TEST;
import static com.maksym.task.TestConstants.OBJECT_MAPPER;
import static com.maksym.task.TestConstants.SUCCESS_REQUEST_FIXTURE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceIT {

    @Value("${local.server.port}")
    private int port;
    @Resource
    private UserRepository userRepository;
    private String BASE_URL;
    private final List<User> expectedUsers = List.of(EXPECTED_USER_WITH_ID_1, EXPECTED_USER_WITH_ID_2);

    @BeforeEach
    public void init() {
        BASE_URL = "http://localhost:" + port + "/user";
        userRepository.saveAll(expectedUsers);
        assert userRepository.count() == 2;
    }

    @Test
    void successGetAllUsers() {
        User[] actual = RequestUtil.makeGetRequestAndGetResponse(BASE_URL, HttpStatus.OK).getBody().as(User[].class);
        assertEquals(expectedUsers.size(), actual.length);
    }

    @Test
    void successGet_WhenIdIsExist() {
        User actual = RequestUtil.makeGetRequestAndGetResponse(BASE_URL + "/1", HttpStatus.OK).getBody().as(User.class);
        assertEquals(EXPECTED_USER_WITH_ID_1.getDob(), actual.getDob());
        assertEquals(EXPECTED_USER_WITH_ID_1.getFullName(), actual.getFullName());
        assertEquals(EXPECTED_USER_WITH_ID_1.getGender(), actual.getGender());
        assertEquals(EXPECTED_USER_WITH_ID_1.getId(), actual.getId());
        assertEquals(EXPECTED_USER_WITH_ID_1.getLogin(), actual.getLogin());
    }

    @Test
    void failureGet_WhenUserDoesNotExist() {
        String actualResponse =  RequestUtil.makeGetRequestAndGetResponse(BASE_URL + "/3", HttpStatus.BAD_REQUEST).getBody().asString();
        assertEquals("User 3 Not Found", actualResponse);
    }

    @Test
    void successSaveNewUser() {
        String body = MustacheFixturesUtil.compileTemplate(SUCCESS_REQUEST_FIXTURE_PATH, validUserRequest());
        User actual =  RequestUtil.makePostRequestAndGetResponse(BASE_URL, body, HttpStatus.OK).getBody().as(User.class);

        assertEquals(userRepository.count(), 3);
        assertEquals(DATE_1995_10_05, actual.getDob());
        assertEquals(NAME_PETRO, actual.getFullName());
        assertEquals(Gender.MALE, actual.getGender());
        assertEquals(LOGIN_OTHER, actual.getLogin());
    }

    @ParameterizedTest
    @MethodSource("invalidUserRequest")
    void failurePost_WhenRequestNotWellFormed(String body) {
        String actualResponse =  RequestUtil.makePostRequestAndGetResponse(BASE_URL, body, HttpStatus.BAD_REQUEST).getBody().asString();
        assertTrue(actualResponse.matches(".*cannot be empty"));
    }

    @Test
    void failurePost_WhenLoginIsNotUnique() {
        User user = validUserRequest();
        user.setLogin(LOGIN_ADMIN);
        String body = MustacheFixturesUtil.compileTemplate(SUCCESS_REQUEST_FIXTURE_PATH, user);
        String actualResponse =  RequestUtil.makePostRequestAndGetResponse(BASE_URL, body, HttpStatus.BAD_REQUEST).getBody().asString();
        assertTrue(actualResponse.matches("'login' is duplicate"));
    }

    @Test
    @SneakyThrows
    void successUpdateExistUser() {
        User user = new User();
        user.setFullName(NAME_TEST);
        String body = OBJECT_MAPPER.writeValueAsString(user);
        User actual =  RequestUtil.makePutRequestAndGetResponse(BASE_URL + "/1", body, HttpStatus.OK).getBody().as(User.class);

        assertEquals(EXPECTED_USER_WITH_ID_1.getDob(), actual.getDob());
        assertEquals(NAME_TEST, actual.getFullName());
        assertEquals(EXPECTED_USER_WITH_ID_1.getGender(), actual.getGender());
        assertEquals(EXPECTED_USER_WITH_ID_1.getLogin(), actual.getLogin());
        assertEquals(EXPECTED_USER_WITH_ID_1.getId(), actual.getId());
    }

    @Test
    @SneakyThrows
    void failurePut_WhenDoesNotExist() {
        User user = new User();
        user.setFullName(NAME_TEST);
        String body = OBJECT_MAPPER.writeValueAsString(user);
        String actual =  RequestUtil.makePutRequestAndGetResponse(BASE_URL + "/3", body, HttpStatus.BAD_REQUEST).getBody().asString();
        assertEquals("User 3 Not Found", actual);
    }

    private static Stream<Arguments> invalidUserRequest() {
        String nullDob = updateAndGetUserRequestAsJson(userRequest -> userRequest.setDob(null));
        String nullFullName = updateAndGetUserRequestAsJson(userRequest -> userRequest.setFullName(null));
        String nullGender = updateAndGetUserRequestAsJson(userRequest -> userRequest.setGender(null));
        String nullLogin = updateAndGetUserRequestAsJson(userRequest -> userRequest.setLogin(null));

        return Stream.of(
                Arguments.of(nullDob),
                Arguments.of(nullFullName),
                Arguments.of(nullGender),
                Arguments.of(nullLogin)
        );
    }
    
    @SneakyThrows
    public static String updateAndGetUserRequestAsJson(Consumer<User> consumer) {
        User userRequest = validUserRequest();
        consumer.accept(userRequest);
        return OBJECT_MAPPER.writeValueAsString(userRequest);
    }

    private static User validUserRequest() {
        User user = new User();
        user.setDob(DATE_1995_10_05);
        user.setFullName(NAME_PETRO);
        user.setGender(Gender.MALE);
        user.setLogin(LOGIN_OTHER);

        return user;
    }
}
