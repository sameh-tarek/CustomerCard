package dtos;

import java.io.Serializable;

public class CustomerCardRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cardNumber;
    private String customerName;
    private String nationalId;
    private String phoneNumber;
    private String email;
    private String address;
    private String gender;

    public CustomerCardRequestDTO() {
    }

    public CustomerCardRequestDTO(String cardNumber, String customerName, String nationalId, String phoneNumber, 
                                  String email, String address, String gender) {
        this.cardNumber = cardNumber;
        this.customerName = customerName;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.gender = gender;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
