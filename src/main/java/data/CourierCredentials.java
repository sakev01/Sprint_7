package data;

public class CourierCredentials {
    private String login;
    private String password;

    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCredentials from(CourierData courier) {
        return new CourierCredentials(courier.getLogin(), courier.getPassword());
    }

    // Геттеры и сеттеры
    public String getLogin() { return login; }
    public String getPassword() { return password; }
}
