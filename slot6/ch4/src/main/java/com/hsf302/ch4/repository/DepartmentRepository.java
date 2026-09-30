package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import com.hsf302.ch4.dto.DepartmentStatDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // sẽ bổ sung dần ở các TODO sau
    Optional<Department> findByCode(String code);        // dùng lại ở TODO 16, 22
    List<Department> findByStudentsIsEmpty();            // WHERE NOT EXISTS (SELECT ... FROM students ...)

    @Query("SELECT new com.hsf302.ch4.dto.DepartmentStatDTO(d.code, d.name, COUNT(s), AVG(s.gpa)) " +
           "FROM Department d LEFT JOIN d.students s " +
           "GROUP BY d.code, d.name " +
           "ORDER BY d.code")
    List<DepartmentStatDTO> getDepartmentStats();

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.students WHERE d.code = :code")
    Optional<Department> findByCodeWithStudents(@Param("code") String code);

    @Query("SELECT d.name, COUNT(s.id) " +
            "FROM Department d LEFT JOIN d.students s " +
            "GROUP BY d.name")
    List<Object[]> countStudentsByDepartment();
}
