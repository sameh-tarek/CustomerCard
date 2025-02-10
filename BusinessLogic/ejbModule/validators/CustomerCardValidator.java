package validators;

import dtos.CustomerCardRequestDTO;
import exceptions.InvalidInputException;

public class CustomerCardValidator {

    public static void validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new InvalidInputException("Card number cannot be null or empty");
        }
    }

    public static void validateCustomerRequest(CustomerCardRequestDTO request) {
        if (request == null) {
            throw new InvalidInputException("Customer request cannot be null");
        }
        if (request.getCardNumber() == null || request.getCardNumber().trim().isEmpty()) {
            throw new InvalidInputException("Card number is required");
        }
        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name is required");
        }
    }
}
