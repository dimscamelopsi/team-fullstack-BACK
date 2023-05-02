package fr.aelion.streamer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "media")
@Getter
@Setter
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String title;

    private String summary;

    private Float duration;

    private LocalDate createdAt;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "typemedia_id", nullable = false)
    private TypeMedia typeMedia;

    @ManyToMany(mappedBy = "medias")
    private List<Module> modules;

    public Media() {
        createdAt = LocalDate.now();
    }
    private Integer orderMedia;

}