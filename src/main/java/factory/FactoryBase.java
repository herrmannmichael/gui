package factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;

public class FactoryBase {
    public static Object build(String componentPath, String component, String archiveName) {
        Object port = null;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("C:\\Program Files\\Java\\jdk-15.0.1\\bin\\jarsigner", "-verify", archiveName);
            Process process = processBuilder.start();
            process.waitFor();

            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            boolean isComponentAccepted = false;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("verified")) {
                    isComponentAccepted = true;
                }
            }

            if (isComponentAccepted) {
                System.out.println("component accepted");

                try {
                    //get jar from componentPath
                    System.out.println(componentPath);
                    URL[] urls = {new File(componentPath).toURI().toURL()};

                    //load clazz
                    URLClassLoader urlClassLoader = new URLClassLoader(urls, FactoryBase.class.getClassLoader());
                    Class clazz = Class.forName(component, true, urlClassLoader);

                    //create clazz instance
                    Object instance = clazz.getMethod("getInstance").invoke(null);
                    port = clazz.getDeclaredField("port").get(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("component rejected");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return port;
    }
}
