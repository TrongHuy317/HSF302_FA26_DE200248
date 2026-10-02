package com.hsf302.ch4.service;
import com.hsf302.ch4.dto.CourseStatDTO;
import com.hsf302.ch4.pojo.Course;
import java.util.Optional;
import java.util.List;


public interface CourseService {
    // bổ sung dần từ TODO 6
    long count();
    List<Course> findAllOrderByCode();
    Optional<Course> findById(Long id);
    Optional<Course> findByCode(String code);
    List<Course> findBySemester(String semester);
    long countBySemester(String semester);
    List<Course> findCoursesOfStudent(String studentCode);
    List<Course> findCoursesOfDepartment(String deptCode, boolean distinct);
    List<Course> findCoursesWithoutStudents();
    List<CourseStatDTO> getStatistics();
    List<Course> findFullCourses();
}