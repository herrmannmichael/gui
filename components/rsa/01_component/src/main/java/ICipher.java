import java.io.File;
import java.io.IOException;

public interface ICipher {
    String encrypt(String plainMessage, File publicKeyfile) throws IOException;
    String decrypt(String encryptedMessage, File privateKeyfile) throws IOException;
}