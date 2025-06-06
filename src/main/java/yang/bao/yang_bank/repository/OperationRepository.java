package yang.bao.yang_bank.repository;

import yang.bao.yang_bank.entity.Account;
import yang.bao.yang_bank.entity.Operation;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class OperationRepository {
    private final EntityManager entityManager;

    public OperationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Operation operation) {
        entityManager.getTransaction().begin();
        if (operation.getOperationDate() == null) {
            operation.setOperationDate(java.time.Instant.now());
        }
        entityManager.persist(operation);
        entityManager.getTransaction().commit();
    }

    public Operation findById(Integer id) {
        return entityManager.find(Operation.class, id);
    }

    public List<Operation> findAll() {
        TypedQuery<Operation> query = entityManager.createQuery("SELECT o FROM Operation o", Operation.class);
        return query.getResultList();
    }

    public List<Operation> findByAccount(Account account) {
        TypedQuery<Operation> query = entityManager.createQuery(
                "SELECT o FROM Operation o WHERE o.account = :account", Operation.class);
        query.setParameter("account", account);
        return query.getResultList();
    }

    public void update(Operation operation) {
        entityManager.getTransaction().begin();
        entityManager.merge(operation);
        entityManager.getTransaction().commit();
    }

    public void delete(Operation operation) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(operation) ? operation : entityManager.merge(operation));
        entityManager.getTransaction().commit();
    }

    public void deleteById(Integer id) {
        Operation operation = findById(id);
        if (operation != null) {
            delete(operation);
        }
    }
}