package fe.DE200248;

import fe.DE200248.dao.DepartmentDAO;
import fe.DE200248.dao.EmployeeDAO;
import fe.DE200248.pojo.Department;
import fe.DE200248.pojo.Employee;
import fe.DE200248.pojo.Gender;
import fe.DE200248.util.JPAUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        DepartmentDAO departmentDAO = new DepartmentDAO();

        // 1) Tạo Department + 3 Employee, add qua helper method (TODO 2.4)
        Department it = new Department("Marketing", "Ha Noi");

        Employee e1 = new Employee("aa.nguyen@company.com", "Nguyen Van A",
                Gender.MALE,
                new BigDecimal("15000000"), LocalDate.of(2022, 1, 10));
        Employee e2 = new Employee("bb.tran@company.com", "Tran Thi B", Gender.FEMALE,
                new BigDecimal("18000000"), LocalDate.of(2021, 6, 1));
        Employee e3 = new Employee("cc.le@company.com", "Le Van C", Gender.OTHER,
                new BigDecimal("12000000"), LocalDate.of(2023, 3, 15));

        it.addEmployee(e1);
        it.addEmployee(e2);
        it.addEmployee(e3);

        // 2) Chỉ persist(department) - cascade = ALL tự lo phần Employee (TODO 2.7)
        departmentDAO.save(it);
        
        System.out.println("Da luu Department, id = " + it.getId());

        // 3) Tim lai kem employees bang JOIN FETCH (TODO 2.6) - khong bi 
        // LazyInitializationException du EntityManager cua lan tim nay da dong,
        // vi employees da duoc load ngay trong cung 1 query.
        Department found = departmentDAO.findByIdWithEmployees(it.getId());
        System.out.println("Phong ban: " + found.getName());
        for (Employee e : found.getEmployees()) {
            System.out.println("  - " + e);
        }

        System.out.println("\n=== TODO 2.8: Tái hiện N+1 Query Problem ===");
        // Tạo thêm 1 phòng ban để thấy rõ N+1 (N=2)
        Department hr = new Department("HR", "HCM");
        Employee hrEmp = new Employee("hr@company.com", "HR Employee", Gender.FEMALE, new BigDecimal("10000000"), LocalDate.now());
        hr.addEmployee(hrEmp);
        departmentDAO.save(hr);

        jakarta.persistence.EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            System.out.println("\n--- 1. Gọi findAll() (Không JOIN FETCH) ---");
            java.util.List<Department> allDepts = em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
            System.out.println("-> Tìm thấy " + allDepts.size() + " phòng ban. (Đã sinh ra 1 câu SELECT cho Department)");

            System.out.println("\n--- 2. Lặp qua department.getEmployees() của từng phần tử ---");
            for (Department d : allDepts) {
                System.out.println("Phòng ban: " + d.getName());
                // Truy cập getEmployees() -> Kích hoạt query riêng (Lazy Loading)
                for (Employee e : d.getEmployees()) {
                    System.out.println("  - " + e.getFullName());
                }
            }
            System.out.println("\n-> Kết luận: Có N phòng ban thì sẽ sinh ra thêm N câu SELECT employees (Tổng = 1 + N câu).");
            System.out.println("-> Nguyên nhân: Quan hệ @OneToMany mặc định là LAZY loading.");
        } finally {
            em.close();
        }

        System.out.println("\n=== TODO 2.9: Fix N+1 bằng JOIN FETCH ===");
        System.out.println("--- Gọi findAllWithEmployees() ---");
        // Gọi hàm DAO mới tạo (EntityManager bên trong sẽ mở và đóng ngay trong hàm)
        // Dù đóng EM, vòng lặp sau đó vẫn không bị LazyInitializationException
        java.util.List<Department> allDeptsFixed = departmentDAO.findAllWithEmployees();
        System.out.println("-> Tìm thấy " + allDeptsFixed.size() + " phòng ban. (Chỉ sinh ra ĐÚNG 1 câu SELECT JOIN FETCH)");

        for (Department d : allDeptsFixed) {
            System.out.println("Phòng ban: " + d.getName());
            for (Employee e : d.getEmployees()) {
                System.out.println("  - " + e.getFullName());
            }
        }
        
        System.out.println("\n-> [KẾT LUẬN SO SÁNH SQL]");
        System.out.println("   - Trước khi fix (TODO 2.8): 1 câu SELECT findAll + N câu SELECT employees = 1 + N câu.");
        System.out.println("   - Sau khi fix (TODO 2.9)  : 1 câu SELECT duy nhất chứa JOIN FETCH lấy tất cả dữ liệu cùng lúc.");

        System.out.println("\n--- Thử save() thêm 1 Employee dùng lại email đã tồn tại ---");
        try {
            Employee duplicateEmp = new Employee("aa.nguyen@company.com", "Duplicate Test", Gender.MALE,
                    new BigDecimal("2000"), LocalDate.now());
            duplicateEmp.setDepartment(found);
            EmployeeDAO empDAO = new EmployeeDAO();
            empDAO.save(duplicateEmp);
        } catch (Exception ex) {
            System.out.println("Bắt thành công exception vi phạm Unique Email: " + ex.getMessage());
        }

        System.out.println("\n=== TODO 5.7: ManyToMany Demo ===");
        // 1. Tạo 3 Employee (cần set department vì nullable=false)
        Employee nv1 = new Employee("nv1@test.com", "NV 1", Gender.MALE, new BigDecimal("1000"), LocalDate.now(), true);
        Employee nv2 = new Employee("nv2@test.com", "NV 2", Gender.FEMALE, new BigDecimal("2000"), LocalDate.now(), true);
        Employee nv3 = new Employee("nv3@test.com", "NV 3", Gender.OTHER, new BigDecimal("3000"), LocalDate.now(), true);

        nv1.setDepartment(found);
        nv2.setDepartment(found);
        nv3.setDepartment(found);

        EmployeeDAO empDAO = new EmployeeDAO();
        empDAO.save(nv1);
        empDAO.save(nv2);
        empDAO.save(nv3);

        // 2. Tạo 2 Project
        fe.DE200248.pojo.Project pA = new fe.DE200248.pojo.Project("Dự án A", "PROJ-A");
        fe.DE200248.pojo.Project pB = new fe.DE200248.pojo.Project("Dự án B", "PROJ-B");

        jakarta.persistence.EntityManager em57 = JPAUtil.getEMF().createEntityManager();
        em57.getTransaction().begin();
        em57.persist(pA);
        em57.persist(pB);
        em57.getTransaction().commit();
        em57.close();

        // 3. Phân công chéo
        empDAO.assignEmployeeToProject(nv1.getId(), pA.getId());
        empDAO.assignEmployeeToProject(nv1.getId(), pB.getId());

        empDAO.assignEmployeeToProject(nv2.getId(), pB.getId());

        empDAO.assignEmployeeToProject(nv3.getId(), pA.getId());

        // 4. In ra danh sách project của từng nhân viên
        jakarta.persistence.EntityManager emPrint = JPAUtil.getEMF().createEntityManager();
        try {
            System.out.println("--- Danh sách Project của từng nhân viên ---");
            // Fetch những nhân viên vừa tạo
            java.util.List<Employee> listE = emPrint.createQuery("SELECT e FROM Employee e WHERE e.email LIKE 'nv%@test.com'", Employee.class).getResultList();
            for (Employee e : listE) {
                System.out.println("Nhân viên: " + e.getFullName());
                // Truy cập getProjects() cần có Transaction/EntityManager đang mở để khỏi dính LazyInitializationException
                for (fe.DE200248.pojo.Project p : e.getProjects()) {
                    System.out.println("  -> Tham gia: " + p.getProjectName() + " (" + p.getProjectCode() + ")");
                }
            }
        } finally {
            emPrint.close();
        }

        System.out.println("\n=== TODO 5.8: JPQL Thống kê Project ===");
        fe.DE200248.dao.ProjectDAO projectDAO = new fe.DE200248.dao.ProjectDAO();
        java.util.List<Object[]> stats = projectDAO.getProjectStats();
        for (Object[] row : stats) {
            String projName = (String) row[0];
            Long empCount = (Long) row[1];
            java.math.BigDecimal totalSalary = (java.math.BigDecimal) row[2];
            System.out.println("Project: " + projName + " | Số NV active: " + empCount + " | Tổng lương: " + totalSalary);
        }

        System.out.println("\n=== TODO 5.9: Gỡ nhân viên khỏi Project ===");
        System.out.println("Gỡ NV1 ra khỏi Dự án A...");
        empDAO.unassignEmployeeFromProject(nv1.getId(), pA.getId());
        
        System.out.println("Gọi lại thống kê để xác nhận (số lượng NV của Dự án A sẽ giảm 1, NV gốc và Project gốc không bị xóa):");
        java.util.List<Object[]> statsAfter = projectDAO.getProjectStats();
        for (Object[] row : statsAfter) {
            String projName = (String) row[0];
            Long empCount = (Long) row[1];
            System.out.println("Project: " + projName + " | Số NV active: " + empCount);
        }

        System.out.println("\n=== TODO 5.10: Tìm Employee tham gia > 1 Project ===");
        System.out.println("Gán lại NV1 vào Dự án A để test...");
        empDAO.assignEmployeeToProject(nv1.getId(), pA.getId());

        java.util.List<Employee> busyEmployees = empDAO.findEmployeesInMultipleProjects();
        if (busyEmployees.isEmpty()) {
            System.out.println("Không có nhân viên nào tham gia nhiều hơn 1 dự án.");
        } else {
            for (Employee e : busyEmployees) {
                System.out.println("Nhân viên bận rộn (>1 project): " + e.getFullName());
            }
        }

        System.out.println("\n=== TODO 5.11 / Checklist: Demo kiểm tra equals()/hashCode() ===");
        java.util.Set<Employee> testSet = new java.util.HashSet<>();
        Employee testEmp1 = new Employee("nv1@test.com", "NV 1", Gender.MALE, new BigDecimal("1000"), LocalDate.now(), true);
        Employee testEmp2 = new Employee("nv1@test.com", "Bản sao NV 1 (Khác instance)", Gender.MALE, new BigDecimal("1000"), LocalDate.now(), true);
        testSet.add(testEmp1);
        testSet.add(testEmp2);
        System.out.println("Thêm 2 object có cùng email vào Set...");
        System.out.println("-> Kích thước của Set hiện tại: " + testSet.size() + " (Kỳ vọng: 1)");

        JPAUtil.close();
    }
}