import configuration.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
            writer.write("starting " + strings.get(3)  + "\n\r");
            writer.write("plain text: " + strings.get(0)+ "   |   cipher: " + strings.get(4) + "\n\r");
            writer.write(strings.get(3) + " finished");
            writer.close();
        }
    }

    public void toggleEnable(){
        enable =! enable;
    }

    public String getLogFile() throws FileNotFoundException {
        File file = findUsingIOApi(Configuration.instance.logDirectory);
        StringBuilder stringBuilder01 = new StringBuilder();
        String log = "";
        assert file != null;
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            stringBuilder01.append(scanner.nextLine()).append("\n\r");
        }
        scanner.close();
        return stringBuilder01.toString();
    }

    public static File findUsingIOApi(String sdir) {
        File dir = new File(sdir);
        if (dir.isDirectory()) {
            Optional<File> opFile = Arrays.stream(dir.listFiles(File::isFile))
                    .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

            if (opFile.isPresent()){
                return opFile.get();
            }
        }
        return null;
    }
}
