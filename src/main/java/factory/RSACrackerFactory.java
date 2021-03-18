package factory;

import configuration.Configuration;

public class RSACrackerFactory  extends FactoryBase{
    public static Object build() {
        return build(Configuration.instance.pathToRsaCrackerJavaArchive, "Cipher", Configuration.instance.rsaCrackerArchiveName);
    }
}