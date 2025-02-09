package MDB;

public class MessageReceiverFactory {
    public static MessageReceiver getMessageReceiver(String type) {
        if ("JMS".equalsIgnoreCase(type)) {
            return new JMSMessageReceiver();
        }
        
        throw new IllegalArgumentException("Unsupported message receiver type: " + type);
    }
}