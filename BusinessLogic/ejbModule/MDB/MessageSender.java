package MDB;

import enums.OperationType;

public interface MessageSender extends AutoCloseable{
    void sendRequest(Object request, OperationType operationType, String correlationId);
    
    @Override
    void close();
}
