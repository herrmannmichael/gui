import configuration.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class Log {
    private boolean enable;
    public Log(){}
    public void  newFile(List<String> strings) throws IOException {
        // List Items
        // 0 message
        // 1 alogrithm
        // 2 filename
        // 3 encrypt/decrypt
        // 4 cipher

        if(enable){
            long unixTime = Instant.now().getEpochSecond();
            File file = new File(Configuration.instance.logDirectory + strings.get(3) + "_" + strings.get(1) + "_" + unixTime + ".txt");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write("starting " + strings.get(3));
            writer.write("plain text: " + strings.get(0)+ "   |   cipher: " + strings.get(4));
            writer.write(strings.get(3) + " finished");
            writer.close();
        }
    }

    public void toggleEnable(){
        enable =! enable;
    }
}
