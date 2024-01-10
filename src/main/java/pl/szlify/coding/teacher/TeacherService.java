package pl.szlify.coding.teacher;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.Teacher;
import pl.szlify.coding.teacher.model.dto.TeacherDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }


    @Transactional
    public void create(Teacher teacher) {
        teacherRepository.save(teacher);
    }


    @Transactional
    public void deleteById(int idToDelete) {
        teacherRepository.deleteById(idToDelete);
    }


//    public List<Teacher> findAllByLanguage(Language language) {
//        return teacherRepository.findAllByLanguagesContaining(language);
//    }
    public List<TeacherDto> findAllByLanguage(Language language) {
        return teacherRepository.findAllByLanguagesContaining(language).stream()
                .map(TeacherDto::fromEntity)
                .toList();
    }

    public Teacher findTeacherById(int teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id=" + teacherId + " not found"));
    }

    @Transactional
    public void fireTeacher(int teacherId) {
        Teacher teacher = findTeacherById(teacherId);
        teacher.setFired(true);
        teacherRepository.save(teacher);

    }

    @Transactional
    public void hireTeacher(int teacherId) {
        Teacher teacher = findTeacherById(teacherId);
        teacher.setFired(false);
        teacherRepository.save(teacher);
    }


    @Transactional
    public void update(Teacher updatedTeacher) {
        Teacher existingTeacher = teacherRepository.findWithLockingById(updatedTeacher.getId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id=" + updatedTeacher.getId() + " not found"));

        existingTeacher.setFirstName(updatedTeacher.getFirstName());
        existingTeacher.setLastName(updatedTeacher.getLastName());
        existingTeacher.setLanguages(updatedTeacher.getLanguages());

        teacherRepository.save(existingTeacher);
    }

}
