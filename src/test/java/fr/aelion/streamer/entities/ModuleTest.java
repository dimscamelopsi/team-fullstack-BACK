package fr.aelion.streamer.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ModuleTest {
    Module module;
    @BeforeEach
    void setUp() {
        module=new Module();
    }

    @Test
    @DisplayName("Module should return Math name ")
    void getName() {
        String name = "Math";
        module.setName(name);
        Assertions.assertEquals(module.getName(), name);

    }

    @Test
    @DisplayName("Module should return this objective value ")
    void getObjective() {
        String objective = "Learn math";
        module.setName(objective);
        Assertions.assertEquals(module.getName(), objective);
    }

    @Test
    @DisplayName("Module should return this course object")
    void getCourse() {
        Course course = new Course();
        module.setCourse(course);
        Assertions.assertEquals(module.getCourse(), course);
    }

    @Test
    @DisplayName("Module must have 2 medias Media 1 and Media 2")
    void getMedias() {
            Module module = new Module();
            List<Media> medias = new ArrayList<>();
            Media media1 = new Media();
            media1.setId(1);
            media1.setTitle("Media 1");
            medias.add(media1);
            Media media2 = new Media();
            media2.setId(2);
            media2.setTitle("Media 2");
            medias.add(media2);
            module.setMedias(medias);

            Assertions.assertEquals(module.getMedias().size(), 2);
            Assertions.assertEquals(module.getMedias().get(0).getTitle(), "Media 1");
            Assertions.assertEquals(module.getMedias().get(1).getTitle(), "Media 2");
        }

}