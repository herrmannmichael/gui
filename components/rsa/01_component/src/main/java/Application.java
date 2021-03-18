import java.io.File;
import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        Cipher cipher = Cipher.getInstance();

        File file = new File("C:\\Users\\arthu\\OneDrive\\DHBW\\Semester 4\\Kryptographie\\Komplexaufgabe\\gui\\key\\publicKeyfile.json");
        File filePrivate = new File("C:\\Users\\arthu\\OneDrive\\DHBW\\Semester 4\\Kryptographie\\Komplexaufgabe\\gui\\key\\privateKeyfile.json");

        String enryptedMessage = cipher.innerEncrypt("test", file);
        String decryptedMessage = cipher.innerDecrypt(enryptedMessage, filePrivate);

        System.out.println(enryptedMessage + " " + decryptedMessage);


    }
}
