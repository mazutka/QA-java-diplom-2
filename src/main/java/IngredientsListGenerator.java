import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class IngredientsListGenerator {

    public static List<String> randomIngredientsList(int countIngredientsInOrder, boolean isIncorrectIngredient){

        List<String> ingredientsInOrder = new ArrayList<>();
        OrderClient orderClient = new OrderClient();
        ValidatableResponse getIngredientsResponse = orderClient.getIngredients();
        List<String> ingredientsList = getIngredientsResponse.extract().jsonPath().getList("data._id");
        int countIngredientsList = ingredientsList.size();
        if (isIncorrectIngredient) {
            Faker faker = new Faker();
            ingredientsInOrder.add(faker.crypto().md5().substring(0,24));
            countIngredientsInOrder = countIngredientsInOrder -1;
        }
        for (int i = 0; i < countIngredientsInOrder; i++) {
            ingredientsInOrder.add(ingredientsList.get(RandomUtils.nextInt(0,countIngredientsList-1)));
        }
        return ingredientsInOrder;
    }
}
