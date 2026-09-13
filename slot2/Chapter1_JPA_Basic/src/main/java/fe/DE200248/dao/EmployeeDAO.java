package fe.DE200248.dao;

import fe.DE200248.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EmployeeDAO {


    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("hsf302FU");

    // --------- CREATE (TODO 0.3) ---------
    public void save(Employee e) {

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(e);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close(); 
        }
    }
}
