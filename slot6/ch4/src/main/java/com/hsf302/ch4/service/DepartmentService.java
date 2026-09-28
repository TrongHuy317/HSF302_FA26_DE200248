package com.hsf302.ch4.service;

import java.util.List;
import java.util.Optional;
import com.hsf302.ch4.pojo.Department;
import com.hsf302.ch4.dto.DepartmentStatDTO;

public interface DepartmentService {
    // Các method được bổ sung dần từ TODO 6
    long count();                                   // TODO 6
    boolean existsById(Long id);                    // TODO 6
    List<Department> findDepartmentsWithoutStudents();  // TODO 11d
    List<DepartmentStatDTO> getStatistics();   // TODO 14 (dùng lại ở TODO 23)
    Optional<Department> findByCode(String code);   // TODO 16a
    Department getWithStudents(String code);   // TODO 16b
}
