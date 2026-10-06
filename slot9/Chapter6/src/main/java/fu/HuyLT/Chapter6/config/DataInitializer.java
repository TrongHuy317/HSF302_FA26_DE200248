package fu.HuyLT.Chapter6.config;

import fu.HuyLT.Chapter6.entity.Student;
import fu.HuyLT.Chapter6.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(StudentRepository studentRepository) {
        return args -> {
            if (studentRepository.count() == 0) {
                studentRepository.save(new Student("Nguyen Van A", "a@gmail.com", 20));
                studentRepository.save(new Student("Tran Thi B", "b@gmail.com", 21));
                studentRepository.save(new Student("Le Van C", "c@gmail.com", 22));
            }
        };
    }
}
