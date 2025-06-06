package yang.bao.yang_bank.repository;

import yang.bao.yang_bank.entity.Account;
import yang.bao.yang_bank.entity.Custumer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AccountRepository {
    private final EntityManager entityManager;

    public AccountRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Account account) {
        entityManager.getTransaction().begin();
        if (account.getBalance() == null) {
            account.setBalance(0.0);
        }
        if (account.getCreationDate() == null) {
            account.setCreationDate(java.time.Instant.now());
        }
        entityManager.persist(account);
        entityManager.getTransaction().commit();
    }

    public Account findById(Integer id) {
        return entityManager.find(Account.class, id);
    }

    public List<Account> findAll() {
        TypedQuery<Account> query = entityManager.createQuery("SELECT a FROM Account a", Account.class);
        return query.getResultList();
    }

    public List<Account> findByCustomer(Custumer custumer) {
        TypedQuery<Account> query = entityManager.createQuery(
                "SELECT a FROM Account a WHERE a.custumer = :custumer", Account.class);
        query.setParameter("custumer", custumer);
        return query.getResultList();
    }

    public void update(Account account) {
        entityManager.getTransaction().begin();
        entityManager.merge(account);
        entityManager.getTransaction().commit();
    }

    public void delete(Account account) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(account) ? account : entityManager.merge(account));
        entityManager.getTransaction().commit();
    }

    public void deleteById(Integer id) {
        Account account = findById(id);
        if (account != null) {
            delete(account);
        }
    }
}