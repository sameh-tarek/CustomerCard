package MDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import dao.CustomerCardDAO;
import dtos.CustomerCardRequestDTO;
import dtos.CustomerCardResponseDTO;
import entities.CustomerCard;
import mapper.CustomerCardMapper;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "connectionFactoryJndiName", propertyValue = "jms/QueueConnectionFactory"),
        @ActivationConfigProperty(propertyName = "destinationJndiName", propertyValue = "jms/RequestQueue")
    }
)
public class CustomerCardMDB implements MessageListener {

    @EJB
    private CustomerCardDAO customerCardDAO;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                String operationType = message.getStringProperty("operationType"); 
                CustomerCardRequestDTO request = (CustomerCardRequestDTO) objectMessage.getObject();
                Object response = processRequest(operationType, request);
                sendResponse(response);
            }
        } catch (JMSException e) {
            logError("JMS Exception during message processing", e);
        } catch (Exception e) {
            logError("General exception during message processing", e);
        }
    }

    private Object processRequest(String operationType, CustomerCardRequestDTO request) {
        try {
            switch (operationType) {
                case "create":
                    return createCustomerCard(request);
                case "update":
                    return updateCustomerCard(request);
                case "delete":
                    return deleteCustomerCard(request);
                case "getByCardNumber":
                    return getCustomerByCardNumber(request);
                case "getAll":
                    return getAllCustomers();
                default:
                    return "Unknown operation";
            }
        } catch (Exception e) {
            logError("Exception in processRequest method", e);
            return "Error processing request";
        }
    }

    private String createCustomerCard(CustomerCardRequestDTO request) {
        try {
            CustomerCard customerCard = CustomerCardMapper.toEntity(request);
            customerCardDAO.create(customerCard);
            return "Customer added successfully";
        } catch (Exception e) {
            logError("Error adding customer to database", e);
            return "Error adding customer to database";
        }
    }

    private String updateCustomerCard(CustomerCardRequestDTO request) {
        try {
            CustomerCard customerCard = customerCardDAO.findByCardNumber(request.getCardNumber());
            if (customerCard == null) {
                return "Customer card not found";
            }

            customerCard.setCustomerName(request.getCustomerName());
            customerCardDAO.update(customerCard, request.getCardNumber());
            return "Customer updated successfully";
        } catch (Exception e) {
            logError("Error updating customer in database", e);
            return "Error updating customer in database";
        }
    }

    private String deleteCustomerCard(CustomerCardRequestDTO request) {
        try {
            CustomerCard existingCard = customerCardDAO.findByCardNumber(request.getCardNumber());
            if (existingCard == null) {
                return "Customer card not found";
            }
            customerCardDAO.delete(request.getCardNumber());
            return "SUCCESS";
        } catch (Exception e) {
            logError("Error deleting customer from database", e);
            return "ERROR";
        }
    }

    private CustomerCardResponseDTO getCustomerByCardNumber(CustomerCardRequestDTO request) {
        try {
            CustomerCard customerCard = customerCardDAO.findByCardNumber(request.getCardNumber());
            if (customerCard == null) {
                return null;
            }
            return CustomerCardMapper.toDTO(customerCard);
        } catch (Exception e) {
            logError("Error fetching customer by card number", e);
            return null;
        }
    }

    private List<CustomerCardResponseDTO> getAllCustomers() {
        try {
            List<CustomerCard> customers = customerCardDAO.findAll();
            if (customers == null || customers.isEmpty()) {
                return new ArrayList<>();
            }
            return customers.stream()
                    .map(CustomerCardMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logError("Error fetching all customers", e);
            return new ArrayList<>();
        }
    }

    private void sendResponse(Object response) {
        try {
            InitialContext ctx = new InitialContext();
            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("jms/QueueConnectionFactory");
            Queue responseQueue = (Queue) ctx.lookup("jms/ResponseQueue");
            QueueConnection qc = qcf.createQueueConnection();
            QueueSession qs = qc.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            QueueSender sender = qs.createSender(responseQueue);
            ObjectMessage message = qs.createObjectMessage((Serializable) response);
            sender.send(message);
            sender.close();
            qs.close();
            qc.close();
        } catch (NamingException | JMSException e) {
            logError("Error sending response", e);
        }
    }

    private void logError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }
}
