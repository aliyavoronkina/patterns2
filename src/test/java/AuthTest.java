import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import java.util.UUID;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {
    private static final String BASE_URL = "http://localhost:9999";

    @BeforeAll
    static void setUpAll() {
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;
        Configuration.timeout = 15000;

        // Уникальный user-data-dir для каждого запуска
        System.setProperty("chromeoptions.args",
                "--remote-allow-origins=* " +
                        "--disable-dev-shm-usage " +
                        "--no-sandbox " +
                        "--disable-gpu " +
                        "--user-data-dir=/tmp/chrome-profile-" + UUID.randomUUID()
        );
    }

    @BeforeEach
    void setUp() {
        open(BASE_URL);
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    @Test
    void shouldLoginWithActiveUser() {
        DataGenerator.RegistrationDto registeredUser = DataGenerator.getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=login]").shouldNotBe(visible);
        $("[data-test-id=password]").shouldNotBe(visible);
        $("h2").shouldBe(visible).shouldHave(exactText("Личный кабинет"));
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        DataGenerator.RegistrationDto blockedUser = DataGenerator.getRegisteredUser("blocked");

        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=login]").shouldBe(visible);
        $("[data-test-id=password]").shouldBe(visible);
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

        $("[data-test-id=login]").shouldBe(visible);
        $("[data-test-id=password]").shouldBe(visible);
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

        $("[data-test-id=login]").shouldBe(visible);
        $("[data-test-id=password]").shouldBe(visible);
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithNotRegisteredUser() {
        $("[data-test-id=login] input").setValue("notregistereduser");
        $("[data-test-id=password] input").setValue("randompassword123");
        $("[data-test-id=action-login]").click();

        $("[data-test-id=login]").shouldBe(visible);
        $("[data-test-id=password]").shouldBe(visible);
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(visible)
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}