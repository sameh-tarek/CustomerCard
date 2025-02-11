package MDB;

public interface MessageReceiver extends AutoCloseable{
    Object receiveResponse(String correlationId);
    
    @Override
    void close();
}
