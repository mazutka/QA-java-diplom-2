import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static junit.framework.TestCase.assertEquals;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertFalse;

public class UserGetListOrdersWithoutAuthorizationTest {
    @Test
    @DisplayName("API. GET '/api/orders'. Получение списка заказов пользователя без авторизации")
    public void userGetListOrdersWithoutAuthorizationTest(){
        OrderClient orderClient = new OrderClient();
        ValidatableResponse userOrdersResponse = orderClient.getUserOrders(null);
        int statusCode = userOrdersResponse.extract().statusCode();
        boolean isGetUserOrders = userOrdersResponse.extract().path("success");
        String message = userOrdersResponse.extract().path("message");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isGetUserOrders);
        assertEquals("You should be authorised",message);
    }
}
