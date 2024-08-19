package se.verran.springbootdemowithtests.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import se.verran.springbootdemowithtests.entities.Student;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext (classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)//restettar h2databasen efter varje metod
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        Student student1 = new Student("Malin", "Olsson", LocalDate.of(1995, 6, 21), "malinolsson@gmail.com");
        Student student2 = new Student("Kevin", "Andersson", LocalDate.of(1994, 7, 26), "kevinandersson@gmail.com");

        studentRepository.save(student1);
        studentRepository.save(student2);
    }


    @Test
    void existsStudentByEmail_WhenEmailExist_ShouldReturnTrue() {
        boolean studentExistByEmail = studentRepository.existsStudentByEmail("malinolsson@gmail.com");
        assertTrue(studentExistByEmail);
    }

    @Test
    void existsStudentByEmail_WhenEmailDoesNotExist_ShouldReturnFalse() {
        assertFalse(studentRepository.existsStudentByEmail("false@mail.com"));
    }

}