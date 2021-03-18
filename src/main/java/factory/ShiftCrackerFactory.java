package factory;

import configuration.Configuration;

public class ShiftCrackerFactory extends FactoryBase{
    public static Object build() {
        return build(Configuration.instance.pathToShiftCrackerJavaArchive, "CaesarCipher", Configuration.instance.shiftCrackerArchiveName);
    }
}