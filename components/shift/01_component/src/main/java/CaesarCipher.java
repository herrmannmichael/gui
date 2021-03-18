import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CaesarCipher {
    // static instance
    private static final CaesarCipher instance = new CaesarCipher();
    // port
    public Port port;

    // private constructor
    private CaesarCipher() {
        port = new Port();
    }

    // static method getInstance
    public static CaesarCipher getInstance() {
        return instance;
    }

    // inner methods

    public String innerEncrypt(String plainMessage, File keyfile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < plainMessage.length(); i++) {
            char character = (char) (plainMessage.codePointAt(i) + readKey(keyfile));
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    public String innerDecrypt(String encryptedMessage, File keyfile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < encryptedMessage.length(); i++) {
            char character = (char) (encryptedMessage.codePointAt(i) - readKey(keyfile));
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private int readKey(File keyfile) throws IOException {
        String content = Files.readString(Path.of(keyfile.getPath()), StandardCharsets.US_ASCII);
        JSONObject object = new JSONObject(content);
        String keyString = (String) object.get("key");
        return Integer.parseInt(keyString);
    }

    // inner class port
    public class Port implements ICaesarCipher {

        @Override
        public String encrypt(String plainMessage, File keyfile) throws IOException {
            return innerEncrypt(plainMessage, keyfile);
        }

        @Override
        public String decrypt(String encryptedMessage, File keyfile) throws IOException {
            return innerDecrypt(encryptedMessage, keyfile);
        }
    }
}