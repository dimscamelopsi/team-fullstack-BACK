package fr.aelion.streamer.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {
    Course course;
    @BeforeEach
    void setUp() {
        course=new Course();
    }

    @Test
    @DisplayName("Course should return SQL title ")
    void getId() {
        course.setId(1);
        assertEquals(1, course.getId());
    }

    @Test
    @DisplayName("Course should return SQL title ")
    void getTitle() {
        String title = "SQL";
        course.setTitle(title);
        Assertions.assertEquals(course.getTitle(), title);
    }

    @Test
    @DisplayName("Course should have correct form data now")
    void getCreatedAt() {
        LocalDate now = LocalDate.now();
        course.setCreatedAt(now);
        Assertions.assertEquals(now, course.getCreatedAt());

    }

    @Test
    @DisplayName("Course should have correct form data now ")
    void getUpdatedAt() {
        LocalDate now = LocalDate.now();
        course.setUpdatedAt(now);
        Assertions.assertEquals(now, course.getUpdatedAt());
    }

    @Test
    @DisplayName("Course should return objective ")
    void getObjective() {
        String objective="Lean the sql bases";
        course.setObjective(objective);
        Assertions.assertEquals(objective, course.getObjective());

    }

    @Test
    @DisplayName("Course should return module1 and module 2 ")
    void getModules() {
        Set<Module> modules = new HashSet<>();
        Module module1 = new Module();
        Module module2 = new Module();
        modules.add(module1);
        modules.add(module2);
        course.setModules(modules);
        assertEquals(modules, course.getModules());

    }

    @Test
    @DisplayName("Course should return true ")
    void getPublish() {
        course.setPublish(true);
        assertTrue(course.getPublish());
    }

    @Test
    @DisplayName("Course ")
    void getStudent() {
        Student student = new Student();
        course.setStudent(student);
        assertEquals(student, course.getStudent());
    }
}