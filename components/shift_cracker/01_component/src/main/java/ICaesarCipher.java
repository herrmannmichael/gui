import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public interface ICaesarCipher {
    String decrypt(String encryptedMessage);
}