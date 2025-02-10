package MDB;

import enums.MessageType;

public class MessageReceiverFactory {
    public static MessageReceiver getMessageReceiver(MessageType type) {
        if (type == MessageType.JMS) {
            return new JMSMessageReceiver();
        }
        throw new IllegalArgumentException("Unsupported message receiver type: " + type);
    }
}
