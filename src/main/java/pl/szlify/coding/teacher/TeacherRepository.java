package pl.szlify.coding.teacher;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    List<Teacher> findAllByLanguagesContaining(Language language);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<Teacher> findWithLockingById(int id);

    @Query(value = "SELECT t from Teacher t WHERE t.firstName = :name")
    List<Teacher> find(@Param("name") String name);
}
