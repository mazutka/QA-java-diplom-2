import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserCreateAndLoginTest {
    private UserClient userClient;
    private String accessToken;
    @Before
    public void setUp(){
        userClient = new UserClient();
    }

    @After
    public void cleanUp(){
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("API. POST '/api/auth/register', POST /api/auth/login. Создание пользователя и успешный логин")
    public void userCreateAndLoginTest(){
        //создаем пользователя
        User user = UserGenerator.getRandomUser();
        ValidatableResponse createResponse = userClient.createUser(user);
        int statusCode = createResponse.extract().statusCode();
        boolean isUserCreated = createResponse.extract().path("success");
        String userEmail = createResponse.extract().path("user.email");
        String userName = createResponse.extract().path("user.name");
        assertEquals(SC_OK, statusCode);
        assertTrue(isUserCreated);
        assertEquals(user.getEmail().toLowerCase(), userEmail);
        assertEquals(user.getName(), userName);

        //логинемся под пользователем
        ValidatableResponse loginResponse = userClient.loginUser(UserCredentials.from(user));
        statusCode = loginResponse.extract().statusCode();
        boolean isUserLogged = loginResponse.extract().path("success");
        accessToken = loginResponse.extract().path("accessToken");
        userEmail = loginResponse.extract().path("user.email");
        userName = loginResponse.extract().path("user.name");
        assertEquals(SC_OK, statusCode);
        assertTrue(isUserLogged);
        assertEquals(user.getEmail().toLowerCase(), userEmail);
        assertEquals(user.getName(), userName);
    }

}
