package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // bổ sung dần từ TODO 7
    Optional<Course> findByCode(String code);
}
