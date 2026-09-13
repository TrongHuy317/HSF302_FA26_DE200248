package fe.DE200248;

import fe.DE200248.dao.EmployeeDAO;
import fe.DE200248.entity.Employee;
import fe.DE200248.entity.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- BẮT ĐẦU DEMO LUỒNG CRUD (TODO 0.8) ---");

        try {
            EmployeeDAO dao = new EmployeeDAO();
            
            // 1. CREATE (Tạo mới)
            System.out.println("\n[1] Đang tạo mới Employee...");
            Employee emp = new Employee();
            emp.setFullName("Nguyen Trong Huy");
            // Sinh email ngẫu nhiên để không bị trùng lặp khi chạy nhiều lần (do ràng buộc unique=true)
            emp.setEmail("huynt" + System.currentTimeMillis() + "@example.com"); 
            emp.setSalary(new BigDecimal("1500.50"));
            emp.setGender(Gender.MALE);
            emp.setHireDate(LocalDate.of(2023, 1, 15));
            emp.setActive(true);
            
            // --- TODO 0.10: Giải thích Entity Lifecycle ---
            // 1) Lúc này (trước save): entity 'emp' vừa được tạo bằng 'new', đang ở trạng thái New/Transient.
            // 2) Bên trong hàm save(): ngay sau khi gọi em.persist(emp), nó chuyển sang trạng thái Managed.
            dao.save(emp);
            
            // 3) Lúc này (sau khi save return): EntityManager bên trong DAO đã bị close(), 
            // do đó entity 'emp' chuyển sang trạng thái Detached.
            Long savedId = emp.getId();
            System.out.println(" -> Đã tạo thành công! ID được cấp là: " + savedId);

            // 2. READ (Đọc kiểm tra)
            System.out.println("\n[2] Đang tìm kiếm (Read) Employee có ID = " + savedId + "...");
            Employee readEmp1 = dao.findById(savedId);
            System.out.println(" -> Kết quả tìm được: " + readEmp1);

            // 3. UPDATE (Cập nhật)
            System.out.println("\n[3] Đang cập nhật lương (Update) cho Employee...");
            readEmp1.setSalary(new BigDecimal("9999.99"));
            readEmp1.setActive(false);
            
            // 4) Khi gọi dao.update(): bên trong DAO, object trả về từ em.merge() là Managed (trong transaction đó).
            // Còn object 'readEmp1' (cũ) truyền vào vẫn giữ nguyên trạng thái Detached.
            readEmp1 = dao.update(readEmp1); 
            System.out.println(" -> Đã gọi lệnh update thành công.");

            // 4. READ (Đọc lại kiểm tra update)
            System.out.println("\n[4] Đang tìm kiếm lại để kiểm tra lương mới...");
            Employee readEmp2 = dao.findById(savedId);
            System.out.println(" -> Kết quả sau update: " + readEmp2);

            // 5. DELETE (Xóa)
            System.out.println("\n[5] Đang xóa (Delete) Employee có ID = " + savedId + "...");
            
            // 5) Trong hàm delete(): sau khi gọi em.remove(e), entity e chuyển sang trạng thái Removed.
            // Khi transaction commit(), data thực sự bị xóa khỏi DB.
            dao.delete(savedId);
            System.out.println(" -> Đã gọi lệnh delete thành công.");

            // 6. READ (Đọc lại kiểm tra delete)
            System.out.println("\n[6] Đang tìm kiếm lại để xem đã thực sự xóa chưa...");
            Employee readEmp3 = dao.findById(savedId);
            if (readEmp3 == null) {
                System.out.println(" -> Kết quả: trả về NULL (Nhân viên đã bị xóa hoàn toàn khỏi DB).");
            } else {
                System.out.println(" -> Kết quả: " + readEmp3);
            }

            System.out.println("\n--- TODO 0.9: KIỂM CHỨNG RÀNG BUỘC UNIQUE EMAIL ---");
            String duplicateEmail = "test_unique_" + System.currentTimeMillis() + "@example.com";
            
            System.out.println(" -> Tạo Nhân viên A với email: " + duplicateEmail);
            Employee empA = new Employee();
            empA.setFullName("Nhân viên A");
            empA.setEmail(duplicateEmail);
            empA.setSalary(new BigDecimal("1000"));
            empA.setGender(Gender.FEMALE);
            empA.setHireDate(LocalDate.now());
            empA.setActive(true);
            dao.save(empA);
            System.out.println(" -> Lưu Nhân viên A thành công!");

            System.out.println(" -> Tạo Nhân viên B với CÙNG email: " + duplicateEmail);
            Employee empB = new Employee();
            empB.setFullName("Nhân viên B");
            empB.setEmail(duplicateEmail); // Cố tình set trùng email
            empB.setSalary(new BigDecimal("2000"));
            empB.setGender(Gender.MALE);
            empB.setHireDate(LocalDate.now());
            empB.setActive(true);

            try {
                dao.save(empB);
                System.out.println(" -> LỖI LOGIC: Không có exception nào bị ném ra, unique constraint không hoạt động!");
            } catch (Exception ex) {
                System.out.println(" -> BẮT ĐƯỢC LỖI THÀNH CÔNG: Đã ngăn chặn lưu trùng email!");
                System.out.println(" -> Chi tiết lỗi từ Hibernate: " + ex.getMessage());
            }

            // Dọn dẹp
            dao.delete(empA.getId());

            System.out.println("\n--- KẾT THÚC DEMO THÀNH CÔNG, KHÔNG CÓ LỖI ---");
            
        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra trong quá trình Demo:");
            e.printStackTrace();
        }
    }
}