import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Cipher {
    // static instance
    private static Cipher instance = new Cipher();
    // port
    public Port port;

    // private constructor
    private Cipher() {
        port = new Port();
    }

    // static method getInstance
    public static Cipher getInstance() {
        return instance;
    }

    // inner methods

    public String innerEncrypt(String plainMessage, File publicKeyfile) throws IOException, ParseException {
        StringBuilder stringBuilder = new StringBuilder();

        byte[] bytes = plainMessage.getBytes(Charset.defaultCharset());
        byte[] encrypted = crypt(new BigInteger(bytes), readKey(publicKeyfile)).toByteArray();

        for(byte character: encrypted){
            stringBuilder.append(String.valueOf(character));
        }

        return stringBuilder.toString();
    }

    public String innerDecrypt(String encryptedMessage, File privateKeyfile) throws IOException, ParseException {
        StringBuilder stringBuilder = new StringBuilder();

        byte[] bytes = encryptedMessage.getBytes(Charset.defaultCharset());
        byte[] encrypted = crypt(new BigInteger(bytes), readKey(privateKeyfile)).toByteArray();

        for(byte character: encrypted){
            stringBuilder.append(String.valueOf(character));
        }

        return stringBuilder.toString();
    }

    private Key readKey(File keyfile) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(new FileReader(keyfile));

        BigInteger nKey =  new BigInteger((String)object.get("n"));
        BigInteger eKey =  new BigInteger((String)object.get("e"));
        Key key = new Key(nKey, eKey);
        return key;
    }

    private BigInteger crypt(BigInteger message, Key key) {
        return message.modPow(key.getE(), key.getN());
    }

    // inner class port
    public class Port implements ICipher {
        @Override
        public String encrypt(String plainMessage, File publicKeyfile) throws IOException, ParseException {
            return innerEncrypt(plainMessage, publicKeyfile);
        }

        @Override
        public String decrypt(String encryptedMessage, File privateKeyfile) throws IOException, ParseException {
            return innerDecrypt(encryptedMessage, privateKeyfile);
        }
    }
}