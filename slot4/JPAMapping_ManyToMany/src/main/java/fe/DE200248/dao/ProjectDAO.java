package fe.DE200248.dao;

import fe.DE200248.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ProjectDAO {
    
    // TODO 5.8: Viết JPQL đếm số nhân viên active tham gia mỗi project và tính tổng salary của các nhân viên đó
    public List<Object[]> getProjectStats() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            String jpql = "SELECT p.projectName, COUNT(e), SUM(e.salary) " +
                          "FROM Project p JOIN p.employees e " +
                          "WHERE e.active = true " +
                          "GROUP BY p.projectName";
            return em.createQuery(jpql, Object[].class).getResultList();
        } finally {
            em.close();
        }
    }
}
