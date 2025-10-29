import java.util.Random;

public class DataGenerator {
    private static final String BASE_URL = "http://localhost:9999";
    private static final Random random = new Random();

    private DataGenerator() {
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto generateUser(String status) {
            return new RegistrationDto(generateRandomLogin(), generateRandomPassword(), status);
        }

        public static void registerUser(RegistrationDto user) {
        }

        public static String generateRandomLogin() {
            return generateRandomString(10);
        }

        public static String generateRandomPassword() {
            return generateRandomString(10);
        }

        private static String generateRandomString(int length) {
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            return sb.toString();
        }
    }
}