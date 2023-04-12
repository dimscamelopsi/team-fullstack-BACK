package fr.aelion.streamer.controllers;

import fr.aelion.streamer.dto.CourseAddDto;
import fr.aelion.streamer.dto.FullCourseDto;
import fr.aelion.streamer.entities.Course;
import fr.aelion.streamer.repositories.CourseRepository;
import fr.aelion.streamer.services.interfaces.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/course")
public class CourseController {
    @Autowired
    private CourseService service;

    @Autowired
    private CourseRepository repository;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FullCourseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/manadispcourse/{login}")
    public List<FullCourseDto> findByLogin(@PathVariable("login") String login){
        try {
            return service.getListCourseByAutor(login);}
        catch (ConverterNotFoundException e){
            System.out.println("[CourseController] Error FindAutor : " + e.getMessage());}
        return null;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> findOne(@PathVariable() int id) {
        try {
            return ResponseEntity.ok(service.findOne(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable() int id) {
        try {
            service.remove(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FullCourseDto> add(@RequestBody CourseAddDto course) {
        FullCourseDto courseDto = this.service.add(course);
        return ResponseEntity.ok(courseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> update(@PathVariable("id") int id, @RequestBody FullCourseDto fullCourseDto){
        Optional<Course> courseData = repository.findById(id);

        if(courseData.isPresent()){
            Course course = courseData.get();
            course.setTitle(fullCourseDto.getTitle());
            course.setObjective(fullCourseDto.getObjective());
            course.setPublish(fullCourseDto.getPublish());
            course.setUpdatedAt(fullCourseDto.getUpdatedAt());
            return new ResponseEntity<>(repository.save(course), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
