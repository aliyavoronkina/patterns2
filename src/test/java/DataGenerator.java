import org.apache.commons.lang3.RandomStringUtils;

public class DataGenerator {
    private DataGenerator() {
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto generateUser(String status) {
            return new RegistrationDto(generateRandomLogin(), generateRandomPassword(), status);
        }

        public static RegistrationDto generateUserWithInvalidLogin() {
            return new RegistrationDto(generateRandomLogin(), generateRandomPassword(), "active");
        }

        public static RegistrationDto generateUserWithInvalidPassword() {
            return new RegistrationDto(generateRandomLogin(), generateRandomPassword(), "active");
        }

        public static String generateRandomLogin() {
            return RandomStringUtils.randomAlphanumeric(10);
        }

        public static String generateRandomPassword() {
            return RandomStringUtils.randomAlphanumeric(10);
        }
    }
}