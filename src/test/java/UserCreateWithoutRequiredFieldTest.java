import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
@RunWith(Parameterized.class)
public class UserCreateWithoutRequiredFieldTest {
    private UserClient userClient;
    private final User user;

    public UserCreateWithoutRequiredFieldTest(User user){
        this.user = user;
    }

    @Parameterized.Parameters(name = "{index}: Данные нового пользователя - {0}")
    public static Object[][] orderData() {
        return new Object[][]{
                {UserGenerator.getRandomWithoutPassword()},
                {UserGenerator.getRandomWithoutEmail()},
                {UserGenerator.getRandomWithoutName()}
        };
    }

    @Before
    public void setUp(){
        userClient = new UserClient();
    }

    @Test
    @DisplayName("API. POST '/api/auth/register'. Нельзя создать пользователя без email, пароля или имени")
    @Step("email - {this.user.email}/ Пароль - {this.user.password}/ Имя - {this.user.name}")
    public void uerCreateWithoutRequiredFieldTest(){
        ValidatableResponse createResponse = userClient.createUser(user);
        int statusCode = createResponse.extract().statusCode();
        boolean isUserCreated = createResponse.extract().path("success");
        String message =createResponse.extract().path("message");
        assertEquals(SC_FORBIDDEN, statusCode);
        assertFalse(isUserCreated);
        assertEquals("Email, password and name are required fields", message);
    }
}
