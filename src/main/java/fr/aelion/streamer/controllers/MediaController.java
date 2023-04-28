package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.MediaDto;
import fr.aelion.streamer.entities.Media;
import fr.aelion.streamer.repositories.ModuleRepository;
import fr.aelion.streamer.services.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private MediaService mediaService;



    @GetMapping("")
    public ResponseEntity<List<MediaDto>> getAllMedia() {
        List<MediaDto> mediaList = mediaService.findAll();
        return ResponseEntity.ok(mediaList);
    }

    @PostMapping
    public ResponseEntity<?> createMedia(
         //  @RequestParam("moduleId") Integer moduleId,
            @RequestParam("mediaType") String mediaType,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("duration") String duration,
            @RequestParam("mediaUrl") String mediaUrl
    ) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\": \"Le fichier est manquant ou vide\"}");
        }


       // TypeMedia typeMedia = ty
       // String mediaUrl = saveMediaFile(file);
        Media media = mediaService.createMedia(title, summary, mediaType, mediaUrl, duration);
       // Module module = moduleRepository.findById(moduleId).orElse(null);
        /**
         *       if (module == null) {
         *             return ResponseEntity.badRequest().body("{\"error\": \"Module not found\"}");
         *         }
         *
         *         ModuleMedia moduleMedia = moduleService.addMediaToModule(module, media);
         *         if (moduleMedia != null) {
         *             return ResponseEntity.status(HttpStatus.CREATED).body("Media created and linked to the module");
         *         } else {
         *             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error linking media to module");
         *
         */

        return ResponseEntity.status(HttpStatus.CREATED).body("Media created and linked to the module");
    }

    private String saveMediaFile(MultipartFile file) {
        return null;
    }


    /**
     *
     * @param
     * @param    @GetMapping("/download")
     *     public ResponseEntity<Resource> downloadFile(@RequestParam String filename, HttpServletRequest request) {
     *         Resource resource = mediaService.load(filename);
     *         String contentType = null;
     *         try {
     *             contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
     *         } catch (IOException e) {
     *             e.printStackTrace();
     *         }
     *         if(contentType == null) {
     *             contentType = "application/octet-stream";
     *         }
     *         return ResponseEntity.ok()
     *                 .contentType(MediaType.parseMediaType(contentType))
     *                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
     *                 .body(resource);
     *     }quest
     * @return
     *     @PostMapping("/upload")
     *     public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
     *         mediaService.save(file);
     *         String message = "Uploaded the file successfully: " + file.getOriginalFilename();
     *         return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
     *     }
     *       @PostMapping("")
     *     public ResponseEntity<MediaDto> addMedia(@ModelAttribute MediaAddDto mediaAddDto) {
     *         MediaDto mediaDto = mediaService.add(mediaAddDto);
     *         return ResponseEntity.status(HttpStatus.CREATED).body(mediaDto);
     *     }
     */


}
