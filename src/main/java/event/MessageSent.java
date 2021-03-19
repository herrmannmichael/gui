package event;

public class MessageSent {
    private String fromParticipant;
    private String toParticipant;
    private String message;
    private String encryptedMessage;
    private String algorithm;
    private String keyfile;

    public MessageSent(String fromParticipant, String toParticipant, String message, String encryptedMessage, String algorithm, String keyfile){
        this.fromParticipant = fromParticipant;
        this.toParticipant = toParticipant;
        this.message = message;
        this.algorithm = algorithm;
        this.encryptedMessage = encryptedMessage;
        this.keyfile = keyfile;
    }

    public String toString(){
        return "Event - MessageSent - fromParticipant [" + fromParticipant + "], toParticipant [" + toParticipant + "], message [" + message + "]";
    }

    public String getMessage() {
        return message;
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

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public String getKeyfile() {
        return keyfile;
    }
}
