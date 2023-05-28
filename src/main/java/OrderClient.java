import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String ORDER_INGREDIENTS_PATH = "/api/ingredients";
    private static final String ORDER_CREATE_PATH = "/api/orders";
    private static final String ORDERS_USER_PATH = "/api/orders";

    public ValidatableResponse getIngredients(){
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_INGREDIENTS_PATH)
                .then();
    }

    public ValidatableResponse createOrder(String accessToken, Order order){
        return given()
                .header(new Header("Authorization",accessToken))
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_CREATE_PATH)
                .then();
    }

    public ValidatableResponse getUserOrders(String accessToken){
        return given()
                .header(new Header("Authorization",accessToken))
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_USER_PATH)
                .then();
    }


}
