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

    public static void registerUser(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static RegistrationDto getRegisteredUser(String status) {
        String login = generateLogin();
        String password = generatePassword();
        RegistrationDto user = new RegistrationDto(login, password, status);
        registerUser(user);
        return user;
    }

    public static RegistrationDto getRegisteredActiveUser() {
        return getRegisteredUser("active");
    }

    public static RegistrationDto getRegisteredBlockedUser() {
        return getRegisteredUser("blocked");
    }

    public static RegistrationDto getWrongLoginUser() {
        return new RegistrationDto(generateLogin(), generatePassword(), "active");
    }

    public static RegistrationDto getWrongPasswordUser() {
        String login = generateLogin();
        RegistrationDto user = new RegistrationDto(login, generatePassword(), "active");
        registerUser(user);
        return new RegistrationDto(login, generatePassword(), "active");
    }

    public static String generateLogin() {
        return faker.name().username();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }
}