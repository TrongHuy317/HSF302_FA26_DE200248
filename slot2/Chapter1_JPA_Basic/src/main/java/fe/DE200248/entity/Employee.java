package fe.DE200248.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true)
    private String email;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    // Luôn dùng STRING, KHÔNG dùng mặc định ORDINAL (số thứ tự dễ sai khi enum thay đổi)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    // JPA 2.2+ map LocalDate trực tiếp, không cần @Temporal
    private LocalDate hireDate;

    private boolean active;

    // KHÔNG có cột tương ứng trong DB - tính toán ngay khi gọi getter
    @Transient
    private int yearsOfService;

    public int getYearsOfService() {
        if (hireDate != null) {
            return Period.between(hireDate, LocalDate.now()).getYears();
        }
        return 0;
    }
}
