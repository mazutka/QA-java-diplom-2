import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserChangeDataWithoutAuthorizationTest {
    private UserClient userClient;
    private String accessToken;

    private User user;

    @Before
    public void setUp(){
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
    }

    @After
    public void cleanUp(){
        userClient.deleteUser(accessToken);
    }
    @Test
    @DisplayName("API. PATCH '/api/auth/user'. Нельзя изменить данные пользователя без авторизации")
    public void userChangeDataWithoutAuthorizationTest(){
        user = UserGenerator.getRandomUser();
        ValidatableResponse updateResponse;
        updateResponse = userClient.updateUser(null, user);
        int statusCode = updateResponse.extract().statusCode();
        boolean isUserUpdated = updateResponse.extract().path("success");
        String message = updateResponse.extract().path("message");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isUserUpdated);
        assertEquals("You should be authorised",message);
    }
}
