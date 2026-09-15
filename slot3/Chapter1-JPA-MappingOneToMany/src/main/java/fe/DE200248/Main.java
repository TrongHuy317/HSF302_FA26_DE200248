package fe.DE200248;

import fe.DE200248.pojo.Department;
import fe.DE200248.pojo.Employee;
import fe.DE200248.pojo.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Department dept = new Department("IT", "Ha Noi");
        Employee emp = new Employee("test@company.com", "Test", Gender.OTHER,
                new BigDecimal("1000"), LocalDate.now());
        dept.addEmployee(emp);
        System.out.println(dept.getEmployees().contains(emp));
        System.out.println(emp.getDepartment() == dept);
    }
}