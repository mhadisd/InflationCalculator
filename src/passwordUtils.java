import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class passwordUtils {

    public static String hashPassword(String password, String fixedSalt) {
        // Generate a fixed salt based on the person's username
        byte[] salt = (fixedSalt + password).getBytes();
        try {
            // Hash the password using the salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());

            // Combine the salt and hashed password into a single byte array
            byte[] saltedHash = new byte[salt.length + bytes.length];
            System.arraycopy(salt, 0, saltedHash, 0, salt.length);
            System.arraycopy(bytes, 0, saltedHash, salt.length, bytes.length);

            // Encode the salted hash as a Base64 string and return it
            return Base64.getEncoder().encodeToString(saltedHash);
        } catch (NoSuchAlgorithmException ex) {
            // Print an error message if there was an error hashing the password
            System.out.println("Error hashing password: " + ex.getMessage());
            return "";
        }
    }

}
