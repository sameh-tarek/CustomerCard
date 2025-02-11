package MDB;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import enums.OperationType;

@Stateless
public class JMSMessageSender implements MessageSender {

    private QueueConnectionFactory qcf;
    private Queue requestQueue;

    private QueueConnection qc;
    private QueueSession qs;
    private QueueSender sender;

    public JMSMessageSender() {
        try {
            InitialContext ctx = new InitialContext();
            qcf = (QueueConnectionFactory) ctx.lookup("jms/QueueConnectionFactory");
            requestQueue = (Queue) ctx.lookup("jms/RequestQueue");
        } catch (NamingException e) {
            throw new RuntimeException("Failed to initialize JMS resources", e);
        }
    }

    @Override
    public void sendRequest(Object request, OperationType operationType, String correlationId) {
        try {
            qc = qcf.createQueueConnection();
            qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            sender = qs.createSender(requestQueue);

            ObjectMessage message = qs.createObjectMessage((Serializable) request);
            message.setStringProperty("operationType", operationType.name());
            message.setJMSCorrelationID(correlationId);

            sender.send(message);
        } catch (JMSException e) {
            System.err.println("Failed to send JMS message: " + e.getMessage());
            throw new RuntimeException("Failed to send JMS message", e);
        }
    }

    @Override
    public void close() {
        try {
            if (sender != null) sender.close();
            if (qs != null) qs.close();
            if (qc != null) qc.close();
        } catch (JMSException e) {
            System.err.println("Error while closing JMS resources: " + e.getMessage());
        }
    }
}
