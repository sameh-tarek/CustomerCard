package dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import entities.CustomerCard;

@Stateless
public class CustomerCardDAO {

    @PersistenceContext(unitName = "DAOEAR_PU")
    private EntityManager em;

    public void create(CustomerCard customerCard) {
        try {
            if (em != null) {
                em.persist(customerCard);
            } else {
                throw new IllegalStateException("EntityManager is null");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error persisting customer card", e);
        }
    }

    public CustomerCard findById(Long id) {
        try {
            return em.find(CustomerCard.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding customer by ID", e);
        }
    }

    public List<CustomerCard> findAll() {
        try {
            TypedQuery<CustomerCard> query = em.createQuery("SELECT c FROM CustomerCard c", CustomerCard.class);
            return query.getResultList();
        } catch (Exception e) {
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
            } else {
                throw new IllegalArgumentException("Customer card not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating customer card", e);
        }
    }

    public void delete(String cardNumber) {
        try {
            CustomerCard customerCard = findByCardNumber(cardNumber);
            if (customerCard != null) {
                em.remove(customerCard);
            } else {
                throw new IllegalArgumentException("Customer card not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting customer card", e);
        }
    }

    public CustomerCard findByCardNumber(String cardNumber) {
        try {
            TypedQuery<CustomerCard> query = em.createQuery("SELECT c FROM CustomerCard c WHERE c.cardNumber = :cardNumber", CustomerCard.class);
            query.setParameter("cardNumber", cardNumber);
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error finding customer by card number", e);
        }
    }
}
