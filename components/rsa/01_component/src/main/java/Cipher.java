import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Cipher {
    // static instance
    private static final Cipher instance = new Cipher();
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

    public String innerEncrypt(String plainMessage, File publicKeyfile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        byte[] bytes = plainMessage.getBytes(Charset.defaultCharset());
        //byte[] encrypted = crypt(new BigInteger(bytes), readKey(publicKeyfile)).toByteArray();

        Key key = readKey(publicKeyfile);

        for(byte character: bytes){
            stringBuilder.append(crypt(new BigInteger(String.valueOf(character)), key));
            stringBuilder.append(",");
        }

        return stringBuilder.toString();
    }

    public String innerDecrypt(String encryptedMessage, File privateKeyfile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        Key key = readKey(privateKeyfile);
        encryptedMessage = encryptedMessage.replaceAll(",", "");

        stringBuilder.append(crypt(new BigInteger(String.valueOf(encryptedMessage)), key));

        return stringBuilder.toString();
    }

    private Key readKey(File keyfile) throws IOException {
        String content = Files.readString(Path.of(keyfile.getPath()), StandardCharsets.US_ASCII);
        JSONObject object = new JSONObject(content);

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
        public String encrypt(String plainMessage, File publicKeyfile) throws IOException {
            return innerEncrypt(plainMessage, publicKeyfile);
        }

        @Override
        public String decrypt(String encryptedMessage, File privateKeyfile) throws IOException {
            return innerDecrypt(encryptedMessage, privateKeyfile);
        }
    }
}