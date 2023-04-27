package fr.aelion.streamer.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    Student student;

    @BeforeEach()
    void setUp() {

        student = new Student();

        student.setLastName("Aubert");
        student.setFirstName("Jean-luc");
        student.setEmail("jean-luc.aubert@aelion.fr");

    }

    @Test
    @DisplayName("Standared properties should be 'Aubert', 'Jean-Luc', 'jean-luc.aubert@aelion.fr")
    void testStdProperties() {
        assertAll("Aubert Jean-Luc Jean-luc.aubert@aelion.fr",
                () -> assertEquals("Aubert", student.getLastName()),
                () -> assertEquals("Jean-luc", student.getFirstName()),
                () -> assertEquals("jean-luc.aubert@aelion.fr", student.getEmail())
        );
    }

    @Test
    @DisplayName("Student should have 'Casper' as lastName")
    void testPublicAttribute() {
        Student student = new Student();

        student.setLastName("Casper");
        student.setFirstName("jules");
        student.setEmail("jt@test.com");
        assertEquals("Casper", student.getLastName());
    }
}