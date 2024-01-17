package pl.szlify.coding.teacher.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.student.model.Student;

import java.util.HashSet;
import java.util.Set;





@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@SQLDelete(sql = "UPDATE teacher SET deleted = 1, version = version + 1 WHERE id = ? AND version = ?")

//@Where(clause = "deleted = false") // tutaj ustawiamy gdy chcemy wyswietlic tylko tych ktorzy sa zatudnieni albo nie
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Pattern(regexp = "[A-Z][a-z]{1,50}", message = "The name must begin with a capital letter and contain from 1 to 50 letters.")
    private String firstName;
    @Pattern(regexp = "[A-Z][a-z]{1,50}", message = "The lastname must begin with a capital letter and contain from 1 to 50 letters.")
    @NotNull(message = "lastname is mandatory")
    @NotBlank(message = "lastname is mandatory")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "teacher_language", joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "language")
    private Set<Language> languages = new HashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<Student> students;

    @Version
    private Integer version;

    @Column(name = "deleted")
    private boolean deleted = false;

//    @Column(name = "fired")
//    private boolean fired = false;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
