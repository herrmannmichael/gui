import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CaesarCipher {
    // static instance
    private static CaesarCipher instance = new CaesarCipher();
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

    public String innerEncrypt(String plainMessage, File keyfile) throws IOException, ParseException {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < plainMessage.length(); i++) {
            char character = (char) (plainMessage.codePointAt(i) + readKey(keyfile));
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    public String innerDecrypt(String encryptedMessage, File keyfile) throws IOException, ParseException {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < encryptedMessage.length(); i++) {
            char character = (char) (encryptedMessage.codePointAt(i) - readKey(keyfile));
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private int readKey(File keyfile) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(new FileReader(keyfile));
        String keyString = (String) object.get("key");
        return Integer.getInteger(keyString);
    }

    // inner class port
    public class Port implements ICaesarCipher {

        @Override
        public String encrypt(String plainMessage, File keyfile) throws IOException, ParseException {
            return innerEncrypt(plainMessage, keyfile);
        }

        @Override
        public String decrypt(String encryptedMessage, File keyfile) throws IOException, ParseException {
            return innerDecrypt(encryptedMessage, keyfile);
        }
    }
}