package se.verran.springbootdemowithtests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import se.verran.springbootdemowithtests.entities.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class SchoolServiceTest {
    SchoolService schoolService;
    StudentService mockStudentService;
    List<Student> mockStudentList = new ArrayList<>();

    Student student1;
    Student student2;
    Student student3;
    Student student4;
    Student student5;
    Student student6;
    Student student7;
    Student student8;

    @BeforeEach
    void setUp() {
        mockStudentService = mock(StudentService.class);
        schoolService = new SchoolService(mockStudentService);


        student1 = new Student("Malin", "Olsson", LocalDate.of(1995, 6, 21), "malinolsson@gmail.com");
        student2 = new Student("Kevin", "Andersson", LocalDate.of(1994, 7, 26), "kevinandersson@gmail.com");
        student3 = new Student("Gertrud", "Anks", LocalDate.of(2024, 4, 7), "gertrudands@gmail.com");

        student4 = new Student("Lisa", "Svan", LocalDate.of(2004, 2, 23), "lisasvan@gmail.com");
        student5 = new Student("Tom", "Anks", LocalDate.of(2007, 4, 23), "tomanks@gmail.com");
        student6 = new Student("Ed", "Olsson", LocalDate.of(2007, 4, 23), "ed@gmail.com");
        student7 = new Student("Edd", "Andersson", LocalDate.of(2007, 4, 23), "edd@gmail.com");
        student8 = new Student("Eddy", "Jonsson", LocalDate.of(2007, 4, 23), "eddy@gmail.com");

        student1.setJavaProgrammingGrade(2.5);
        student2.setJavaProgrammingGrade(4.5);
        student3.setJavaProgrammingGrade(1.2);
        student4.setJavaProgrammingGrade(3.7);
        student5.setJavaProgrammingGrade(2.3);
        student6.setJavaProgrammingGrade(5.0);
        student7.setJavaProgrammingGrade(4.9);
        student8.setJavaProgrammingGrade(2.8);

        mockStudentList.add(student1);
        mockStudentList.add(student2);
        mockStudentList.add(student3);
    }


    @Test
    void numberOfStudentsPerGroupWhenDividedIntoNumberOfGroups_WhenNumberOfNumberOfGroupsIsLessThanTwo_ShouldReturnErrorMessageForToFewGroups() {
        int numberOfGroups = 1;
        assertEquals("There should be at least two groups", schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(numberOfGroups));
    }

    @Test
    void numberOfStudentsPerGroupWhenDividedIntoNumberOfGroups_WhenNumberOfNumberOfGroupsEqualsTwo_ShouldNotThrowErrorMessageForToFewGroups() {
        int numberOfGroups = 2;
        assertNotEquals("There should be at least two groups", schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(numberOfGroups));
    }

    @Test
    void numberOfStudentsPerGroupWhenDividedIntoNumberOfGroups_WhenNumberOfGroupsIsBiggerThanNumberOfStudents_ShouldReturnErrorMessageNotAbleToDivide() {
        int numberOfGroups = 4;
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        assertEquals("Not able to divide 3 students into 4 groups", schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(numberOfGroups));
    }

    @Test
    void numberOfStudentsPerGroupWhenDividedIntoNumberOfGroups_WhenDividingThreeStudentsInThreeGroups_ShouldNotReturnErrorMessageNotAbleToDivide() {
        int numberOfGroups = 3;
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        assertNotEquals("Not able to divide 3 students into 3 groups", schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(numberOfGroups));
    }

    @Test
    void numberOfStudentsPerGroupWhenDividedIntoNumberOfGroups_WhenFewerThenTwoInEachGroup_ShouldReturnErrorMessageNotAbleToManageGroups() {
        int numberOfGroups = 2;
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        assertEquals("Not able to manage 2 groups with 3 students", schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(numberOfGroups));
    }

    @Test
    void numberOfStudentsPerGroupWhenDividedIntoNumberOfGroups_WhenDividingInTwoGroupsWithFourStudents_ShouldReturnMessageGroupsCouldBeFormed() {
        int numberOfGroups = 2;
        Student student4 = new Student("Lisa", "Svan", LocalDate.of(2004, 2, 23), "lisasvan@gmail.com");
        mockStudentList.add(student4);
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        assertEquals("2 groups could be formed with 2 students per group", schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(numberOfGroups));
    }


    @Test
    void numberOfStudentsPerGroupWhenDividedIntoNumberOfGroups_WhenDividingInTwoGroupsWithFiveStudents_ShouldReturnMessageGroupsCouldBeFormedPlusOneStudentsHanging() {
        int numberOfGroups = 2;
        mockStudentList.add(student4);
        mockStudentList.add(student5);
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        assertEquals("2 groups could be formed with 2 students per group, but that would leave 1 student hanging", schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(numberOfGroups));
    }

    @Test
    void numberOfStudentsPerGroupWhenDividedIntoNumberOfGroups_WhenDividingInThreeGroupsWithEightStudents_ShouldReturnMessageGroupsCouldBeFormedPlusTwoStudentsHanging() {
        int numberOfGroups = 3;
        mockStudentList.add(student4);
        mockStudentList.add(student5);
        mockStudentList.add(student6);
        mockStudentList.add(student7);
        mockStudentList.add(student8);
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        assertEquals("3 groups could be formed with 2 students per group, but that would leave 2 students hanging", schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(numberOfGroups));
    }

    @Test
    void numberOfGroupsWhenDividedIntoGroupsOf_WhenStudentsPerGroupIsLessThanTwo_ShouldReturnErrorMessageSizeOfGroupShouldBeAtLeastTwo() {
        assertEquals("Size of group should be at least 2", schoolService.numberOfGroupsWhenDividedIntoGroupsOf(1));
    }

    @Test
    void numberOfGroupsWhenDividedIntoGroupsOf_WhenStudentsPerGroupEqualsTwo_ShouldNotReturnErrorMessageSizeOfGroupShouldBeAtLeastTwo() {
        assertNotEquals("Size of group should be at least 2", schoolService.numberOfGroupsWhenDividedIntoGroupsOf(2));
    }

    @Test
    void numberOfGroupsWhenDividedIntoGroupsOf_WhenNumberOfStudentsIsLessThanNumberOfStudentsPerGroup_ShouldReturnErrorMessageNotAbleToManageGroups() {
        mockStudentList.remove(1);

        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        assertEquals("Not able to manage groups of 3 with only 2 students", schoolService.numberOfGroupsWhenDividedIntoGroupsOf(3));
    }

    @Test
    void numberOfGroupsWhenDividedIntoGroupsOf_WhenNumberOfStudentsDividedWithStudentsPerGroupIsLessThanTwo_ShouldReturn_ErrorMessageNotAbleToManageGroups() {
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        assertEquals("Not able to manage groups of 2 with only 3 students", schoolService.numberOfGroupsWhenDividedIntoGroupsOf(2));
    }

    @Test
    void numberOfGroupsWhenDividedIntoGroupsOf_WhenNumberOfStudentsIsEightAndStudentsPerGroupIsFour_ShouldReturnMessageGroupIsPossible() {
        mockStudentList.add(student4);
        mockStudentList.add(student5);
        mockStudentList.add(student6);
        mockStudentList.add(student7);
        mockStudentList.add(student8);

        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);

        assertEquals("4 students per group is possible, there will be 2 groups", schoolService.numberOfGroupsWhenDividedIntoGroupsOf(4));
    }

    @Test
    void numberOfGroupsWhenDividedIntoGroupsOf_WhenNumberOfStudentsIsSevenAndStudentsPerGroupIsThree_ShouldReturnMessageGroupIsPossiblePlusOneStudentHanging() {
        mockStudentList.add(student4);
        mockStudentList.add(student5);
        mockStudentList.add(student6);
        mockStudentList.add(student7);

        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);

        assertEquals("3 students per group is possible, there will be 2 groups, there will be 1 student hanging", schoolService.numberOfGroupsWhenDividedIntoGroupsOf(3));
    }

    @Test
    void numberOfGroupsWhenDividedIntoGroupsOf_WhenNumberOfStudentsIsEighthAndStudentsPerGroupIsThree_ShouldReturnMessageGroupIsPossiblePlusTwoStudentsHanging() {
        mockStudentList.add(student4);
        mockStudentList.add(student5);
        mockStudentList.add(student6);
        mockStudentList.add(student7);
        mockStudentList.add(student8);

        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);

        assertEquals("3 students per group is possible, there will be 2 groups, there will be 2 students hanging", schoolService.numberOfGroupsWhenDividedIntoGroupsOf(3));
    }

    @Test
    void calculateAverageGrade_WhenStudentListIsEmpty_ShouldThrowExceptionWithMessageNoStudentsFound() {
        mockStudentList.clear();
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            schoolService.calculateAverageGrade();
        });
        assertEquals("No students found", exception.getReason());
    }

    @Test
    void calculateAverageGrade_WhenStudentListIsEmpty_ShouldThrowExceptionWithErrorCodeNOT_FOUND() {
        mockStudentList.clear();
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            schoolService.calculateAverageGrade();
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void calculateAverageGrade_WhenStudentsArePresent_ShouldReturnAverageGradeOfTwoPointSeven() {
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);
        assertEquals("Average grade is 2.7", schoolService.calculateAverageGrade());
    }

    @Test
    void getTopScoringStudents_WhenStudentListIsEmpty_ShouldThrowExceptionWithErrorCodeNOT_FOUND() {
        mockStudentList.clear();
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            schoolService.getTopScoringStudents();
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void getTopScoringStudents_WhenStudentListIsEmpty_ShouldThrowExceptionWithMessageNoStudentsFound() {
        mockStudentList.clear();
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            schoolService.getTopScoringStudents();
        });
        assertEquals("No students found", exception.getReason());
    }


    @Test
    void getTopScoringStudents_WhenEightStudentsArePresent_ShouldReturnAListOfTwo() {
        mockStudentList.add(student4);
        mockStudentList.add(student5);
        mockStudentList.add(student6);
        mockStudentList.add(student7);
        mockStudentList.add(student8);
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);

        assertEquals(2, schoolService.getTopScoringStudents().size());
    }

    @Test
    void getTopScoringStudents_WhenEightStudentsArePresent_ShouldReturnStudent6AtIndexZero() {
        mockStudentList.add(student4);
        mockStudentList.add(student5);
        mockStudentList.add(student6);
        mockStudentList.add(student7);
        mockStudentList.add(student8);
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);

        assertEquals(student6, schoolService.getTopScoringStudents().get(0));
    }

    @Test
    void getTopScoringStudents_WhenEightStudentsArePresent_ShouldReturnStudent7AtIndexOne() {
        mockStudentList.add(student4);
        mockStudentList.add(student5);
        mockStudentList.add(student6);
        mockStudentList.add(student7);
        mockStudentList.add(student8);
        when(mockStudentService.getAllStudents()).thenReturn(mockStudentList);

        assertEquals(student7, schoolService.getTopScoringStudents().get(1));
    }
}