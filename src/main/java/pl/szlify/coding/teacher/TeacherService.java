package pl.szlify.coding.teacher;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.Teacher;
import pl.szlify.coding.teacher.model.command.CreateTeacherCommand;
import pl.szlify.coding.teacher.model.command.UpdateTeacherCommand;
import pl.szlify.coding.teacher.model.dto.TeacherDto;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<TeacherDto> findAll() {
        return teacherRepository.findAll().stream()
                .map(TeacherDto::fromEntity)
                .toList();
    }

    public TeacherDto create(CreateTeacherCommand command) {
        Teacher teacher = command.toEntity();
        return TeacherDto.fromEntity(teacherRepository.save(teacher));
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
    public TeacherDto update(int id, UpdateTeacherCommand command) {
        Teacher teacher = teacherRepository.findWithLockingById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} not found", id)));

        if (command.getFirstName() != null) {
            teacher.setFirstName(command.getFirstName());
        }
        if (command.getLastName() != null) {
            teacher.setLastName(command.getLastName());
        }
        if (command.getLanguages() != null) {
            teacher.setLanguages(command.getLanguages());
        }

//        teacherRepository.save(teacher);
        return TeacherDto.fromEntity(teacher);
    }

    public TeacherDto findById(int id) {
        return teacherRepository.findById(id)
                .map(TeacherDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat
                        .format("Teacher with id={0} not found", id)));
    }
}
