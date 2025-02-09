package mapper;


import dtos.CustomerCardRequestDTO;
import dtos.CustomerCardResponseDTO;
import entities.CustomerCard;

public class CustomerCardMapper {

    
    public static CustomerCard toEntity(CustomerCardRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        CustomerCard customerCard = new CustomerCard();
        customerCard.setCardNumber(dto.getCardNumber());
        customerCard.setCustomerName(dto.getCustomerName());
        customerCard.setNationalId(dto.getNationalId());
        customerCard.setPhoneNumber(dto.getPhoneNumber());
        customerCard.setEmail(dto.getEmail());
        customerCard.setAddress(dto.getAddress());
        customerCard.setGender(dto.getGender());
        return customerCard;
    }

 
    public static CustomerCardResponseDTO toDTO(CustomerCard entity) {
        if (entity == null) {
            return null;
        }
        CustomerCardResponseDTO dto = new CustomerCardResponseDTO();
        dto.setId(entity.getId());
        dto.setCardNumber(entity.getCardNumber());
        dto.setCustomerName(entity.getCustomerName());
        dto.setNationalId(entity.getNationalId());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setGender(entity.getGender());
        return dto;
    }
}
