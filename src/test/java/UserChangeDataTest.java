import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class UserChangeDataTest {

    private UserClient userClient;
    private String accessToken;
    private User user;
    private final User userChanges;


    public UserChangeDataTest(User userChanges){
        this.userChanges = userChanges;
    }

    @Parameterized.Parameters(name = "{index}: Значения изменяемых полей - {0}")
    public static Object[][] orderData() {
        return new Object[][]{
                {UserGenerator.getRandomUser()},
                {UserGenerator.getRandomUserOnlyEmail()},
                {UserGenerator.getRandomUserOnlyName()},
                {UserGenerator.getRandomUserOnlyPassword()},
        };
    }
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
    @DisplayName("API. PATCH '/api/auth/user'. Изменение данных пользователя")
    @Step("email - {this.userChanges.email}/ Пароль - {this.userChanges.password}/ Имя - {this.userChanges.name}")
    public void userChangeDataTest(){
        user = user.userUpdate(userChanges);
        ValidatableResponse updateResponse;
        updateResponse = userClient.updateUser(accessToken, user);
        int statusCode = updateResponse.extract().statusCode();
        boolean isUserUpdated = updateResponse.extract().path("success");
        String userEmail = updateResponse.extract().path("user.email");
        String userName = updateResponse.extract().path("user.name");
        assertEquals(SC_OK, statusCode);
        assertTrue(isUserUpdated);
        assertEquals(user.getEmail().toLowerCase(), userEmail);
        assertEquals(user.getName(), userName);
    }
}
