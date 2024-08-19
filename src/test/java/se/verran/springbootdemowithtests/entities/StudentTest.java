package se.verran.springbootdemowithtests.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    Student student1;
    Student student2;


    @BeforeEach
    void setUp() {
        student1 = new Student("Malin", "Olsson", LocalDate.of(1995, 6, 21), "malinolsson@gmail.com");
        student2 = new Student("Kevin", "Andersson", LocalDate.of(1994, 7, 26), "kevinandersson@gmail.com");

    }

    @Test
    void getAge_WhenStudentIsBorn1994_7_26_ShouldReturnAge30() {
        assertEquals(30, student2.getAge());
    }
}