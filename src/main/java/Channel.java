import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import event.MessageReceived;
import event.MessageSent;
import factory.RSAFactory;
import factory.ShiftFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Channel {
    private static final HashMap<String, Channel> channels = new HashMap<>();
    private final EventBus eventBus;
    private String name;
    private List<String> intruderList;
    private GUI gui;

    public Channel(String name, GUI gui){
        this.eventBus = new EventBus();
        this.eventBus.register(this);
        this.name = name;
        this.intruderList = new ArrayList<>();
        this.gui = gui;
    }

    @Subscribe
    public void receive(MessageSent messageSent){
        //HSQLDB.instance.saveMessage();
    }

    @Subscribe
    public void receive(MessageReceived messageReceived) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException {

        List<String> tmpParameterList = new ArrayList<>();
        tmpParameterList.add(messageReceived.encryptedMessage());
        tmpParameterList.add(messageReceived.getAlgorithm());

        Object algorithm = null;

        switch (messageReceived.getAlgorithm()) {
            case "rsa" -> {
                algorithm = RSAFactory.build();
                tmpParameterList.add(messageReceived.getKeyfile().replace("public", "private"));
            }
            case "shift" -> {
                tmpParameterList.add(messageReceived.getKeyfile());
                algorithm = ShiftFactory.build();
            }
        }

        String decryptedMessage = GUI.decrypt(algorithm, tmpParameterList);
        HSQLDB.instance.logPostbox(messageReceived.getFromParticipant(), messageReceived.getToParticipant(), decryptedMessage);

        for (String intruder: intruderList){
            System.out.println("intruder is listening");
            String decryptedMessageByIntruder = "unknown";
            HSQLDB.instance.logPostbox(messageReceived.getFromParticipant(), intruder, decryptedMessageByIntruder);
            tmpParameterList = new ArrayList<>();
            tmpParameterList.add(messageReceived.encryptedMessage());
            System.out.println("key file" + messageReceived.getKeyfile());
            tmpParameterList.add(messageReceived.getKeyfile());
            try{
                switch (messageReceived.getAlgorithm()){
                    case "rsa" -> decryptedMessageByIntruder = gui.rsaCrackEncryptedWithin30Seconds(tmpParameterList);
                    case "shift" -> decryptedMessageByIntruder = gui.crackShift(tmpParameterList);
                }

                HSQLDB.instance.updateIntruderPostbox(decryptedMessageByIntruder);

                gui.getOutputArea().setText("intruder ["+ intruder +"] cracked message from participant " +
                        "["+messageReceived.getFromParticipant()+"] | [" + decryptedMessageByIntruder + "]\n" + gui.getOutputArea().getText());
            }
            catch (Exception e){
                e.printStackTrace();
                gui.getOutputArea().setText("intruder [" + intruder + "] | crack message from participant " +
                        "["+ messageReceived.getFromParticipant() +"] failed \n" + gui.getOutputArea().getText());
            }
        }
    }

    public static HashMap<String, Channel> getChannels() {
        return channels;
    }

    public String getName() {
        return name;
    }

    public static void addChannel(String name, Channel channel){
        channels.put(name, channel);
    }

    public static void removeChannel(String name){
        channels.remove(name);
    }

    public void intrude(String intruderName){
        this.intruderList.add(intruderName);
    }
}
