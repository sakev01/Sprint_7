package dummytest;

import client.OrderClient;
import data.OrderData;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;
import java.util.Collections;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderBeginnerTest {
    private OrderClient orderClient = new OrderClient();

    @Test
    public void createOrderTest() {
        OrderData orderData = new OrderData(
                "Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35",
                5, "2020-06-06", "Saske, come back to Konoha",
                Collections.singletonList("BLACK")
        );
        ValidatableResponse response = orderClient.createOrder(orderData);
        response.assertThat()
                .statusCode(HttpStatus.SC_CREATED) // Проверка статус-кода
                .body("track", is(notNullValue())); // Проверка наличия track в ответе
    }
}
