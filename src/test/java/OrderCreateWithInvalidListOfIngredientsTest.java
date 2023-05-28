import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class OrderCreateWithInvalidListOfIngredientsTest {

    private UserClient userClient;
    private String accessToken;
    private final List<String> listIngredients;
    private final String expMessage;


    public OrderCreateWithInvalidListOfIngredientsTest(List<String> listIngredients, String expMessage){
        this.listIngredients = listIngredients;
        this.expMessage = expMessage;
    }

    @Parameterized.Parameters(name = "{index}: Список ингредиентов - {0}, Проверяемое сообщение об ошибке - {1}")
    public static Object[][] orderData() {
        return new Object[][]{
                {IngredientsListGenerator.randomIngredientsList(0,false), "Ingredient ids must be provided"},
                {IngredientsListGenerator.randomIngredientsList(1,true), "One or more ids provided are incorrect"},
                {IngredientsListGenerator.randomIngredientsList(2,true), "One or more ids provided are incorrect"},
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
    @DisplayName("API. POST '/api/orders'. Создание заказа c некорректным списком ингредиентов")
    public void orderCreateWithInvalidListOfIngredientsTest(){
        OrderClient orderClient = new OrderClient();
        Order order = new Order(listIngredients);
        ValidatableResponse orderResponse = orderClient.createOrder(accessToken, order);
        int statusCode = orderResponse.extract().statusCode();
        boolean isOrderCreated = orderResponse.extract().path("success");
        String message = orderResponse.extract().path("message");
        assertEquals(SC_BAD_REQUEST, statusCode);
        assertFalse(isOrderCreated);
        assertEquals(expMessage,message);
    }
}
