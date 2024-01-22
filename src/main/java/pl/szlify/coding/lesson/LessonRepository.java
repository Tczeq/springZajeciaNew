package pl.szlify.coding.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PostMapping;
import pl.szlify.coding.lesson.model.Lesson;
import pl.szlify.coding.teacher.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;


public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    List<Lesson> findByTeacher(Teacher teacher);

    boolean existsByTeacherIdAndTermAfterAndTermBefore(int teacherId, LocalDateTime from, LocalDateTime to);

}


