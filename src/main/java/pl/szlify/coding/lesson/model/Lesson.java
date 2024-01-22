package pl.szlify.coding.lesson.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertFalse;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.model.Teacher;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
//@SQLDelete(sql = "UPDATE lesson SET deleted = 1 WHERE id = ?")
@SQLDelete(sql = "UPDATE lesson SET deleted = 1 WHERE id = ? AND deleted = ?")
//@Where(clause = "deleted = false")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//jQuery - wybieramy nauczyciela i przeladowuje nam studentow dla danego anuczyciela
    //Asocjacja
    //walidacja lekcji

    private LocalDateTime term;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private Student student;

    @Version
    private Integer version;

    @Column(name = "deleted")
    @AssertFalse
    private Boolean isDeleted;
}
