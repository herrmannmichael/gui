import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

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
    public String innerDecrypt(String  encryptedMessage, File publicKeyFile) throws IOException, RSACrackerException {
        StringBuilder stringBuilder = new StringBuilder();

        Key key = readKey(publicKeyFile);

        BigInteger plainmessage = execute(key, new BigInteger(encryptedMessage));

        stringBuilder.append(plainmessage);

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

    public BigInteger execute(Key key, BigInteger cipher) throws RSACrackerException {
        BigInteger p, q, d;
        List<BigInteger> factorList = factorize(key.getN());

        if (factorList.size() != 2) {
            throw new RSACrackerException("cannot determine factors p and q");
        }

        p = factorList.get(0);
        q = factorList.get(1);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        d = key.getE().modInverse(phi);
        return cipher.modPow(d, key.getN());
    }

    public List<BigInteger> factorize(BigInteger n) {
        BigInteger two = BigInteger.valueOf(2);
        List<BigInteger> factorList = new LinkedList<>();

        if (n.compareTo(two) < 0) {
            throw new IllegalArgumentException("must be greater than one");
        }

        while (n.mod(two).equals(BigInteger.ZERO)) {
            factorList.add(two);
            n = n.divide(two);
        }

        if (n.compareTo(BigInteger.ONE) > 0) {
            BigInteger factor = BigInteger.valueOf(3);
            while (factor.multiply(factor).compareTo(n) <= 0) {
                if (n.mod(factor).equals(BigInteger.ZERO)) {
                    factorList.add(factor);
                    n = n.divide(factor);
                } else {
                    factor = factor.add(two);
                }
            }
            factorList.add(n);
        }

        return factorList;
    }

    // inner class port
    public class Port implements ICipher {
        @Override
        public String decrypt(String  encryptedMessage, File publicKeyFile) throws IOException, RSACrackerException {
            return innerDecrypt(encryptedMessage, publicKeyFile);
        }
    }
}