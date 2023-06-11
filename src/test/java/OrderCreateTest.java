import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.apache.http.HttpStatus.SC_OK;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private UserClient userClient;
    private String accessToken;
    private final List<String> listIngredients;


    public OrderCreateTest(List<String> listIngredients){
        this.listIngredients = listIngredients;
    }

    @Parameterized.Parameters(name = "{index}: Список ингредиентов - {0}")
    public static Object[][] orderData() {
        return new Object[][]{
                {IngredientsListGenerator.randomIngredientsList(1,false)},
                {IngredientsListGenerator.randomIngredientsList(10,false)},
        };
    }

    @Before
    public void setUp(){
        userClient = new UserClient();
        User user = UserGenerator.getRandomUser();
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
    }

    @After
    public void cleanUp(){
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("API. POST '/api/orders'. Создание заказа")
    public void orderCreateTest(){
        OrderClient orderClient = new OrderClient();
        Order order = new Order(listIngredients);
        ValidatableResponse orderResponse = orderClient.createOrder(accessToken, order);
        int statusCode = orderResponse.extract().statusCode();
        boolean isOrderCreated = orderResponse.extract().path("success");
        List<String> actListIngredients = orderResponse.extract().jsonPath().getList("order.ingredients._id");
        assertEquals(SC_OK, statusCode);
        assertTrue(isOrderCreated);
        assertEquals(listIngredients.size(),actListIngredients.size());
        assertTrue(actListIngredients.containsAll(listIngredients));
    }
}
