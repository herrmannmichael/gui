import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

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
    public String innerDecrypt(String encryptedMessage, File privateKeyfile) throws IOException, ParseException, RSACrackerException {
        StringBuilder stringBuilder = new StringBuilder();

        BigInteger plainmessage = execute(readKey(privateKeyfile), new BigInteger(encryptedMessage));

        stringBuilder.append(plainmessage);

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
        public String decrypt(String encryptedMessage, File privateKeyfile) throws IOException, ParseException, RSACrackerException {
            return innerDecrypt(encryptedMessage, privateKeyfile);
        }
    }
}