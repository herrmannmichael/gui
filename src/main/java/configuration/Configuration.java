package configuration;

public enum Configuration {
    instance;

    public String userDirectory = System.getProperty("user.dir");
    public String fileSeparator = System.getProperty("file.separator");

    public String logDirectory = userDirectory + fileSeparator + "log" + fileSeparator;

    public String keyDirectory = userDirectory + fileSeparator + "key" + fileSeparator;

    public String fileDirectory = logDirectory + fileSeparator;

    public String keyFile = logDirectory + fileSeparator + "keyfile.json";
    public String publicKeyFile = logDirectory + fileSeparator + "publicKeyfile.json";
    public String privateKeyFile = logDirectory + fileSeparator + "privateKeyfile.json";

    public String commonPathToJavaArchive = userDirectory + fileSeparator + "components" + fileSeparator;
    public String componentFolderName = "01_component";

    public String inputDataFile = logDirectory + fileSeparator + "input.txt";
    public String encryptedDataFile = logDirectory + fileSeparator + "encrypted.txt";

    public String rsaArchiveName =  "rsa.jar";
    public String pathToRsaJavaArchive = commonPathToJavaArchive + "rsa" + fileSeparator + componentFolderName + fileSeparator + "build" + fileSeparator + "libs" + fileSeparator + rsaArchiveName;
    public String rsaCrackerArchiveName = "rsa_cracker.jar";
    public String pathToRsaCrackerJavaArchive = commonPathToJavaArchive + "rsa_cracker" + fileSeparator + componentFolderName + fileSeparator + "build" + fileSeparator + "libs" + fileSeparator + rsaCrackerArchiveName;
    public String shiftArchiveName = "shift.jar";
    public String pathToShiftJavaArchive = commonPathToJavaArchive + "shift" + fileSeparator + componentFolderName + fileSeparator + "build" + fileSeparator + "libs" + fileSeparator + shiftArchiveName;
    public String shiftCrackerArchiveName = "shift_cracker.jar";
    public String pathToShiftCrackerJavaArchive = commonPathToJavaArchive + "shift_cracker" + fileSeparator + componentFolderName + fileSeparator + "build" + fileSeparator + "libs" + fileSeparator + shiftCrackerArchiveName;
}