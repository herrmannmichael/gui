public class Application {
    public static void main(String[] args) {
        CaesarCipher cipher = CaesarCipher.getInstance();
        System.out.println(cipher.innerDecrypt("tdixbo{mfdlfo"));
    }
}
