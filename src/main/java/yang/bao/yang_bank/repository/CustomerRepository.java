package yang.bao.yang_bank.repository;

import yang.bao.yang_bank.entity.Custumer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerRepository {
    private final EntityManager entityManager;

    public CustomerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Custumer customer) {
        entityManager.getTransaction().begin();
        entityManager.persist(customer);
        entityManager.getTransaction().commit();
    }

    public Custumer findById(Integer id) {
        return entityManager.find(Custumer.class, id);
    }

    public Custumer findByUsername(String username) {
        TypedQuery<Custumer> query = entityManager.createQuery(
                "SELECT c FROM Custumer c WHERE c.username = :username", Custumer.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public List<Custumer> findAll() {
        TypedQuery<Custumer> query = entityManager.createQuery("SELECT c FROM Custumer c", Custumer.class);
        return query.getResultList();
    }

    public void update(Custumer customer) {
        entityManager.getTransaction().begin();
        entityManager.merge(customer);
        entityManager.getTransaction().commit();
    }

    public void delete(Custumer customer) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(customer) ? customer : entityManager.merge(customer));
        entityManager.getTransaction().commit();
    }

    public void deleteById(Integer id) {
        Custumer customer = findById(id);
        if (customer != null) {
            delete(customer);
        }
    }
}