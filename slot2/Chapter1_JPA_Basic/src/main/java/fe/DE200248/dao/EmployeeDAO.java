package fe.DE200248.dao;

import fe.DE200248.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import java.math.BigDecimal;

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
    
    // --------- READ (TODO 0.4) ---------
    public Employee findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Employee.class, id); // tra ve null neu khong ton tai
        } finally {
            em.close();
        }
    }

    public List<Employee> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT e FROM Employee e", Employee.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // --------- READ co dieu kien (TODO 0.5) ---------
    public Employee findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Employee> result = em.createQuery(
                    "SELECT e FROM Employee e WHERE e.email = :email", Employee.class)
                    .setParameter("email", email)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public List<Employee> findBySalaryGreaterThanAndActive(BigDecimal minSalary) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT e FROM Employee e WHERE e.salary > :minSalary AND e.active = true",
                    Employee.class)
                    .setParameter("minSalary", minSalary)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
