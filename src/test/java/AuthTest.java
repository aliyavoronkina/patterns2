import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {
    private static final String BASE_URL = "http://localhost:9999";

    @BeforeAll
    static void setUpAll() {
        Configuration.browserSize = "1280x800";
        Configuration.headless = true;
    }

    @BeforeEach
    void setUp() {
        open(BASE_URL);
    }

    @Test
    void shouldLoginWithActiveUser() {
        DataGenerator.RegistrationDto registeredUser = DataGenerator.getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();

        // Проверяем успешный вход
        $("h2").shouldBe(visible).shouldHave(text("Личный кабинет"));
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        DataGenerator.RegistrationDto blockedUser = DataGenerator.getRegisteredUser("blocked");

        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();

        // Проверяем сообщение об ошибке для заблокированного пользователя
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(visible)
                .shouldHave(text("заблокирован"));
    }

    @Test
    void shouldNotLoginWithInvalidLogin() {
        DataGenerator.RegistrationDto user = DataGenerator.getRegisteredUser("active");

        $("[data-test-id=login] input").setValue("invalid_" + user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();

        // Проверяем сообщение об ошибке неверного логина/пароля
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithInvalidPassword() {
        DataGenerator.RegistrationDto user = DataGenerator.getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue("invalid_" + user.getPassword());
        $("[data-test-id=action-login]").click();

        // Проверяем сообщение об ошибке неверного логина/пароля
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithNotRegisteredUser() {
        String randomLogin = "notregistereduser";
        String randomPassword = "randompassword123";

        $("[data-test-id=login] input").setValue(randomLogin);
        $("[data-test-id=password] input").setValue(randomPassword);
        $("[data-test-id=action-login]").click();

        // Проверяем сообщение об ошибке для незарегистрированного пользователя
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}