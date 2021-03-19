import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import event.MessageReceived;
import event.MessageSent;

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

    }

    @Subscribe
    public void receive(MessageReceived messageReceived){

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
