import configuration.Configuration;
import factory.RSAFactory;
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
import java.util.ArrayList;
import java.util.List;


public class GUI extends Application {

    private Log log = new Log();
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MSA | Mergentheim/Mosbach Security Agency");


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
            File file = new File(Configuration.instance.fileDirectory+parameterList.get(2));
            parameterList.add("encrypt");
            if(parameterList.get(1).equals("rsa")){
                //cipher = rsa.encrypt(parameterList.get(0),file);
                Object rsa = RSAFactory.build();
                Method encryptMethod = rsa.getClass().getMethod("encrypt", String.class, File.class);
                cipher = (String) encryptMethod.invoke(rsa, parameterList.get(0) ,file);
                parameterList.add(cipher);
            }
            else if (parameterList.get(1).equals("shift")){
                //cipher = shift.encrypt(parameterList.get(0),file);
                parameterList.add(cipher);
            }
            log.newFile(parameterList);
            return cipher;
        }
        else if(input.contains("decrypt message")){
            String cipher = "";
            File file = new File(Configuration.instance.fileDirectory+parameterList.get(2));
            parameterList.add("decrypt");
            if(parameterList.get(1).equals("rsa")){
                //cipher = rsa.encrypt(parameterList.get(0),file);
                parameterList.add(cipher);
            }
            else if (parameterList.get(1).equals("shift")){
                //cipher = shift.encrypt(parameterList.get(0),file);
                parameterList.add(cipher);
            }
            log.newFile(parameterList);
            return cipher;
        }
        else if(input.contains("crack encrypted message")){
            if (input.contains("using shift")){
                //
            }
            else if (input.contains("using rsa")){

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

}