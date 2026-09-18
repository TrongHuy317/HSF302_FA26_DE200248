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

        JPAUtil.close();
    }
}