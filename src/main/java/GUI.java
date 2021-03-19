import configuration.Configuration;
import event.MessageReceived;
import event.MessageSent;
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
import java.util.List;
import java.util.concurrent.*;


public class GUI extends Application {

    private TextArea outputArea;
    private final Log log = new Log();
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("MSA | Mergentheim/Mosbach Security Agency");
        HSQLDB.instance.setupDatabase();
        HSQLDB.instance.initChannelsFromDB(this);
        System.out.println(HSQLDB.instance.registerParticipant("branch_hkg","normal"));
        System.out.println(HSQLDB.instance.registerParticipant("branch_cpt","normal"));
        System.out.println(HSQLDB.instance.registerParticipant("branch_sfo","normal"));
        System.out.println(HSQLDB.instance.registerParticipant("branch_syd","normal"));
        System.out.println(HSQLDB.instance.registerParticipant("branch_wuh","normal"));
        System.out.println(HSQLDB.instance.registerParticipant("msa","intruder"));

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

        outputArea = new TextArea();
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

    private String executeCommand(String input) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, SQLException {
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
                Object rsa = RSAFactory.build();
                cipher = decrypt(rsa, parameterList);
                parameterList.add(cipher);
            }
            else if (parameterList.get(1).equals("shift")){
                Object shift = ShiftFactory.build();
                cipher = decrypt(shift, parameterList);
                parameterList.add(cipher);
            }
            log.newFile(parameterList);
            return cipher;
        }
        else if(input.contains("crack encrypted message")){
            String encryptedMessage = "";
            if (input.contains("using shift")){
                return crackShift(parameterList);
            }
            else if (input.contains("using rsa")){
                try{
                    return rsaCrackEncryptedWithin30Seconds(parameterList);

                }
                catch (Exception e){
                    return  "cracking encrypted message [" + parameterList.get(0) +"] failed";
                }
            }
        }
        else if(input.contains("register participant")){
            String message = HSQLDB.instance.registerParticipant(parameterList.get(0),parameterList.get(1));
            System.out.println(message);
            return message;
        }
        else if(input.contains("create channel")){
            return HSQLDB.instance.createChannel(parameterList.get(0), parameterList.get(1), parameterList.get(2), this);
        }
        else if(input.contains("show channel")){
            return HSQLDB.instance.showChannel();
        }
        else if(input.contains("drop channel")){
            String message = HSQLDB.instance.dropChannel(parameterList.get(0));
            System.out.println(message);
            return message;
        }
        else if(input.contains("intrude channel")){
            if(Channel.getChannels().containsKey(parameterList.get(0))){

            }
            else {
                return "channel " + parameterList.get(0) + " does not exist";
            }


        }
        else if(input.contains("send message")){
            if(HSQLDB.instance.doesChannelWithParticipantsExist(parameterList.get(1),parameterList.get(2))){
                List<String> tmpParameterList = new ArrayList<>();
                tmpParameterList.add(parameterList.get(0));
                tmpParameterList.add(parameterList.get(3));
                tmpParameterList.add(parameterList.get(4));

                Object algorithm = null;

                System.out.println(parameterList.get(3));

                if(parameterList.get(3).equals("rsa")){
                    System.out.println("rsa drin");
                    algorithm = RSAFactory.build();
                }
                else if (parameterList.get(3).equals("shift")){
                    algorithm = ShiftFactory.build();
                    System.out.println("shift drin");
                }

                String encryptedMessage = encrypt(algorithm,tmpParameterList);
                String channelName = HSQLDB.instance.getChannelName(parameterList.get(1),parameterList.get(2));
                Channel channel = Channel.getChannels().get(channelName);
                MessageSent messageSent = new MessageSent(parameterList.get(1), parameterList.get(2), parameterList.get(0), encryptedMessage, parameterList.get(3), parameterList.get(4));
                channel.receive(messageSent);

                MessageReceived messageReceived = new MessageReceived(messageSent.getFromParticipant(), messageSent.getToParticipant(), messageSent.getEncryptedMessage(), messageSent.getAlgorithm(), messageSent.getKeyfile());
                channel.receive(messageReceived);

                return parameterList.get(2) + " received new message";
            }
            else {
                return "no valid channel from ["+parameterList.get(1)+"] to ["+parameterList.get(2)+"]";
            }
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

    public static String encrypt(Object strategy, List<String> parameterList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File file = new File(Configuration.instance.keyDirectory+parameterList.get(2));
        System.out.println(file.getAbsolutePath());
        Method encryptMethod = strategy.getClass().getMethod("encrypt", String.class, File.class);
        return (String) encryptMethod.invoke(strategy, parameterList.get(0) ,file);
    }

    public static String decrypt(Object strategy, List<String> parameterList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File file = new File(Configuration.instance.keyDirectory+parameterList.get(2));
        System.out.println(file.getAbsolutePath());
        Method encryptMethod = strategy.getClass().getMethod("decrypt", String.class, File.class);
        return (String) encryptMethod.invoke(strategy, parameterList.get(0) ,file);
    }

    public String crackShift(List<String> parameterList) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String encryptedMessage;
        Object shiftCracker = ShiftCrackerFactory.build();
        Method encryptMethod = shiftCracker.getClass().getMethod("decrypt", String.class);
        encryptedMessage = (String) encryptMethod.invoke(shiftCracker, parameterList.get(0));
        return encryptedMessage;
    }

    private String rsaCrackEncrypted(List<String> parameterList) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File file = new File(Configuration.instance.keyDirectory+parameterList.get(1));
        Object rsaCracker = RSACrackerFactory.build();
        Method encryptMethod = rsaCracker.getClass().getMethod("decrypt", String.class, File.class);
        return (String) encryptMethod.invoke(rsaCracker, parameterList.get(0), file);
    }

    public String rsaCrackEncryptedWithin30Seconds(List<String> parameterList) throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callableEncryptedMessage = new RsaCrackEncrypted(parameterList);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> callableEncryptedMessageResult = executorService.submit(callableEncryptedMessage);

        return callableEncryptedMessageResult.get(30, TimeUnit.SECONDS);
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

    public TextArea getOutputArea() {
        return outputArea;
    }
}