package fe.DE200248;

import fe.DE200248.dao.EmployeeDAO;
import fe.DE200248.entity.Employee;
import fe.DE200248.entity.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Đang khởi tạo kết nối Database...");

        try {
            EmployeeDAO dao = new EmployeeDAO();
            System.out.println("Kết nối Database thành công! Bảng đã được tự động tạo/cập nhật.");

            // Test chức năng lưu Employee
            Employee emp = new Employee();
            emp.setFullName("Nguyen Trong Huy");
            emp.setEmail("huynt@example.com");
            emp.setSalary(new BigDecimal("1500.50"));
            emp.setGender(Gender.MALE);
            emp.setHireDate(LocalDate.of(2023, 1, 15));
            emp.setActive(true);

            System.out.println("Trước khi save, ID = " + emp.getId());
            dao.save(emp);
            System.out.println("Sau khi save thành công, ID = " + emp.getId());

        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra khi kết nối hoặc lưu dữ liệu:");
            e.printStackTrace();
        }
    }
}