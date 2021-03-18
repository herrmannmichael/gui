package configuration;

public enum Configuration {
    instance;

    public String userDirectory = System.getProperty("user.dir");
    public String fileSeparator = System.getProperty("file.separator");

    public String logDirectory = userDirectory + fileSeparator + "log" + fileSeparator;

    public String keyDirectory = userDirectory + fileSeparator + "key" + fileSeparator;

    public String keyFile = logDirectory + fileSeparator + "keyfile.json";
    public String publicKeyFile = logDirectory + fileSeparator + "publicKeyfile.json";
    public String privateKeyFile = logDirectory + fileSeparator + "privateKeyfile.json";

}