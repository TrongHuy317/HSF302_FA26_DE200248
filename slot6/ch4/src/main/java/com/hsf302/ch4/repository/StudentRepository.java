package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.List;
import com.hsf302.ch4.pojo.Gender;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import com.hsf302.ch4.dto.StudentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.hsf302.ch4.pojo.Department;

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
    // ===== Exercise 2 =====
    List<Student> findByCourses_CodeOrderByFullNameAsc(String courseCode);
    long countByCourses_Code(String courseCode);
    List<Student> findByCourses_CodeAndActiveTrueOrderByFullNameAsc(String courseCode);
    List<Student> findByCoursesIsEmptyOrderByFullNameAsc();
    boolean existsByStudentCodeAndCourses_Code(String studentCode, String courseCode);

    @Query("SELECT s FROM Student s " +                                          // TODO 12
           "WHERE s.department.code = :code AND s.gpa >= :minGpa ORDER BY s.gpa DESC")
    List<Student> findGoodStudentsInDepartment(@Param("code") String code, @Param("minGpa") double minGpa);

    @Query("SELECT s FROM Student s " +
           "WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :kw, '%')) " +
           "   OR LOWER(s.email)    LIKE LOWER(CONCAT('%', :kw, '%')) " +
           "ORDER BY s.fullName")
    List<Student> searchByKeyword(@Param("kw") String keyword);

    @Query("SELECT s FROM Student s " +
           "WHERE s.gpa > (SELECT AVG(s2.gpa) FROM Student s2) " +
           "ORDER BY s.gpa DESC")
    List<Student> findAboveAverageGpa();

    @Query(value = "SELECT TOP (:n) s.* " +
                   "FROM students s JOIN departments d ON s.department_id = d.id " +
                   "WHERE d.code = :code " +
                   "ORDER BY s.gpa DESC",
           nativeQuery = true)
    List<Student> findTopNByDepartmentNative(@Param("code") String code, @Param("n") int n);

    @Query("SELECT s.studentCode AS studentCode, s.fullName AS fullName, " +
           "       s.gpa AS gpa, d.name AS departmentName " +
           "FROM Student s JOIN s.department d " +
           "WHERE s.active = true " +
           "ORDER BY s.fullName")
    List<StudentSummary> findActiveSummaries();

    @Query("SELECT s FROM Student s WHERE s.department.code = :code AND s.active = true")
    Page<Student> findActiveByDepartment(@Param("code") String code, Pageable pageable);

    // ===== Part E — Modifying =====
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Student s SET s.active = false WHERE s.gpa < :threshold AND s.active = true")
    int deactivateLowGpa(@Param("threshold") double threshold);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Student s SET s.department = :to WHERE s.department = :from")
    int transferStudents(@Param("from") Department from, @Param("to") Department to);

    long deleteByActiveFalse();

    @Query("SELECT s FROM Student s WHERE s.gender = :gender")
    List<Student> findByGender(@Param("gender") Gender gender);

    @Query("SELECT s FROM Student s JOIN s.courses c " +
            "WHERE c.code = :code AND s.gpa >= :minGpa ORDER BY s.gpa DESC")
    List<Student> findGoodStudentsInCourse(@Param("code") String courseCode,
                                           @Param("minGpa") double minGpa);
}
