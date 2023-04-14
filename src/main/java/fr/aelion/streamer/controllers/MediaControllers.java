package fr.aelion.streamer.controllers;
import fr.aelion.streamer.dto.CourseAddDto;
import fr.aelion.streamer.dto.FullCourseDto;
import fr.aelion.streamer.dto.MediaAddDto;
import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.services.MediaService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/media")
public class MediaControllers {
    @Autowired
    private MediaService mediaService;

    @PostMapping
    public ResponseEntity<MediaDto> add(@RequestBody MediaAddDto media) {
        MediaDto mediaDto = this.mediaService.add(media);
        return ResponseEntity.ok(mediaDto);
    }

}
