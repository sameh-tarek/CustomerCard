package sevices;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import MDB.MessageReceiver;
import MDB.MessageSender;
import MDB.MessageReceiverFactory;
import MDB.MessageSenderFactory;
import dtos.CustomerCardRequestDTO;
import dtos.CustomerCardResponseDTO;
import exceptions.CustomerCardException;

@Stateless
public class CustomerCardServiceBean implements CustomerCardServiceRemote {

    private MessageSender messageSender;
    private MessageReceiver messageReceiver;

    public CustomerCardServiceBean() {
        String messageType = "JMS"; // Can be switched to "Kafka", "RabbitMQ", etc.
        this.messageSender = MessageSenderFactory.getMessageSender(messageType);
        this.messageReceiver = MessageReceiverFactory.getMessageReceiver(messageType);
    }

    @Override
    public List<CustomerCardResponseDTO> getAllCustomersCards() {
        try {
            CustomerCardRequestDTO request = new CustomerCardRequestDTO();
            messageSender.sendRequest(request, "getAll");

            Object response = messageReceiver.receiveResponse();

            if (response instanceof List<?>) {
                List<?> responseList = (List<?>) response;
                List<CustomerCardResponseDTO> customerList = new ArrayList<>();

                for (Object obj : responseList) {
                    if (obj instanceof CustomerCardResponseDTO) {
                        customerList.add((CustomerCardResponseDTO) obj);
                    }
                }
                return customerList;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error while retrieving all customer cards", e);
        }
    }

    @Override
    public CustomerCardResponseDTO getCustomerByCardNumber(String cardNumber) {
        try {
        	 if (cardNumber == null || cardNumber.isEmpty()) {
                 throw new CustomerCardException("Card number cannot be null or empty");
             }
            CustomerCardRequestDTO request = new CustomerCardRequestDTO();
            request.setCardNumber(cardNumber);
            messageSender.sendRequest(request, "getByCardNumber");

            return (CustomerCardResponseDTO) messageReceiver.receiveResponse();
        } catch (Exception e) {
            throw new RuntimeException("Error while retrieving customer card by card number", e);
        }
    }

    @Override
    public String addNewCustomerCard(CustomerCardRequestDTO request) {
        try {
            messageSender.sendRequest(request, "create");
            return (String) messageReceiver.receiveResponse();
        } catch (Exception e) {
            throw new RuntimeException("Error while adding new customer card", e);
        }
    }

    @Override
    public String updateCustomerCard(CustomerCardRequestDTO request, String cardNumber) {
        try {
            messageSender.sendRequest(request, "update");
            return (String) messageReceiver.receiveResponse();
        } catch (Exception e) {
            throw new RuntimeException("Error while updating customer card", e);
        }
    }

    @Override
    public String deleteCustomerCard(String cardNumber) {
        try {
            CustomerCardRequestDTO request = new CustomerCardRequestDTO();
            request.setCardNumber(cardNumber);
            messageSender.sendRequest(request, "delete");
            return (String) messageReceiver.receiveResponse();
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting customer card", e);
        }
    }
}
