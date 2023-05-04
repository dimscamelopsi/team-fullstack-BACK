package fr.aelion.streamer.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;

@Getter
@Setter
public class MediaDto {

    private int id;

    private String title;

    private String summary;

    private Float duration;

    private String totalTime;

    private LocalDate createdAt;

    private String url;

    private TypeMediaDto typeMedia;

    private Integer orderMedia;

    private MultipartFile file;

    private File file2;

    private Resource resource;

    private String fileName; // Ajout de la propriété fileName
}