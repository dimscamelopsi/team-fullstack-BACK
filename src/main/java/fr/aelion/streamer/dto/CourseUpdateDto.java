package fr.aelion.streamer.dto;

import fr.aelion.streamer.entities.Student;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CourseUpdateDto {

    private int id;

    private String title;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private String objective;
    private Boolean publish;

    @ManyToOne(targetEntity = Student.class)
    @JoinColumn(name = "personne_id")
    private Student student;

    public CourseUpdateDto() {
        this.updatedAt = LocalDate.now();
    }
}
