package MDB;

import enums.OperationType;

public interface MessageSender {
    void sendRequest(Object request, OperationType operationType, String correlationId);
}
