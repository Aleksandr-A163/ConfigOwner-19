package helpers;

import com.github.javafaker.Faker;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FakerData {
    private static final Faker faker = new Faker();

    public static String generatePassword(int minLength, int maxLength) {
        // Ensure the base characters for the password
        char lowerCase = faker.regexify("[a-z]").charAt(0);
        char upperCase = faker.regexify("[A-Z]").charAt(0);
        char oneDigit = faker.regexify("[0-9]").charAt(0);
        char specialChar = faker.regexify("[!@#$%^&*()]").charAt(0);

        // Generate the rest of the password
        String remainingChars = faker.internet().password(minLength - 3, maxLength - 3, true, true, true);

        // Combine all parts
        String combined = "" + lowerCase + upperCase + + oneDigit + specialChar + remainingChars;

        // Shuffle the combined characters to ensure randomness
        List<Character> passwordChars = combined.chars()
                                                .mapToObj(e -> (char)e)
                                                .collect(Collectors.toList());
        Collections.shuffle(passwordChars);

        // Convert back to string
        StringBuilder passwordBuilder = new StringBuilder();
        for (char c : passwordChars) {
            passwordBuilder.append(c);
        }

        return passwordBuilder.toString();
    }

    public static String generateUsername() {
        return faker.name().username();
    }
}