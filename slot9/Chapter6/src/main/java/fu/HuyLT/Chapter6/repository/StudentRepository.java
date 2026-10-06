package fu.HuyLT.Chapter6.repository;

import fu.HuyLT.Chapter6.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
