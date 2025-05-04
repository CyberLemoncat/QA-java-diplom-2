package praktikum;

import model.RandomData;
import model.UserData;
import org.junit.Before;

public class Model extends BaseClass {
    protected UserData randomUser;
    protected String accessToken;

    @Before
    public void setUpTestData() {
        randomUser = RandomData.generateRandomUser();
    }
}
