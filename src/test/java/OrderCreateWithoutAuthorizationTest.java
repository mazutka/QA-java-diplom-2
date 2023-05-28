import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertFalse;

public class OrderCreateWithoutAuthorizationTest {
    @Test
    @DisplayName("API. POST '/api/orders'. Создание заказа без авторизации")
    public void orderCreateWithoutAuthorizationTest(){
        OrderClient orderClient = new OrderClient();
        Order order = new Order(IngredientsListGenerator.randomIngredientsList(5, false));
        ValidatableResponse orderResponse = orderClient.createOrder(null, order);
        int statusCode = orderResponse.extract().statusCode();
        boolean isOrderCreated = orderResponse.extract().path("success");
        String message = orderResponse.extract().path("message");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isOrderCreated);
        Assert.assertEquals("You should be authorised",message);
    }
}
