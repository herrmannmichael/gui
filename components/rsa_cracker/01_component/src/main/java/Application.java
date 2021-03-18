import java.io.File;
import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException, RSACrackerException {
        File file = new File("C:\\Users\\arthu\\OneDrive\\DHBW\\Semester 4\\Kryptographie\\Komplexaufgabe\\gui\\key\\publicKeyfile.json");
        Cipher cipher = Cipher.getInstance();
        System.out.println(cipher.innerDecrypt("6773,5391,9580,2358,9464,10123,5669", file));
    }
}
