package couriertest;

import client.CourierClient;
import data.CourierCredentials;
import data.CourierData;
import data.CourierGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.apache.http.HttpStatus;

public class CreateCourierTest {
    private CourierClient courierClient;
    private CourierData courier;
    private Integer courierId;

    @Before
    public void setUp() {
      courierClient = new CourierClient();
    }

    @After
    public void cleanUp() {
        //написать проверку: если курьер создан -- удаляем
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Courier logged in")
    public void courierCanBeCreatedAndLoggedIn() {
        // Courier - Создание курьера
        courier = CourierGenerator.getRandomCourier();

        ValidatableResponse createResponse = courierClient.createCourier(courier);
        createResponse.assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));

        // Получение и сохранение courierId
        courierId = createResponse.extract().path("id");

        // Courier - Логин курьера в системе
        CourierCredentials credentials = CourierCredentials.from(courier);
        ValidatableResponse responseLogin = courierClient.loginCourier(credentials);
        responseLogin.assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Creation duplicate courier")
    public void cannotCreateDuplicateCourier() {
        // Создаем курьера
        courier = CourierGenerator.getRandomCourier();

        ValidatableResponse createResponse = courierClient.createCourier(courier);
        createResponse.assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("ok", is(true));

        // Пытаемся создать того же курьера снова
        ValidatableResponse duplicateCreateResponse = courierClient.createCourier(courier);
        duplicateCreateResponse.assertThat()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Create courier without login or password")
    public void cannotCreateCourierWithoutLoginOrPassword() {

        // курьера без логина
        CourierData courierWithoutLogin = new CourierData(null, "password", "Name");

        // Попытка создать курьера без логина
        ValidatableResponse responseWithoutLogin = courierClient.createCourier(courierWithoutLogin);
        responseWithoutLogin.assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));

        // курьера без пароля
        CourierData courierWithoutPassword = new CourierData("login", null, "Name");

        // Попытка создать курьера без пароля
        ValidatableResponse responseWithoutPassword = courierClient.createCourier(courierWithoutPassword);
        responseWithoutPassword.assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

}
