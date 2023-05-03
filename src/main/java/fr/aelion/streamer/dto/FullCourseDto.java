package fr.aelion.streamer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class FullCourseDto {

    private int id;

    private String title;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private String objective;

    private Boolean publish;

    private Set<ModuleDto> modules = new HashSet<>();
}
