import java.io.File;
import java.io.IOException;

public interface ICaesarCipher {
    String encrypt(String plainMessage, File keyfile) throws IOException;
    String decrypt(String encryptedMessage, File keyfile) throws IOException;
}