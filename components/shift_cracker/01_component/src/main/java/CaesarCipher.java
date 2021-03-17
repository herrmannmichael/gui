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

    public String innerDecrypt(String encryptedMessage) {
        StringBuilder stringBuilder = new StringBuilder();

        if (encryptedMessage.equals("")) {
            System.exit(0);
        }

        char[] sourceText = new char[encryptedMessage.length()];
        int[] unicode = new int[encryptedMessage.length()];
        int[] unicodeCopy = new int[encryptedMessage.length()];

        for (int count = 0; count < encryptedMessage.length(); count++) {
            sourceText[count] = encryptedMessage.charAt(count);
        }

        String hex;
        int dec;

        for (int count = 0; count < sourceText.length; count++) {
            hex = Integer.toHexString(sourceText[count]);
            dec = Integer.parseInt(hex, 16);
            unicode[count] = dec;
            unicodeCopy[count] = dec;
        }

        for (int shift = 1; shift <= 25; shift++) {
            stringBuilder.append(smartShift(shift, unicode, unicodeCopy));
            stringBuilder.append(",");
        }

        return stringBuilder.toString();
    }

    private static String smartShift(int shift, int[] unicode, int[] unicodeCopy) {
        for (int x = 0; x <= unicode.length - 1; x++) {
            unicodeCopy[x] = unicode[x];

            if (unicode[x] >= 65 && unicode[x] <= 90) {
                unicodeCopy[x] += shift;
                if (unicodeCopy[x] > 90) {
                    unicodeCopy[x] -= 26;
                }
            }
        }

        String[] processed = new String[unicode.length];
        char[] finalProcess = new char[unicode.length];

        for (int count = 0; count < processed.length; count++) {
            processed[count] = Integer.toHexString(unicodeCopy[count]);
            int hexToInt = Integer.parseInt(processed[count], 16);
            char intToChar = (char) hexToInt;
            finalProcess[count] = intToChar;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (char character : finalProcess) {
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    // inner class port
    public class Port implements ICaesarCipher {
        @Override
        public String decrypt(String encryptedMessage) {
            return innerDecrypt(encryptedMessage);
        }
    }
}