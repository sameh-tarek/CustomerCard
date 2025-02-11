package MDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import enums.OperationType;
import mapper.CustomerCardMapper;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "connectionFactoryJndiName", propertyValue = "jms/QueueConnectionFactory"),
        @ActivationConfigProperty(propertyName = "destinationJndiName", propertyValue = "jms/RequestQueue")
    }
)
public class CustomerCardMDB implements MessageListener {
    
    private static final Logger logger = Logger.getLogger(CustomerCardMDB.class.getName());

    @EJB
    private CustomerCardDAO customerCardDAO;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                OperationType operationType = OperationType.valueOf(message.getStringProperty("operationType")); 
                String correlationId = message.getJMSCorrelationID();
                CustomerCardRequestDTO request = (CustomerCardRequestDTO) objectMessage.getObject();
                logger.info("Received message with operation: " + operationType);
                Object response = processRequest(operationType, request);
                sendResponse(response, correlationId);
            }
        } catch (JMSException e) {
            logError("JMS Exception during message processing", e);
        } catch (Exception e) {
            logError("General exception during message processing", e);
        }
    }

    private Object processRequest(OperationType operationType, CustomerCardRequestDTO request) {
        try {
            switch (operationType) {
                case CREATE:
                    return createCustomerCard(request);
                case UPDATE:
                    return updateCustomerCard(request);
                case DELETE:
                    return deleteCustomerCard(request);
                case GET_BY_CARD_NUMBER:
                    return getCustomerByCardNumber(request);
                case GET_ALL:
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
            logger.info("Attempting to create a new customer card...");
            CustomerCard existingCustomer = customerCardDAO.findByCardNumber(request.getCardNumber());
            if (existingCustomer != null) {
                logger.warning("Customer with card number already exists: " + request.getCardNumber());
                return "Customer already exists!";
            }
            
            CustomerCard customerCard = CustomerCardMapper.toEntity(request);
            customerCardDAO.create(customerCard);
            logger.info("Customer added successfully: " + request.getCardNumber());
            return "Customer added successfully";
        } catch (Exception e) {
            logError("Error adding customer to database", e);
            return "Error adding customer to database";
        }
    }

    private String updateCustomerCard(CustomerCardRequestDTO request) {
        try {
            logger.info("Attempting to update customer card: " + request.getCardNumber());
            CustomerCard customerCard = customerCardDAO.findByCardNumber(request.getCardNumber());
            if (customerCard == null) {
                logger.warning("Customer card not found: " + request.getCardNumber());
                return "Customer card not found";
            }

            customerCard.setCustomerName(request.getCustomerName());
            customerCardDAO.update(customerCard, request.getCardNumber());
            logger.info("Customer updated successfully: " + request.getCardNumber());
            return "Customer updated successfully";
        } catch (Exception e) {
            logError("Error updating customer in database", e);
            return "Error updating customer in database";
        }
    }

    private String deleteCustomerCard(CustomerCardRequestDTO request) {
        try {
            logger.info("Soft deleting customer card: " + request.getCardNumber());
            CustomerCard existingCard = customerCardDAO.findByCardNumber(request.getCardNumber());
            if (existingCard == null) {
                return "Customer card not found";
            }
            customerCardDAO.softDelete(request.getCardNumber());
            return "Customer card soft deleted successfully";
        } catch (Exception e) {
            logError("Error performing soft delete", e);
            return "ERROR";
        }
    }

    
    private CustomerCardResponseDTO getCustomerByCardNumber(CustomerCardRequestDTO request) {
        try {
            logger.info("Fetching customer by card number: " + request.getCardNumber());
            CustomerCard customerCard = customerCardDAO.findByCardNumber(request.getCardNumber());
            if (customerCard == null) {
                logger.warning("Customer card not found for number: " + request.getCardNumber());
                return null;
            }
            return CustomerCardMapper.toDTO(customerCard);
        } catch (Exception e) {
            logError("Error fetching customer by card number", e);
            return null;
        }
    }


    private List<CustomerCardResponseDTO> getAllCustomers() {
        logger.info("Fetching all customer cards...");
        try {
            List<CustomerCard> customers = customerCardDAO.findAll();
            if (customers == null || customers.isEmpty()) {
                logger.warning("No customer records found.");
                return new ArrayList<>();
            }
            logger.info("Successfully fetched " + customers.size() + " customer records.");
            return customers.stream()
                    .map(CustomerCardMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logError("Error fetching all customers", e);
            return new ArrayList<>();
        }
    }

    private void sendResponse(Object response, String correlationId) {
        try {
            InitialContext ctx = new InitialContext();
            QueueConnectionFactory qcf = (QueueConnectionFactory) ctx.lookup("jms/QueueConnectionFactory");
            Queue responseQueue = (Queue) ctx.lookup("jms/ResponseQueue");
            QueueConnection qc = qcf.createQueueConnection();
            QueueSession qs = qc.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            QueueSender sender = qs.createSender(responseQueue);
            ObjectMessage message = qs.createObjectMessage((Serializable) response);
            message.setJMSCorrelationID(correlationId);
            sender.send(message);
            sender.close();
            qs.close();
            qc.close();
            logger.info("Response sent successfully with correlation ID: " + correlationId);
        } catch (NamingException | JMSException e) {
            logError("Error sending response", e);
        }
    }

    private void logError(String message, Exception e) {
        logger.log(Level.SEVERE, message, e);
    }
}
