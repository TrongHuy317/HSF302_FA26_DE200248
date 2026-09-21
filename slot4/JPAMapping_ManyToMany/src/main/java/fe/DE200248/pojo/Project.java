package fe.DE200248.pojo;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;

    @Column(unique = true, nullable = false)
    private String projectCode;

    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees = new HashSet<>();


    public Project() {
    }

    public Project(String projectName, String projectCode) {
        this.projectName = projectName;
        this.projectCode = projectCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    /*
     * Lý do KHÔNG dùng `id` cho equals() và hashCode():
     * - Khi một entity mới được khởi tạo (transient state), `id` của nó là null.
     * - Nếu ta add entity này vào một Set, nó sẽ được lưu theo mã băm (hash) của null.
     * - Khi entity được lưu xuống DB (persisted state), DB sinh ra `id` cho nó. Lúc này mã băm bị thay đổi.
     * - Set không tìm thấy mã băm ban đầu nữa nên sẽ dẫn tới lỗi không thể tìm thấy hoặc xóa phần tử trong Set.
     * - Do đó, trong JPA ta nên dùng Business Key (Natural Key) - một trường duy nhất không bao giờ đổi (như projectCode, email).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(projectCode, project.projectCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectCode);
    }
}
