package MDB;

public class MessageSenderFactory {
    public static MessageSender getMessageSender(String type) {
        if ("JMS".equalsIgnoreCase(type)) {
            return new JMSMessageSender();
        }
        throw new IllegalArgumentException("Unsupported message sender type: " + type);
    }
}