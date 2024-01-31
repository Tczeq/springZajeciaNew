package pl.szlify.coding.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import pl.szlify.coding.common.Language;
import pl.szlify.coding.teacher.model.Teacher;
import pl.szlify.coding.teacher.model.command.CreateTeacherCommand;
import pl.szlify.coding.teacher.model.command.UpdateTeacherCommand;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.szlify.coding.common.Language.JAVA;
import static pl.szlify.coding.common.Language.PYTHON;

/*
  Takie dodanie w nawiasie wevEnvironment sprawia ze
  aplikacja zostaje uruchomiona na prawdziwym serwerze webowym
  @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
*/
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:teacher_test_cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void testGetAll_ResultsInTeacherListBeingReturned() throws Exception {
        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(JAVA, PYTHON))
                .build();
        Teacher teacher2 = Teacher.builder()
                .firstName("Test2")
                .lastName("Testowy2")
                .languages(Set.of(Language.C, Language.CPP))
                .build();
        teacherRepository.save(teacher);
        teacherRepository.save(teacher2);

        mockMvc.perform(get("/api/v1/teachers"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetById_ResultsInTeacherBeingReturned() throws Exception {
        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(JAVA, PYTHON))
                .build();
        teacherRepository.save(teacher);

        int teacherId = teacher.getId();

        mockMvc.perform(get("/api/v1/teachers/" + teacherId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(teacherId)))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("Testowy")))
                .andExpect(jsonPath("$.languages", containsInAnyOrder(JAVA.name(), PYTHON.name())));
    }

    @Test
    void testCreate_ResultsInTeacherBeingSaved() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(JAVA, PYTHON))
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        assertTrue(teacherRepository.findById(1).isEmpty());

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("Testowy")))
                .andExpect(jsonPath("$.languages", containsInAnyOrder(JAVA.name(), PYTHON.name())));

        assertTrue(teacherRepository.findById(1).isPresent());
    }

    @Test
    void testCreate_teacherNotSaved_ResultsWrongInputFirstName() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("test")
                .lastName("Testowy")
                .languages(Set.of(JAVA, PYTHON))
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[0].field", is("firstName")))
                .andExpect(jsonPath("$.violations[0].message", is("The name must begin with a capital letter and contain from 1 to 50 letters.")));
    }

    @Test
    void testCreate_teacherNotSaved_ResultsFirstNameNull() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .lastName("Testowy")
                .languages(Set.of(JAVA, PYTHON))
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[0].field", is("firstName")))
                .andExpect(jsonPath("$.violations[0].message", is("firstname is obligatory")));
    }

    @Test
    void testCreate_teacherNotSaved_ResultsWrongInputLastName() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("Test")
                .lastName("1estowy")
                .languages(Set.of(JAVA, PYTHON))
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[0].field", is("lastName")))
                .andExpect(jsonPath("$.violations[0].message", is("The lastname must begin with a capital letter and contain from 1 to 50 letters.")));
    }

    @Test
    void testCreate_teacherNotSaved_ResultsLastNameNull() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("Test")
                .languages(Set.of(JAVA, PYTHON))
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[0].field", is("lastName")))
                .andExpect(jsonPath("$.violations[0].message", is("lastname is obligatory")));
    }

    @Test
    void testCreate_teacherNotSaved_ResultsInAtLeastOneLanguage() throws Exception {
        CreateTeacherCommand command = CreateTeacherCommand.builder()
                .firstName("Test")
                .lastName("Testowy")
                .build();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations[0].field", is("languages")))
                .andExpect(jsonPath("$.violations[0].message", is("At least one language")));
    }

    //nie dziala
    @Test
    void testUpdate_ResultsInTeacherBeingUpdated() throws Exception {
        UpdateTeacherCommand command = UpdateTeacherCommand.builder()
                .firstName("Updatename")
                .lastName("Updatelastname")
                .languages(Set.of(JAVA, PYTHON))
                .build();

        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(JAVA, PYTHON))
                .build();

        teacherRepository.save(teacher);

        int teacherId = teacher.getId();

        String jsonCommand = new ObjectMapper().writeValueAsString(command);

        mockMvc.perform(put("/api/v1/teachers/" + teacherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommand))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(teacherId)))
                .andExpect(jsonPath("$.firstName", is("Updatename")))
                .andExpect(jsonPath("$.lastName", is("Updatelastname")))
                .andExpect(jsonPath("$.languages", containsInAnyOrder(JAVA.name(), PYTHON.name())));

    }

    @Test
    void testDelete_ResultsInTeacherBeingDeleted() throws Exception {
        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(JAVA, PYTHON))
                .build();
        teacherRepository.save(teacher);

        int teacherId = teacher.getId();

        mockMvc.perform(delete("/api/v1/teachers/" + teacherId))
                .andDo(print())
                .andExpect(status().isOk());

        Teacher deletedTeacher = teacherRepository.findById(teacherId).orElse(null);
        assertNotNull(deletedTeacher);
        assertTrue(deletedTeacher.isDeleted());
    }

    @Test
    void testGetAllLanguages_ResultsInTeacherGotAllLanguages() throws Exception {
        Language java = JAVA;
        Teacher teacher = Teacher.builder()
                .firstName("Test")
                .lastName("Testowy")
                .languages(Set.of(java, PYTHON))
                .build();
        Teacher teacher2 = Teacher.builder()
                .firstName("Test2")
                .lastName("Testowy2")
                .languages(Set.of(Language.C, java))
                .build();
        Teacher teacher3 = Teacher.builder()
                .firstName("Test3")
                .lastName("Testowy3")
                .languages(Set.of(Language.C, PYTHON))
                .build();

        teacherRepository.save(teacher);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);

        mockMvc.perform(get("/api/v1/teachers?language=" + java))
                .andDo(print())
                .andExpect(status().isOk());

        //można też tak
//        MvcResult mvcResult = mockMvc.perform(get("/api/v1/teachers?language=" + java))
//                .andDo(print())
//                .andReturn();
//
//        assertEquals(200, mvcResult.getResponse().getStatus());

    }
}