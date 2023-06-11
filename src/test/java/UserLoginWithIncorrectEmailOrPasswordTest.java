import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class UserLoginWithIncorrectEmailOrPasswordTest {
    private UserClient userClient;
    private String accessToken;
    private final String field;
    private final String value;
    private UserCredentials credentials;

    public UserLoginWithIncorrectEmailOrPasswordTest(String field, String value){
        this.field = field;
        this.value = value;
    }

    @Parameterized.Parameters(name = "{index}: Поле - {0}, значение {1}")
    public static Object[][] orderData() {
        return new Object[][]{
                {"email", UserGenerator.getRandomEmail()},
                {"password", UserGenerator.getRandomPassword()},
                {"email", null},
                {"password", null}
        };
    }

    @Before
    public void setUp(){
        userClient = new UserClient();
        //создаем пользователя
        User user = UserGenerator.getRandomUser();
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        credentials = UserCredentials.from(user);
    }

    @After
    public void cleanUp(){
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("API. POST '/api/auth/login'. Вход невозможен с некорректным или отсутствующим email или паролем")
    public void userLoginWithIncorrectEmailOrPasswordTest(){
        if (field.equals("password")) {
            credentials.setPassword(value);
        } else {
            credentials.setEmail(value);
        }
        ValidatableResponse loginResponse = userClient.loginUser(credentials);
        int statusCode = loginResponse.extract().statusCode();
        boolean isUserLogged = loginResponse.extract().path("success");
        String message = loginResponse.extract().path("message");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isUserLogged);
        assertEquals("email or password are incorrect", message);
    }
}
