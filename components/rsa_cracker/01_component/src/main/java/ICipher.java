import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public interface ICipher {
    String decrypt(String  encryptedMessage, File publicKeyFile) throws IOException, ParseException, RSACrackerException;
}