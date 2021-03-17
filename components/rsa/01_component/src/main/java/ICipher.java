import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public interface ICipher {
    String encrypt(String plainMessage, File publicKeyfile) throws IOException, ParseException;
    String decrypt(String encryptedMessage, File privateKeyfile) throws IOException, ParseException;
}