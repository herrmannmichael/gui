public enum Configuration {
    instance;

    public String userDirectory = System.getProperty("user.dir");
    public String fileSeparator = System.getProperty("file.separator");

    public String logDirectory = userDirectory + fileSeparator + "log" + fileSeparator;

    public String inputDataFile = logDirectory + fileSeparator + "input.txt";
    public String encryptedDataFile = logDirectory + fileSeparator + "encrypted.txt";
}