import java.io.File;
import java.io.IOException;

public interface ICipher {
    String decrypt(String  encryptedMessage, File publicKeyFile) throws IOException, RSACrackerException;
}