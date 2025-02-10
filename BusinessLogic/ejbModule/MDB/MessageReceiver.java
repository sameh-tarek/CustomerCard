package MDB;

public interface MessageReceiver {
    Object receiveResponse(String correlationId);
}
