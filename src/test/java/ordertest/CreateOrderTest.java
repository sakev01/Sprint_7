package ordertest;

import client.OrderClient;
import data.OrderData;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Test;
import org.junit.Before;

import java.util.*;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private OrderClient orderClient;
    private List<String> color;

    // Конструктор для параметров теста
    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        this.orderClient = new OrderClient();
    }

    // Параметры для теста
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { Arrays.asList("BLACK") }, // можно указать один из цветов — BLACK или GREY;
                { Arrays.asList("GREY") }, //можно указать один из цветов — BLACK или GREY;
                { Arrays.asList("BLACK", "GREY") }, // можно указать оба цвета;
                { Arrays.asList() }  // можно совсем не указывать цвет;
        });
    }

    @Test
    public void testCreateOrderWithColor() {
        OrderData orderData = new OrderData("Naruto", "Uchiha", "Konoha, 142 apt.",
                4, "+7 800 355 35 35", 5, "2020-06-06",
                "Saske, come back to Konoha", this.color);
        ValidatableResponse response = orderClient.createOrder(orderData);
        response.assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", notNullValue());
    }
    @Test
    public void testGetOrdersReturnsListOfOrders() {
        ValidatableResponse response = orderClient.getOrders();
        response.assertThat()
                .statusCode(HttpStatus.SC_OK) // Проверка, что статус-код ответа — 200 OK
                .body("orders", is(notNullValue())) // Проверка - тело ответа содержит список заказов
                .body("orders", hasSize(greaterThan(0))); // Проверк - что список заказов не пустой
    }
    @Test
    public void testGetOrdersWithNonExistentCourierId() {
        Map<String, Object> params = new HashMap<>();
        params.put("courierId", 0); // ID не существует

        ValidatableResponse response = orderClient.getOrdersWithParams(params);
        response.assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", is("Курьер с идентификатором 0 не найден"));
    }
}
