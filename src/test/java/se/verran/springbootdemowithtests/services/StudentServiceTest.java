package se.verran.springbootdemowithtests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import se.verran.springbootdemowithtests.entities.Student;
import se.verran.springbootdemowithtests.repositories.StudentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

class StudentServiceTest {

    Student mockStudent;
    StudentRepository mockStudentRepository;
    StudentService studentService;
    List<Student> mockStudentList;

    @BeforeEach
    void setUp() {

        mockStudent = mock(Student.class);
        mockStudentRepository = mock(StudentRepository.class);
        studentService = new StudentService(mockStudentRepository);

        mockStudentList = Arrays.asList(new Student("Malin", "Olsson", LocalDate.of(1995, 6, 21), "malinolsson@gmail.com"),
                new Student("Kevin", "Andersson", LocalDate.of(1994, 7, 26), "kevinandersson@gmail.com"),
                new Student("Gertrud", "Anks", LocalDate.of(2024, 4, 7), "gertrudands@gmail.com"));
    }

    @Test
    void addStudent_WhenStudentExistByEmail_ShouldThrowResponseStatusException() {
        when(mockStudentRepository.existsStudentByEmail(mockStudent.getEmail())).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> {
            studentService.addStudent(mockStudent);
        });
    }

    @Test
    void addStudent_WhenStudentExistByEmail_ShouldThrowExceptionWithMessage_EmailAlreadyExists() {
        when(mockStudent.getEmail()).thenReturn("gertrudands@gmail.com");
        when(mockStudentRepository.existsStudentByEmail(mockStudent.getEmail())).thenReturn(true);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            studentService.addStudent(mockStudent);
        });

        String exceptionMessage = exception.getReason();
        assertEquals("Email gertrudands@gmail.com already exists", exceptionMessage);
    }


    @Test
    void addStudent_WhenEmailDoesNotExist_ShouldRunSaveMethodFromStudentRepository() {
        when(mockStudentRepository.existsStudentByEmail(mockStudent.getEmail())).thenReturn(false);
        studentService.addStudent(mockStudent);
        verify(mockStudentRepository).save(mockStudent);
    }

    @Test
    void getAllStudents_WhenUsed_ShouldRunMetod_FindAll_FromStudentRepository() {
        studentService.getAllStudents();
        verify(mockStudentRepository).findAll();
    }

    @Test
    void getAllStudents_WhenThreeStudentsArePresent_ShouldReturnAListOfThree() {
        when(mockStudentRepository.findAll()).thenReturn(mockStudentList);
        assertThat(studentService.getAllStudents()).hasSize(3);
    }

    @Test
    void getAllStudents_ShouldReturnMockStudentList() {
        when(mockStudentRepository.findAll()).thenReturn(mockStudentList);
        assertEquals(mockStudentList, studentService.getAllStudents());
    }

    @Test
    void deleteStudent_WhenStudentIdIsOne_ShouldRunMethod_DeleteByIDFromStudentRepository() {
        when(mockStudentRepository.existsById(1)).thenReturn(true);
        studentService.deleteStudent(1);
        verify(mockStudentRepository).deleteById(1);
    }

    @Test
    void deleteStudent_WhenStudentIdIs10_ShouldThrowResponseStatusException() {
        when(mockStudentRepository.existsById(10)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> {
            studentService.deleteStudent(10);
        });
    }


    @Test
    void deleteStudent_WhenStudentIdIsTen_ShouldThrowExceptionWithMessage_CouldNotFindAndDeleteStudentById10() {
        int studentId = 10;
        when(mockStudentRepository.existsById(studentId)).thenReturn(false);
        String exceptionMessage = assertThrows(ResponseStatusException.class, () -> {
            studentService.deleteStudent(studentId);
        }).getReason();
        assertEquals("Could not find and delete student by id 10", exceptionMessage);
    }


    @Test
    void updateStudent_WhenStudentIdIsTen_ShouldThrowResponseStatusException() {
        when(mockStudentRepository.existsById(mockStudent.getId())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> {
            studentService.updateStudent(mockStudent);
        });
    }

    @Test
    void updateStudent_WhenStudentIdIsTen_ShouldThrowExceptionWithMessage_CouldNotFindAndUpdateStudentById10() {
        when(mockStudentRepository.existsById(mockStudent.getId())).thenReturn(false);
        when(mockStudent.getId()).thenReturn(10);
        String exceptionMessage = assertThrows(ResponseStatusException.class, () -> {
            studentService.updateStudent(mockStudent);
        }).getReason();

        assertEquals("Could not find and update student by id 10", exceptionMessage);
    }

    @Test
    void updateStudent_WhenStudentExistsById_ShouldRunMethodSaveFromStudentRepository() {
        when(mockStudentRepository.existsById(mockStudent.getId())).thenReturn(true);
        studentService.updateStudent(mockStudent);
        verify(mockStudentRepository).save(mockStudent);
    }


    @Test
    void getStudentById_WhenStudentCanNotBeFoundById_ShouldThrowResponseStatusException() {
        when(mockStudentRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            studentService.getStudentById(10);
        });
    }

    @Test
    void getStudentById_WhenStudentCanNotBeFoundById_ShouldThrowExceptionWithMessage_CouldNotFindStudent() {
        when(mockStudentRepository.findById(10)).thenReturn(Optional.empty());
        String exceptionMessage = assertThrows(ResponseStatusException.class, () -> {
            studentService.getStudentById(10);
        }).getReason();

        assertEquals("Could not find student by id 10", exceptionMessage);
    }

    @Test
    void getStudentById_WhenStudentIdIsTwo_ShouldReturnStudentWithId2() {
        int studentId = 2;
        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.ofNullable(mockStudentList.get(2)));
        assertEquals(mockStudentList.get(2), studentService.getStudentById(studentId));
    }


    @Test
    void setGradeForStudentById_WhenGradeIsInvalid_ShouldThrowResponseStatusException() {
        int studentId = 2;
        String invalidGrade = "invalid";
        assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(studentId, invalidGrade);
        });
    }

    @Test
    void setGradeForStudentById_WhenGradeIsInvalid_ShouldThrowExceptionWithMessage_ValidGradesAre() {
        int studentId = 2;
        String invalidGrade = "invalid";
        String exceptionMessage = assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(studentId, invalidGrade);
        }).getReason();

        assertEquals("Valid grades are 0.0 - 5.0", exceptionMessage);
    }

    @Test
    void setGradeForStudentById_WhenGradeIsInvalid_ShouldThrowExceptionWithErrorCode_NOT_ACCEPTABLE() {
        int studentId = 2;
        String invalidGrade = "invalid";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(studentId, invalidGrade);
        });

        assertEquals(NOT_ACCEPTABLE, exception.getStatusCode());
    }


    @Test
    void setGradeForStudentById_WhenGradeIsFivePointOne_ShouldThrowResponseStatusException() {
        int studentId = 2;
        String invalidGrade = "5.1";

        assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(studentId, invalidGrade);
        });
    }

    @Test
    void setGradeForStudentById_WhenGradeIsFivePointOne_ShouldThrowExceptionWithMessage_ValidGradesAre() {
        int studentId = 2;
        String invalidGrade = "5.1";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(studentId, invalidGrade);
        });

        assertEquals("Valid grades are 0.0 - 5.0", exception.getReason());
    }

    @Test
    void setGradeForStudentById_WhenGradeIsFivePointOne_ShouldThrowExceptionWithErrorCode_NOT_ACCEPTABLE() {
        int studentId = 2;
        String invalidGrade = "5.1";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(studentId, invalidGrade);
        });

        assertEquals(NOT_ACCEPTABLE, exception.getStatusCode());
    }

    @Test
    void setGradeForStudentById_WhenStudentIdIsTen_ShouldThrowResponseStatusException() {
        int invalidStudentId = 10;
        String validStudentGrade = "3.5";

        when(mockStudentRepository.findById(invalidStudentId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(invalidStudentId, validStudentGrade);
        });
    }


    @Test
    void setGradeForStudentById_WhenStudentIdIsTen_ShouldThrowExceptionWithMessage_CouldNotFindAndUpdateGradesForStudent() {
        int invalidStudentId = 10;
        String validStudentGrade = "3.5";

        when(mockStudentRepository.findById(invalidStudentId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(invalidStudentId, validStudentGrade);
        });

        assertEquals("Could not find and update grades for student by id 10", exception.getReason());
    }


    @Test
    void setGradeForStudentById_WhenStudentIdIsTen_ShouldThrowExceptionWithErrorCode_NOT_FOUND() {
        int invalidStudentId = 10;
        String validStudentGrade = "3.5";

        when(mockStudentRepository.findById(invalidStudentId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            studentService.setGradeForStudentById(invalidStudentId, validStudentGrade);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void setGradeForStudentById_WhenStudentIdIsOneAndGradeIsFourPointSix_ShouldRunMetodSetJavaProgrammingGradeFromStudent() {
        String validGrade = "4.6";
        int studentId = 1;

        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.ofNullable(mockStudent));
        studentService.setGradeForStudentById(studentId, validGrade);
        verify(mockStudent).setJavaProgrammingGrade(4.6);
    }


    @Test
    void setGradeForStudentById_WhenStudentIdIsOneAndGradeIsFourPointSix_ShouldRunMetodSaveFromStudentRepository() {
        String validGrade = "4.6";
        int studentId = 1;

        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.ofNullable(mockStudent));
        studentService.setGradeForStudentById(studentId, validGrade);
        verify(mockStudentRepository).save(mockStudent);
    }
}