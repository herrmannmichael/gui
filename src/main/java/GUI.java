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



import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                    executeCommand(commandLineArea.getText());
                } catch (IOException e) {
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
                            executeCommand(commandLineArea.getText());
                        } catch (IOException e) {
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

    private void executeCommand(String input) throws IOException {
        List<String> parameterList = extractParametersFromBrackets(input);

        if (input.contains("encrypt message")){
            if(parameterList.get(1).equals("rsa")){
                System.out.println("test");
                parameterList.add("encrypt");
                log.newFile(parameterList);
            }
            else if (parameterList.get(1).equals("shift")){

            }
        }
        else if(input.contains("decrypt message")){
            if(parameterList.get(1).equals("rsa")){

            }
            else if (parameterList.get(1).equals("shift")){

            }
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

    }

    private static List<String> extractParametersFromBrackets(String input){
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