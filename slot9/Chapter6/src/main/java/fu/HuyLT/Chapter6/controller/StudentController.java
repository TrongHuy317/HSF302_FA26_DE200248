package fu.HuyLT.Chapter6.controller;

import fu.HuyLT.Chapter6.entity.Student;
import fu.HuyLT.Chapter6.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students/list";
    }

    @GetMapping("/{id}")
    public String detailStudent(@PathVariable Long id, Model model) {
        return studentService.findById(id).map(student -> {
            model.addAttribute("student", student);
            return "students/detail";
        }).orElse("redirect:/students");
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }

    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "students/form";
        }
        
        if (student.getId() == null) {
            studentService.create(student);
        } else {
            studentService.update(student.getId(), student);
        }
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        return studentService.findById(id).map(student -> {
            model.addAttribute("student", student);
            return "students/form";
        }).orElse("redirect:/students");
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/students";
    }
}
