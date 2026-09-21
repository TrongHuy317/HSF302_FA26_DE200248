package fe.DE200248.dao;

import fe.DE200248.pojo.Employee;
import fe.DE200248.pojo.Project;
import fe.DE200248.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class EmployeeDAO {

    public void save(Employee employee) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Employee update(Employee employee) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee merged = em.merge(employee);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee employee = em.find(Employee.class, id);
            if (employee != null) {
                em.remove(employee);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Employee findById(Long id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public List<Employee> findAll() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
        } finally {
            em.close();
        }
    }

    // TODO 5.6: Gán nhân viên vào dự án
    public void assignEmployeeToProject(Long employeeId, Long projectId) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // 1. Tìm cả 2 entity (Employee và Project)
            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, projectId);

            if (employee != null && project != null) {
                // 2. Gọi helper method để đồng bộ hóa 2 chiều
                employee.assignToProject(project);
                // JPA sẽ tự động update bảng employee_project khi commit vì cả 2 đối tượng đang ở trạng thái Managed
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // TODO 5.9: Gỡ nhân viên khỏi dự án
    public void unassignEmployeeFromProject(Long employeeId, Long projectId) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, projectId);

            if (employee != null && project != null) {
                // Gọi helper method để gỡ khỏi Set
                // Do dùng Set thay vì List, Hibernate sẽ chỉ gen 1 lệnh DELETE đúng 1 dòng trên bảng trung gian
                employee.unassignFromProject(project);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // TODO 5.10: Viết JPQL tìm các Employee đang tham gia > 1 project
    public List<Employee> findEmployeesInMultipleProjects() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            String jpql = "SELECT e FROM Employee e WHERE e.active = true AND SIZE(e.projects) > 1";
            return em.createQuery(jpql, Employee.class).getResultList();
        } finally {
            em.close();
        }
    }
}
