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