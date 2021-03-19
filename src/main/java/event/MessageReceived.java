package event;

public class MessageReceived {
    private String fromParticipant;
    private String toParticipant;
    private String message;
    private String algorithm;

    public MessageReceived(String fromParticipant, String toParticipant, String message, String algorithm){
        this.fromParticipant = fromParticipant;
        this.toParticipant = toParticipant;
        this.message = message;
        this.algorithm = algorithm;
    }

    public String toString(){
        return "Event - MessageReceived - fromParticipant [" + fromParticipant + "], toParticipant [" + toParticipant + "], message [" + message + "]";
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
}
