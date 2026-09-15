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
        Department dept = new Department("Marketing", "Ha Noi");

        Employee emp = new Employee("test2@company.com", "Test", Gender.OTHER,
                new BigDecimal("1000"), LocalDate.now());
        Employee e2 = new Employee("bc@company.com", "B", Gender.FEMALE,
                new BigDecimal("1200"), LocalDate.of(2022, 2, 1));
        Employee e3 = new Employee("cd@company.com", "C", Gender.OTHER,
                new BigDecimal("1500"), LocalDate.of(2022, 3, 1));

        dept.addEmployee(emp);
        dept.addEmployee(e2);
        dept.addEmployee(e3);

        DepartmentDAO deptDAO = new DepartmentDAO();
        deptDAO.save(dept);
        System.out.println("Đã thêm: " + dept.getName() + ", ID = " + dept.getId());

        Department found = deptDAO.findByIdWithEmployees(dept.getId());
        System.out.println("Tìm lại phòng ban: " + found.getName());
        for (Employee e : found.getEmployees()) {
            System.out.println("  - " + e);
        }

        System.out.println("Testing duplicate email exception...");
        try {
            Employee duplicateEmp = new Employee("test2@company.com", "Duplicate Test", Gender.MALE,
                    new BigDecimal("2000"), LocalDate.now());
            duplicateEmp.setDepartment(dept);
            EmployeeDAO empDAO = new EmployeeDAO();
            empDAO.save(duplicateEmp);
        } catch (Exception ex) {
            System.out.println("Bắt thành công exception vi phạm Unique Email: " + ex.getMessage());
        }

        JPAUtil.close();
    }
}