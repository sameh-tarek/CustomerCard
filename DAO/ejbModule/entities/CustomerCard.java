package entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "customer_card")
public class CustomerCard implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "card_number", unique = true, nullable = false, length = 50)
    private String cardNumber;
    
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;
    
    @Column(name = "national_id", unique = true, nullable = false, length = 20)
    private String nationalId;
    
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;
    
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(name = "address", length = 255)
    private String address;
    
    @Column(name = "gender", length = 10)
    private String gender;

    public CustomerCard() {}

    public CustomerCard(String cardNumber, String customerName, String nationalId, String phoneNumber, 
                        String email, String address, String gender) {
        this.cardNumber = cardNumber;
        this.customerName = customerName;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.gender = gender;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }


    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

	@Override
	public String toString() {
		return "CustomerCard [id=" + id + ", cardNumber=" + cardNumber + ", customerName=" + customerName
				+ ", nationalId=" + nationalId + ", phoneNumber=" + phoneNumber + ", email=" + email + ", address="
				+ address + ", gender=" + gender + "]";
	}

   
}
