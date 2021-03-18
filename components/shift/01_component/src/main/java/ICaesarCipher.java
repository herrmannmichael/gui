import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public interface ICaesarCipher {
    String encrypt(String plainMessage, File keyfile) throws IOException, ParseException;
    String decrypt(String encryptedMessage, File keyfile) throws IOException, ParseException;
}