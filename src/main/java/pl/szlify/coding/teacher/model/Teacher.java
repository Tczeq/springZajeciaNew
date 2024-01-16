package pl.szlify.coding.teacher.model;

import jakarta.persistence.*;
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

    private String firstName;
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
