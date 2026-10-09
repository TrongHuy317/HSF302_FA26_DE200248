package fu.HuyLT.Chapter6.service;

import fu.HuyLT.Chapter6.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> findAll();
    
    org.springframework.data.domain.Page<Student> getStudentsPaginated(String keyword, int page, int size, String sortField, String sortDir);

    Optional<Student> findById(Long id);

    Student create(Student student);

    /** @return true nếu tìm thấy và cập nhật; false nếu không tồn tại id */
    boolean update(Long id, Student data);

    /** @return true nếu xoá được; false nếu không tồn tại id */
    boolean delete(Long id);

    /** Kiểm tra email trùng. excludeId = null khi thêm mới, = id hiện tại khi cập nhật */
    boolean isEmailTaken(String email, Long excludeId);

    List<String> getMajors();
}
