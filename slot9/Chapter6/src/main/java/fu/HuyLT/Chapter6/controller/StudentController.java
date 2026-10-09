package fu.HuyLT.Chapter6.controller;

import fu.HuyLT.Chapter6.entity.Student;
import fu.HuyLT.Chapter6.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final String FORM_VIEW = "students/form";

    private final StudentService studentService;

    // Constructor injection (recommended) — 1 constructor nên không cần @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /** Chạy trước MỌI handler trong controller → view nào cũng có ${majors} */
    @ModelAttribute("majors")
    public List<String> majors() {
        return studentService.getMajors();
    }

    // ==================== READ ALL / SEARCH / PAGINATE / SORT ====================
    @GetMapping
    public String list(Model model, 
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "5") int size,
                       @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                       @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        
        org.springframework.data.domain.Page<Student> studentPage = studentService.getStudentsPaginated(keyword, page, size, sortField, sortDir);
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        
        return "students/list";
    }

    // ==================== READ ONE ====================

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        return studentService.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    return "students/detail";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên ID: " + id);
                    return "redirect:/students";
                });
    }

    // ==================== CREATE ====================

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new fu.HuyLT.Chapter6.dto.StudentForm());
        return formView(model, false);
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("student") fu.HuyLT.Chapter6.dto.StudentForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        if (!bindingResult.hasFieldErrors("email")
                && studentService.isEmailTaken(form.getEmail(), null)) {
            bindingResult.rejectValue("email", "duplicate", "Email đã tồn tại");
        }
        if (bindingResult.hasErrors()) {
            return formView(model, false);
        }
        try {
            studentService.create(form);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("email", "duplicate", "Email đã tồn tại");
            return formView(model, false);
        }
        ra.addFlashAttribute("successMsg", "Thêm sinh viên thành công!");
        return "redirect:/students";
    }

    // ==================== UPDATE ====================

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        return studentService.findById(id)
                .map(student -> {
                    fu.HuyLT.Chapter6.dto.StudentForm form = new fu.HuyLT.Chapter6.dto.StudentForm();
                    form.setId(student.getId());
                    form.setName(student.getName());
                    form.setEmail(student.getEmail());
                    form.setAge(student.getAge());
                    form.setMajor(student.getMajor());
                    form.setGpa(student.getGpa());
                    model.addAttribute("student", form);
                    return formView(model, true);
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên ID: " + id);
                    return "redirect:/students";
                });
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("student") fu.HuyLT.Chapter6.dto.StudentForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        form.setId(id);

        if (!bindingResult.hasFieldErrors("email")
                && studentService.isEmailTaken(form.getEmail(), id)) {
            bindingResult.rejectValue("email", "duplicate", "Email đã được sinh viên khác sử dụng");
        }
        if (bindingResult.hasErrors()) {
            return formView(model, true);
        }
        try {
            if (studentService.update(id, form)) {
                ra.addFlashAttribute("successMsg", "Cập nhật thành công!");
            } else {
                ra.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên ID: " + id);
            }
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("email", "duplicate", "Email đã được sinh viên khác sử dụng");
            return formView(model, true);
        }
        return "redirect:/students";
    }

    // ==================== DELETE ====================

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        if (studentService.delete(id)) {
            ra.addFlashAttribute("successMsg", "Xóa sinh viên thành công!");
        } else {
            ra.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên để xóa!");
        }
        return "redirect:/students";
    }

    // ==================== Helper ====================

    private String formView(Model model, boolean isEdit) {
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Cập nhật sinh viên" : "Thêm sinh viên mới");
        return FORM_VIEW;
    }
}
