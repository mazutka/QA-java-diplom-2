import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.apache.http.HttpStatus.SC_OK;

public class UserGetListOrdersTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;
    private List<Integer> expOrdersNumbersList;

    @Before
    public void setUp(){
        userClient = new UserClient();
        User user = UserGenerator.getRandomUser();
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        orderClient = new OrderClient();
        ValidatableResponse orderResponse;
        Order order;
        expOrdersNumbersList = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            List<String> listIngredients = new ArrayList<>();
            order = new Order(IngredientsListGenerator.randomIngredientsList(i,false));
            orderResponse = orderClient.createOrder(accessToken, order);
            expOrdersNumbersList.add(orderResponse.extract().path("order.number"));
        }
    }

    @After
    public void cleanUp(){
        userClient.deleteUser(accessToken);
    }
    @Test
    @DisplayName("API. GET '/api/orders'. Получение списка заказов")
    public void userGetListOrdersTest() {
        ValidatableResponse orderResponse = orderClient.getUserOrders(accessToken);
        int statusCode = orderResponse.extract().statusCode();
        boolean isGetOrdersList = orderResponse.extract().path("success");
        List<String> actOrdersNumbersList = orderResponse.extract().jsonPath().getList("orders.number");
        assertEquals(SC_OK, statusCode);
        assertTrue(isGetOrdersList);
        assertEquals(expOrdersNumbersList.size(),actOrdersNumbersList.size());
        assertTrue(actOrdersNumbersList.containsAll(expOrdersNumbersList));
    }
}
