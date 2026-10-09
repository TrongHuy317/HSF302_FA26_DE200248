package fu.HuyLT.Chapter6.service.impl;

import fu.HuyLT.Chapter6.entity.Student;
import fu.HuyLT.Chapter6.repository.StudentRepository;
import fu.HuyLT.Chapter6.service.StudentService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)          // mặc định: mọi method chỉ đọc
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public org.springframework.data.domain.Page<Student> getStudentsPaginated(String keyword, int page, int size, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page, size, sort
        );
        if (keyword == null || keyword.trim().isEmpty()) {
            return studentRepository.findAll(pageable);
        }
        return studentRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword.trim(), keyword.trim(), pageable);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional
    public Student create(fu.HuyLT.Chapter6.dto.StudentForm form) {
        Student student = new Student();
        student.setName(form.getName());
        student.setEmail(form.getEmail());
        student.setAge(form.getAge());
        student.setMajor(form.getMajor());
        student.setGpa(form.getGpa());
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public boolean update(Long id, fu.HuyLT.Chapter6.dto.StudentForm form) {
        return studentRepository.findById(id)
                .map(existing -> {
                    existing.setName(form.getName());
                    existing.setEmail(form.getEmail());
                    existing.setAge(form.getAge());
                    existing.setMajor(form.getMajor());
                    existing.setGpa(form.getGpa());
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (!studentRepository.existsById(id)) {
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean isEmailTaken(String email, Long excludeId) {
        if (email == null || email.isBlank()) return false;
        return excludeId == null
                ? studentRepository.existsByEmailIgnoreCase(email.trim())
                : studentRepository.existsByEmailIgnoreCaseAndIdNot(email.trim(), excludeId);
    }

    @Override
    public List<String> getMajors() {
        return List.of("CNTT", "KTPM", "HTTT", "ATTT", "MMT");
    }
}
