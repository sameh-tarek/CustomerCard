package dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import entities.CustomerCard;

@Stateless
public class CustomerCardDAO {

    private static final Logger logger = Logger.getLogger(CustomerCardDAO.class.getName());

    @PersistenceContext(unitName = "DAOEAR_PU")
    private EntityManager em;

    public void create(CustomerCard customerCard) {
        try {
            if (em != null) {
                em.persist(customerCard);
                logger.info("Customer card created successfully: " + customerCard.getCardNumber());
            } else {
                throw new IllegalStateException("EntityManager is null");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error persisting customer card", e);
            throw new RuntimeException("Error persisting customer card", e);
        }
    }

    public CustomerCard findById(Long id) {
        try {
            CustomerCard customerCard = em.find(CustomerCard.class, id);
            logger.info("Customer card retrieved by ID: " + id);
            return customerCard;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding customer by ID", e);
            throw new RuntimeException("Error finding customer by ID", e);
        }
    }

    public List<CustomerCard> findAll() {
        try {
            TypedQuery<CustomerCard> query = em.createQuery("SELECT c FROM CustomerCard c WHERE c.deleted = false",
            		CustomerCard.class);

            List<CustomerCard> customers = query.getResultList();
            logger.info("Fetched all customer cards, count: " + customers.size());
            return customers;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching all customers", e);
            throw new RuntimeException("Error fetching all customers", e);
        }
    }

    public void update(CustomerCard customerCard, String cardNumber) {
        try {
            CustomerCard c = findByCardNumber(cardNumber);
            if (c != null) {
                c.setCardNumber(customerCard.getCardNumber());
                c.setCustomerName(customerCard.getCustomerName());
                em.merge(c);
                logger.info("Customer card updated: " + cardNumber);
            } else {
                logger.warning("Customer card not found for update: " + cardNumber);
                throw new IllegalArgumentException("Customer card not found");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating customer card", e);
            throw new RuntimeException("Error updating customer card", e);
        }
    }

    public void delete(String cardNumber) {
        try {
            CustomerCard customerCard = findByCardNumber(cardNumber);
            if (customerCard != null) {
                em.remove(customerCard);
                logger.info("Customer card deleted: " + cardNumber);
            } else {
                logger.warning("Customer card not found for deletion: " + cardNumber);
                throw new IllegalArgumentException("Customer card not found");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting customer card", e);
            throw new RuntimeException("Error deleting customer card", e);
        }
    }
    
    public void softDelete(String cardNumber) {
        try {
            CustomerCard customer = findByCardNumber(cardNumber);
            if (customer != null) {
                customer.setDeleted(true);
                em.merge(customer);
                logger.info("Soft deleted customer card with card number: " + cardNumber);
            } else {
                logger.warning("Customer card not found for card number: " + cardNumber);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while soft deleting customer card with card number: " + cardNumber, e);
            throw new RuntimeException("Failed to soft delete customer card: " + cardNumber, e);
        }
    }

    public CustomerCard findByCardNumber(String cardNumber) {
        try {
        	TypedQuery<CustomerCard> query = em.createQuery(
                    "SELECT c FROM CustomerCard c WHERE c.cardNumber = :cardNumber AND c.deleted = false",
                    CustomerCard.class);           
        	query.setParameter("cardNumber", cardNumber);
            CustomerCard customerCard = query.getSingleResult();
            logger.info("Customer card found by card number: " + cardNumber);
            return customerCard;
        } catch (javax.persistence.NoResultException e) {
            logger.warning("No customer card found with card number: " + cardNumber);
            return null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding customer by card number", e);
            throw new RuntimeException("Error finding customer by card number", e);
        }
    }
}
