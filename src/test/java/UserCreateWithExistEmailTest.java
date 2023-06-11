import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.*;

public class UserCreateWithExistEmailTest {
    private UserClient userClient;
    private String accessToken;
    private User user;

    @Before
    public void setUp(){
        userClient = new UserClient();
        //создаем пользователя
        user = UserGenerator.getRandomUser();
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        //изменяем пароль и имя
        user.setPassword(UserGenerator.getRandomPassword());
        user.setName(UserGenerator.getRandomName());
    }

    @After
    public void cleanUp(){
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("API. POST '/api/auth/register'. Создание пользователя с email, который был зарегистрирован ранее невозможно.")
    public void userCreateWithExistEmailTest(){
        ValidatableResponse createResponse = userClient.createUser(user);
        int statusCode = createResponse.extract().statusCode();
        boolean isUserCreated = createResponse.extract().path("success");
        String message = createResponse.extract().path("message");
        assertEquals(SC_FORBIDDEN, statusCode);
        assertFalse(isUserCreated);
        assertEquals("User already exists",message);
    }

}
