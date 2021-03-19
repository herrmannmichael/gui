package event;

public class MessageReceived {
    private String fromParticipant;
    private String toParticipant;
    private String encryptedMessage;
    private String algorithm;
    private String keyfile;

    public MessageReceived(String fromParticipant, String toParticipant, String encryptedMessage, String algorithm, String keyfile){
        this.fromParticipant = fromParticipant;
        this.toParticipant = toParticipant;
        this.encryptedMessage = encryptedMessage;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
    }

    public String toString(){
        return "Event - MessageReceived - fromParticipant [" + fromParticipant + "], toParticipant [" + toParticipant + "], encryptedMessage [" + encryptedMessage + "]";
    }

    public String encryptedMessage() {
        return encryptedMessage;
    }

    public String getFromParticipant() {
        return fromParticipant;
    }

    public String getToParticipant() {
        return toParticipant;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getKeyfile() {
        return keyfile;
    }
}
