import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import event.MessageReceived;
import event.MessageSent;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    private static final List<Channel> channels = new ArrayList<>();
    private final EventBus eventBus;

    public Channel(){
        this.eventBus = new EventBus();
        this.eventBus.register(this);
    }

    @Subscribe
    public void receive(MessageSent messageSent){

    }

    @Subscribe
    public void receive(MessageReceived messageReceived){

    }

    public static List<Channel> getChannels() {
        return channels;
    }

    public static void addChannel(Channel channel){
        channels.add(channel);
    }
}
