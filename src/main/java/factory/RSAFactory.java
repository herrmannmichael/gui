package factory;

import configuration.Configuration;

public class RSAFactory extends FactoryBase{
    public static Object build() {
        return build(Configuration.instance.pathToRsaJavaArchive, "Cipher");
    }
}
