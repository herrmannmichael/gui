package factory;

import configuration.Configuration;

public class ShiftFactory extends FactoryBase{
    public static Object build() {
        return build(Configuration.instance.pathToShiftJavaArchive, "CaesarCipher");
    }
}