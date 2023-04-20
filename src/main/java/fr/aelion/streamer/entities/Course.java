package fr.aelion.streamer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "course")
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate createdAt;

    private LocalDate updatedAt;

    private String objective;

    @OneToMany(mappedBy = "course")
    private Set<Module> modules;

    private Boolean publish;

    @ManyToOne(targetEntity = Student.class)
    @JoinColumn(name = "personne_id")
    private Student student;

    public Course() {
        this.createdAt = LocalDate.now();
    }
}
