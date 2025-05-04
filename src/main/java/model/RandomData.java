package model;

import net.datafaker.Faker;

public class RandomData {
    private static final Faker faker = new Faker();
    public static UserData generateRandomUser() {
        return new UserData(
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().fullName()
        );
    }
    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }

}
