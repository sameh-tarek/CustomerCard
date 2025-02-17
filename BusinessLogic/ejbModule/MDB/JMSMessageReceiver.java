package MDB;

import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Stateless 
public class JMSMessageReceiver implements MessageReceiver {

	private QueueConnectionFactory qcf;
    private Queue responseQueue;

    private QueueConnection qc;
    private QueueSession qs;
    private QueueReceiver receiver;

    public JMSMessageReceiver() {
        try {
            InitialContext ctx = new InitialContext();
            qcf = (QueueConnectionFactory) ctx.lookup("jms/QueueConnectionFactory");
            responseQueue = (Queue) ctx.lookup("jms/ResponseQueue");
        } catch (NamingException e) {
            throw new RuntimeException("Failed to initialize JMS resources", e);
        }
    }

    @Override
    public Object receiveResponse(String correlationId) {
        try {
            qc = qcf.createQueueConnection();
            qs = qc.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            receiver = qs.createReceiver(responseQueue, "JMSCorrelationID = '" + correlationId + "'");

            qc.start();
            Message message = receiver.receive(5000); // Wait for 5 seconds

            if (message == null) {
                System.err.println("No response received within timeout period for correlationId: " + correlationId);
                return null;
            }

            if (message instanceof ObjectMessage) {
                return ((ObjectMessage) message).getObject();
            }
            return null;
        } catch (JMSException e) {
            throw new RuntimeException("Failed to receive JMS message", e);
        }
    }

    @Override
    public void close() {
        try {
            if (receiver != null) receiver.close();
            if (qs != null) qs.close();
            if (qc != null) qc.close();
        } catch (JMSException e) {
            System.err.println("Error while closing JMS resources: " + e.getMessage());
        }
    }
}