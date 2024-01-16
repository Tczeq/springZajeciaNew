package pl.szlify.coding.student.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.teacher.model.Teacher;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

//@SQLDelete(sql = "UPDATE student SET deleted = 1, version = version + 1 WHERE id = ? AND version = ?")
//@SQLDelete(sql = "UPDATE student SET deleted = 1, version = version + 0 WHERE id = ? AND version = ?")
//@SQLDelete(sql = "UPDATE student SET deleted = 1 WHERE id = ? AND deleted = ?")
//@Where(clause = "deleted = false")





@SQLDelete(sql = "UPDATE student SET deleted = 1, version = version +1 WHERE id = ? AND version = ?")

//@Where(clause = "deleted = false") // tutaj ustawiamy gdy chcemy wyswietlic tylko tych ktorzy sa zatudnieni albo nie
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Language language;

    @ManyToOne
    private Teacher teacher;

    @OneToMany(mappedBy = "student")
    private Set<Lesson> lessons = new HashSet<>();

    @Column(name = "deleted")
    private boolean deleted = false;

    @Version
    private Integer version;

}
