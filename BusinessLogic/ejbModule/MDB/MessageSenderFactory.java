package MDB;

import enums.MessageType;

public class MessageSenderFactory {
    public static MessageSender getMessageSender(MessageType type) {
        if (type == MessageType.JMS) {
            return new JMSMessageSender();
        }
        throw new IllegalArgumentException("Unsupported message sender type: " + type);
    }
}
