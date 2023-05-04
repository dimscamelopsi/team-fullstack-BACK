package fr.aelion.streamer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleByPersonDto {
    private String name;
    private String objective;
    private int courseId;
    private int orderModule;
}
