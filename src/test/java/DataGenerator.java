import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    public static RegistrationDto getRegisteredUser(String status) {
        String login = faker.name().username();
        String password = faker.internet().password();
        RegistrationDto user = new RegistrationDto(login, password, status);

        // Регистрируем пользователя через API
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        return user;
    }

    public static class RegistrationDto {
        private String login;
        private String password;
        private String status;

        public RegistrationDto(String login, String password, String status) {
            this.login = login;
            this.password = password;
            this.status = status;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getStatus() {
            return status;
        }
    }
}