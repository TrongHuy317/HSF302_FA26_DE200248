package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.List;
import com.hsf302.ch4.pojo.Gender;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long>,
                                           JpaSpecificationExecutor<Student> {
    // sẽ bổ sung dần ở các TODO sau
    Optional<Student> findByStudentCode(String studentCode);   // WHERE student_code = ?
    boolean existsByEmail(String email);                        // kiểm tra tồn tại
    long countByActiveTrue();                                   // WHERE active = 1 (không cần tham số)
    
    List<Student> findByFullNameContainingIgnoreCase(String keyword);   // UPPER(full_name) LIKE UPPER('%kw%')
    List<Student> findByEmailEndingWith(String suffix);                 // email LIKE '%suffix'
    List<Student> findByEmailIsNull();                                  // email IS NULL

    List<Student> findByGpaBetweenOrderByGpaDesc(double min, double max);   // gpa BETWEEN ? AND ? ORDER BY gpa DESC
    List<Student> findByGenderAndActiveTrue(Gender gender);                  // gender = ? AND active = 1
    List<Student> findByDobAfter(LocalDate date);                            // dob > ?

    List<Student> findByDepartment_CodeOrderByFullNameAsc(String code);   // JOIN departments ... WHERE d.code = ?
    long countByDepartment_Code(String code);
    List<Student> findTop3ByOrderByGpaDesc();                              // SELECT TOP 3 ... ORDER BY gpa DESC

    @Query("SELECT s FROM Student s " +                                          // TODO 12
           "WHERE s.department.code = :code AND s.gpa >= :minGpa ORDER BY s.gpa DESC")
    List<Student> findGoodStudentsInDepartment(@Param("code") String code, @Param("minGpa") double minGpa);

    @Query("SELECT s FROM Student s " +
           "WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :kw, '%')) " +
           "   OR LOWER(s.email)    LIKE LOWER(CONCAT('%', :kw, '%')) " +
           "ORDER BY s.fullName")
    List<Student> searchByKeyword(@Param("kw") String keyword);
}
