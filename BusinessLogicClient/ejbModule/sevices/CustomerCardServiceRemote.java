package sevices;

import java.util.List;

import javax.ejb.Remote;

import dtos.CustomerCardRequestDTO;
import dtos.CustomerCardResponseDTO;
 
@Remote
public interface CustomerCardServiceRemote {
	public List<CustomerCardResponseDTO> getAllCustomersCards();
	public CustomerCardResponseDTO getCustomerByCardNumber(String cardNumber);
	public String addNewCustomerCard(CustomerCardRequestDTO request);
	public String updateCustomerCard(CustomerCardRequestDTO request, String cardNumber);
	public String deleteCustomerCard(String cardNumber);
}
