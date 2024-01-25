package pl.szlify.coding.teacher;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.exception.TeacherNotFoundException;
import pl.szlify.coding.teacher.model.Teacher;
import pl.szlify.coding.teacher.model.command.CreateTeacherCommand;
import pl.szlify.coding.teacher.model.command.UpdateTeacherCommand;
import pl.szlify.coding.teacher.model.dto.TeacherDto;

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
        Teacher teacher = teacherRepository.findWithLockingById(idToDelete)
                .orElseThrow(() -> new TeacherNotFoundException(idToDelete));

        teacher.setDeleted(true);
        teacherRepository.save(teacher);
    }

    public List<TeacherDto> findAllByLanguage(Language language) {
        return teacherRepository.findAllByLanguagesContaining(language).stream()
                .map(TeacherDto::fromEntity)
                .toList();
    }

    @Transactional
    public TeacherDto update(int id, UpdateTeacherCommand command) {
        Teacher teacher = teacherRepository.findWithLockingById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));

        if (command.getFirstName() != null) {
            teacher.setFirstName(command.getFirstName());
        }
        if (command.getLastName() != null) {
            teacher.setLastName(command.getLastName());
        }
        if (command.getLanguages() != null) {
            teacher.setLanguages(command.getLanguages());
        }

        teacherRepository.save(teacher);
        return TeacherDto.fromEntity(teacher);
    }


    // CTRL + ALT + N -> Inline variable
    public TeacherDto findById(int id) {
        return teacherRepository.findById(id)
                .map(TeacherDto::fromEntity)
                .orElseThrow(() -> new TeacherNotFoundException(id));
    }

}
