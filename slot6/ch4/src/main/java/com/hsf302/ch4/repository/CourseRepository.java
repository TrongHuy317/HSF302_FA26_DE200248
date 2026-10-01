package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // bổ sung dần từ TODO 7
    Optional<Course> findByCode(String code);
    List<Course> findBySemesterOrderByCodeAsc(String semester);
    long countBySemester(String semester);
    List<Course> findByStudents_StudentCodeOrderByCodeAsc(String studentCode);
    List<Course> findByStudents_Department_CodeOrderByCodeAsc(String deptCode);          // có thể TRÙNG
    List<Course> findDistinctByStudents_Department_CodeOrderByCodeAsc(String deptCode);  // loại trùng
    List<Course> findByStudentsIsEmpty();
}
