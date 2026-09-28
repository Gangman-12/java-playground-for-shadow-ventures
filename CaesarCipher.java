import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class CaesarCipher {

    private static final String[][] NOTES = {
        {
            "Cryptography protects information by changing it into a form that others cannot easily understand.",
            "Encryption changes readable plaintext into ciphertext using an algorithm and a key.",
            "Decryption turns ciphertext back into readable plaintext when you use the correct key."
        },
        {
            "A Caesar cipher shifts each letter by a fixed number. Its small number of keys makes it easy to break.",
            "Symmetric encryption uses the same secret key to encrypt and decrypt information.",
            "Public-key cryptography uses a public key and a related private key that must remain secret."
        },
        {
            "A hash function creates a fixed-size digest. A hash is designed to be one-way, not decrypted.",
            "A digital signature can help verify who signed data and whether it was changed afterward.",
            "Good security depends on trusted algorithms, protected keys, and careful implementation."
        }
    };

    // Simple Caesar Cipher Encryption (Educational Only)
    public static String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray()) {
            if (Character.isUpperCase(character)) {
                char ch = (char)(((int)character + shift - 65) % 26 + 65);
                result.append(ch);
            } else if (Character.isLowerCase(character)) {
                char ch = (char)(((int)character + shift - 97) % 26 + 97);
                result.append(ch);
            } else {
                result.append(character); // Keep numbers/punctuation unchanged
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int roundsPerLevel = NOTES[0].length;

        System.out.println("Caesar Cipher Achievement Game");
        System.out.println("Decode each note to unlock cryptography information.");
        System.out.println("Each level has " + roundsPerLevel + " notes.");

        for (int level = 1; level <= NOTES.length; level++) {
            System.out.println("\n=== Level " + level + " ===");

            for (int round = 1; round <= roundsPerLevel; round++) {
                playRound(input, level, round, NOTES[level - 1][round - 1]);
                System.out.println("Congratulations! Achievement " + round + " of "
                        + roundsPerLevel + " earned in Level " + level + ".");
            }

            if (level < NOTES.length) {
                System.out.println("Level up! You reached Level " + (level + 1) + ".");
            }
        }

        System.out.println("\nCongratulations! You completed all " + NOTES.length + " levels!");
        input.close();
    }

    private static void playRound(Scanner input, int level, int round, String note) {
        System.out.println("\nRound " + round + " of Level " + level);
        int secretShift = ThreadLocalRandom.current().nextInt(1, 26);
        String encrypted = encrypt(note, secretShift);
        System.out.println("Cipher text: " + encrypted);

        int lowestPossibleKey = 1;
        int highestPossibleKey = 25;
        int midpoint = (lowestPossibleKey + highestPossibleKey) / 2;
        if (secretShift <= midpoint) {
            highestPossibleKey = midpoint;
        } else {
            lowestPossibleKey = midpoint + 1;
        }

        System.out.println("Hint: Each letter was shifted forward by the encryption key.");
        System.out.println("To decrypt, enter that same shift as a number from 1 to 25.");
        System.out.println("The key is in this range: " + lowestPossibleKey + " to "
                + highestPossibleKey + ". The range includes both numbers.");

        while (true) {
            System.out.print("Enter the decryption key: ");
            String keyInput = input.nextLine();
            int guessedKey;

            try {
                guessedKey = Integer.parseInt(keyInput);
            } catch (NumberFormatException exception) {
                System.out.println("Enter a whole number from 1 to 25.");
                continue;
            }

            if (guessedKey < 1 || guessedKey > 25) {
                System.out.println("That key is outside the allowed range of 1 to 25.");
            } else if (guessedKey < lowestPossibleKey || guessedKey > highestPossibleKey) {
                System.out.println("That guess is outside the current hint range of "
                        + lowestPossibleKey + " to " + highestPossibleKey + ".");
            } else if (guessedKey == secretShift) {
                System.out.println("Correct! You unlocked this note:");
                System.out.println(encrypt(encrypted, 26 - guessedKey));
                break;
            } else if (guessedKey < secretShift) {
                lowestPossibleKey = guessedKey + 1;
                System.out.println("Too low. Try a higher key. The new possible range is "
                        + lowestPossibleKey + " to " + highestPossibleKey + ".");
            } else {
                highestPossibleKey = guessedKey - 1;
                System.out.println("Too high. Try a lower key. The new possible range is "
                        + lowestPossibleKey + " to " + highestPossibleKey + ".");
            }
        }
    }
}