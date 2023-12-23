package data;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {
    public static CourierData getRandomCourier() {
        String login = RandomStringUtils.randomAlphabetic(6);
        String password = RandomStringUtils.randomAlphabetic(6);
        String firstName = RandomStringUtils.randomAlphabetic(6);

        return new CourierData(login, password, firstName);
    }
}
