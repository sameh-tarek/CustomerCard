package services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import enums.MessageType;
import enums.OperationType;
import MDB.MessageReceiver;
import MDB.MessageSender;
import MDB.MessageReceiverFactory;
import MDB.MessageSenderFactory;
import dtos.CustomerCardRequestDTO;
import dtos.CustomerCardResponseDTO;
import exceptions.CustomerCardException;
import exceptions.CardNotFoundException;
import sevices.CustomerCardServiceRemote;
import validators.CustomerCardValidator;

@Stateless
public class CustomerCardServiceBean implements CustomerCardServiceRemote {
    
    private static final Logger logger = Logger.getLogger(CustomerCardServiceBean.class.getName());

    private final MessageSender messageSender;
    private final MessageReceiver messageReceiver;

    public CustomerCardServiceBean() {
        MessageType messageType = MessageType.JMS; 
        this.messageSender = MessageSenderFactory.getMessageSender(messageType);
        this.messageReceiver = MessageReceiverFactory.getMessageReceiver(messageType);
        logger.info("CustomerCardServiceBean initialized with messageType: " + messageType);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CustomerCardResponseDTO> getAllCustomersCards() {
        String correlationId = UUID.randomUUID().toString();
        logger.info("Fetching all customer cards | correlationId: " + correlationId);

        try {
            messageSender.sendRequest(new CustomerCardRequestDTO(), OperationType.GET_ALL, correlationId);
            List<CustomerCardResponseDTO> response = (List<CustomerCardResponseDTO>) messageReceiver.receiveResponse(correlationId);

            int size = (response != null) ? response.size() : 0;
            logger.info("Fetched " + size + " customer cards.");
            return (response != null) ? response : new ArrayList<>();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving customer cards | correlationId: " + correlationId, e);
            throw new CustomerCardException("Error retrieving customer cards", e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public CustomerCardResponseDTO getCustomerByCardNumber(String cardNumber) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("Fetching customer card | cardNumber: " + cardNumber + " | correlationId: " + correlationId);

        try {
            CustomerCardValidator.validateCardNumber(cardNumber);
            CustomerCardRequestDTO request = new CustomerCardRequestDTO();
            request.setCardNumber(cardNumber);

            messageSender.sendRequest(request, OperationType.GET_BY_CARD_NUMBER, correlationId);
            CustomerCardResponseDTO response = (CustomerCardResponseDTO) messageReceiver.receiveResponse(correlationId);

            if (response == null) {
                logger.warning("Customer card not found | cardNumber: " + cardNumber);
                throw new CardNotFoundException("Customer card not found");
            }

            logger.info("Successfully fetched customer card | cardNumber: " + cardNumber);
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching customer card | cardNumber: " + cardNumber, e);
            throw new CustomerCardException("Error fetching customer card", e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public String addNewCustomerCard(CustomerCardRequestDTO request) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("Adding new customer card | correlationId: " + correlationId);

        try {
            CustomerCardValidator.validateCustomerRequest(request);
            messageSender.sendRequest(request, OperationType.CREATE, correlationId);
            String response = (String) messageReceiver.receiveResponse(correlationId);

            logger.info("Successfully added new customer card.");
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding new customer card | correlationId: " + correlationId, e);
            throw new CustomerCardException("Error adding new customer card", e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public String updateCustomerCard(CustomerCardRequestDTO request, String cardNumber) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("Updating customer card | cardNumber: " + cardNumber + " | correlationId: " + correlationId);

        try {
            CustomerCardValidator.validateCardNumber(cardNumber);
            CustomerCardValidator.validateCustomerRequest(request);

            messageSender.sendRequest(request, OperationType.UPDATE, correlationId);
            String response = (String) messageReceiver.receiveResponse(correlationId);

            logger.info("Successfully updated customer card | cardNumber: " + cardNumber);
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating customer card | cardNumber: " + cardNumber, e);
            throw new CustomerCardException("Error updating customer card", e);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public String deleteCustomerCard(String cardNumber) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("Deleting customer card | cardNumber: " + cardNumber + " | correlationId: " + correlationId);

        try {
            CustomerCardValidator.validateCardNumber(cardNumber);
            CustomerCardRequestDTO request = new CustomerCardRequestDTO();
            request.setCardNumber(cardNumber);

            messageSender.sendRequest(request, OperationType.DELETE, correlationId);
            String response = (String) messageReceiver.receiveResponse(correlationId);

            logger.info("Successfully deleted customer card | cardNumber: " + cardNumber);
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting customer card | cardNumber: " + cardNumber, e);
            throw new CustomerCardException("Error deleting customer card", e);
        }
    }
}
