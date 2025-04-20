package praktikum;

import com.google.gson.Gson;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import org.junit.Before;

public abstract class BaseClass {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
@Before
public void SetUp(){
    RestAssured.baseURI = BASE_URI;
    RestAssured.filters(new AllureRestAssured());
    RestAssured.config = RestAssured.config().objectMapperConfig(
            ObjectMapperConfig.objectMapperConfig().gsonObjectMapperFactory((type, s) -> new Gson())
    );
}

}
