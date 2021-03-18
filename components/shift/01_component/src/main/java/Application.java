import java.io.File;
import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\arthu\\OneDrive\\DHBW\\Semester 4\\Kryptographie\\Komplexaufgabe\\gui\\key\\keyfile.json");
        CaesarCipher cipher = CaesarCipher.getInstance();
        String encryptedMessage = cipher.innerEncrypt("schwanzlecken", file);
        String decryptedMessage = cipher.innerDecrypt(encryptedMessage, file);
        System.out.println(encryptedMessage + " " + decryptedMessage);
    }
}
