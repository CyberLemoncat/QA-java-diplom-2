package praktikum;

import model.Ingredients;

import java.util.List;

import static constants.Endpoints.GET_INGREDIENTS;
import static io.restassured.RestAssured.given;

public class  GetIngredients {
    public static List<Ingredients>  getAllIngredients() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(GET_INGREDIENTS)
                .jsonPath().getList("data", Ingredients.class);
    }
}
