package pl.szlify.coding.student;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pl.szlify.coding.student.model.Student;
import pl.szlify.coding.teacher.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findAllByTeacher(Teacher teacher);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<Student> findWithLockingById(int id);
}


