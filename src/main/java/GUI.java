import com.jcabi.aspects.Timeable;
import configuration.Configuration;
import factory.RSACrackerFactory;
import factory.RSAFactory;
import factory.ShiftCrackerFactory;
import factory.ShiftFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;


public class GUI extends Application {

    private final Log log = new Log();
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("MSA | Mergentheim/Mosbach Security Agency");
        HSQLDB.instance.setupDatabase();
        System.out.println(HSQLDB.instance.registerParticipant("branch_hkg","normal"));
        System.out.println(HSQLDB.instance.registerParticipant("branch_cpt","normal"));
        System.out.println(HSQLDB.instance.registerParticipant("branch_sfo","normal"));
        System.out.println(HSQLDB.instance.registerParticipant("branch_syd","normal"));
        System.out.println(HSQLDB.instance.registerParticipant("branch_wuh","normal"));
        System.out.println(HSQLDB.instance.registerParticipant("msa","intruder"));
        HSQLDB.instance.shutdown();

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");


        Button executeButton = new Button("Execute");
        executeButton.setPrefSize(100, 20);

        Button closeButton = new Button("Close");
        closeButton.setPrefSize(100, 20);

        TextArea commandLineArea = new TextArea();
        commandLineArea.setWrapText(true);

        TextArea outputArea = new TextArea();
        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        executeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    outputArea.setText(executeCommand(commandLineArea.getText()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                System.out.println("[close] pressed");
                HSQLDB.instance.shutdown();
                System.exit(0);
            }
        });



        hBox.getChildren().addAll(executeButton, closeButton);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(hBox, commandLineArea, outputArea);

        Scene scene = new Scene(vbox, 950, 500);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case F3 -> log.toggleEnable();
                    case F5 -> {
                        try {
                            outputArea.setText(executeCommand(commandLineArea.getText()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    case F8 -> System.out.println("F8");
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String executeCommand(String input) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> parameterList = extractParametersFromBrackets(input);

        // Log List Items
        // 0 message
        // 1 alogrithm
        // 2 filename
        // 3 encrypt/decrypt
        // 4 cipher


        if (input.contains("encrypt message")){
            String cipher = "";

            parameterList.add("encrypt");
            if(parameterList.get(1).equals("rsa")){
                Object rsa = RSAFactory.build();
                cipher = encrypt(rsa, parameterList);
                parameterList.add(cipher);
            }
            else if (parameterList.get(1).equals("shift")){
                //cipher = shift.encrypt(parameterList.get(0),file);
                Object shift = ShiftFactory.build();
                cipher = encrypt(shift, parameterList);
                parameterList.add(cipher);
            }
            log.newFile(parameterList);
            return cipher;
        }
        else if(input.contains("decrypt message")){
            String cipher = "";
            parameterList.add("decrypt");
            if(parameterList.get(1).equals("rsa")){
                //cipher = rsa.encrypt(parameterList.get(0),file);
                Object rsa = RSAFactory.build();
                cipher = decrypt(rsa, parameterList);
                parameterList.add(cipher);
            }
            else if (parameterList.get(1).equals("shift")){
                //cipher = shift.encrypt(parameterList.get(0),file);
                Object shift = ShiftFactory.build();
                cipher = decrypt(shift, parameterList);
                parameterList.add(cipher);
            }
            log.newFile(parameterList);
            return cipher;
        }
        else if(input.contains("crack encrypted message")){
            String ecryptedMessage = "";
            if (input.contains("using shift")){
                Object shiftCracker = ShiftCrackerFactory.build();
                Method encryptMethod = shiftCracker.getClass().getMethod("decrypt", String.class);
                ecryptedMessage = (String) encryptMethod.invoke(shiftCracker, parameterList.get(0));
                return ecryptedMessage;
            }
            else if (input.contains("using rsa")){

                Callable<String> callableEncryptedMessage = new RsaCrackEncrypted(parameterList);
                ExecutorService executorService = Executors.newCachedThreadPool();
                Future<String> callableEncryptedMessageResult = executorService.submit(callableEncryptedMessage);
                try {
                    ecryptedMessage = callableEncryptedMessageResult.get(30, TimeUnit.SECONDS);
                } catch (Exception e){
                    ecryptedMessage = "cracking encrypted message [" + parameterList.get(0) +"] failed";
                }

                return ecryptedMessage;
            }
        }
        else if(input.contains("register participant")){

        }
        else if(input.contains("create channel")){

        }
        else if(input.contains("show channel")){

        }
        else if(input.contains("drop channel")){

        }
        else if(input.contains("intrude channel")){

        }
        else if(input.contains("send message")){

        }
        return "";
    }

    private  List<String> extractParametersFromBrackets(String input){
        if(input.contains("[") && input.contains("]")){
            int indexStart = input.indexOf('[');
            int indexEnd = input.indexOf(']');

            String parameterNonDelimited = input.substring(indexStart + 1, indexEnd);
            List<String> parameterList = new ArrayList<>();
            parameterList.add(parameterNonDelimited);

            String remainingInput = input.substring(indexEnd + 1);

            parameterList.addAll(extractParametersFromBrackets(remainingInput));

            return parameterList;
        }
        return new ArrayList<>();
    }

    private String encrypt(Object strategy, List<String> parameterList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File file = new File(Configuration.instance.keyDirectory+parameterList.get(2));
        System.out.println(file.getAbsolutePath());
        Method encryptMethod = strategy.getClass().getMethod("encrypt", String.class, File.class);
        return (String) encryptMethod.invoke(strategy, parameterList.get(0) ,file);
    }

    private String decrypt(Object strategy, List<String> parameterList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File file = new File(Configuration.instance.keyDirectory+parameterList.get(2));
        System.out.println(file.getAbsolutePath());
        Method encryptMethod = strategy.getClass().getMethod("decrypt", String.class, File.class);
        return (String) encryptMethod.invoke(strategy, parameterList.get(0) ,file);
    }

    private String rsaCrackEncrypted(List<String> parameterList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File file = new File(Configuration.instance.keyDirectory+parameterList.get(1));
        Object rsaCracker = RSACrackerFactory.build();
        Method encryptMethod = rsaCracker.getClass().getMethod("decrypt", String.class, File.class);
        return (String) encryptMethod.invoke(rsaCracker, parameterList.get(0), file);
    }

    class RsaCrackEncrypted implements Callable<String>
    {
        private final List<String> parameterList;

        RsaCrackEncrypted( List<String> parameterList )
        {
            this.parameterList = parameterList;
        }

        @Override public String call() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            return rsaCrackEncrypted(parameterList);
        }
    }
}