import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import event.MessageReceived;
import event.MessageSent;
import factory.RSAFactory;
import factory.ShiftFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Channel {
    private static final HashMap<String, Channel> channels = new HashMap<>();
    private final EventBus eventBus;
    private String name;

    public Channel(String name){
        this.eventBus = new EventBus();
        this.eventBus.register(this);
        this.name = name;
    }

    @Subscribe
    public void receive(MessageSent messageSent){
        //HSQLDB.instance.saveMessage();
    }

    @Subscribe
    public void receive(MessageReceived messageReceived) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        List<String> tmpParameterList = new ArrayList<>();
        tmpParameterList.add(messageReceived.encryptedMessage());
        tmpParameterList.add(messageReceived.getAlgorithm());

        Object algorithm = null;

        switch (messageReceived.getAlgorithm()) {
            case "rsa" -> {
                algorithm = new RSAFactory();
                tmpParameterList.add(messageReceived.getKeyfile().replace("public", "private"));
            }
            case "shift" -> {
                tmpParameterList.add(messageReceived.getKeyfile());
                algorithm = new ShiftFactory();
            }
        }

        String decryptedMessage = GUI.decrypt(algorithm, tmpParameterList);
        //HSQLDB.instance.logPostbox();

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
}
