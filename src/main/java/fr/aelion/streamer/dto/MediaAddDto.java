package fr.aelion.streamer.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MediaAddDto {

    private String title;
    private String summary;
    private Float duration;

    private String url;
    private TypeMediaDto typeMedia;
    private MultipartFile file;
}
