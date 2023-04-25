package fr.aelion.streamer.services;

import fr.aelion.streamer.dto.*;
import fr.aelion.streamer.entities.Student;
import fr.aelion.streamer.repositories.StudentRepository;
import fr.aelion.streamer.services.exceptions.EmailAlreadyExistsException;
import fr.aelion.streamer.services.exceptions.LoginAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class StudentService {
    @Autowired
    private StudentRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public List<StudentDto> findAll() {
        var students = repository.findAll()
                .stream()
                .map(student -> {
                    var studentDto = modelMapper.map(student, StudentDto.class);
                    return studentDto;
                })
                .collect(Collectors.toList());

        return students;
    }

    public List<SimpleStudentDto> findSimpleStudents() {
        return repository.findAll()
                .stream()
                .map(s -> {
                    SimpleStudentDto dto = new SimpleStudentDto();
                    dto.setId(s.getId());
                    dto.setLastName(s.getLastName());
                    dto.setFirstName(s.getFirstName());
                    dto.setEmail(s.getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<SimpleStudentProjection> fromProjection() {
        return repository.getSimpleStudents();
    }

    public Student add(AddStudentDto student) throws Exception {
        Student anyStudent = repository.findByEmail(student.getEmail());
        if (anyStudent != null) {
            throw new EmailAlreadyExistsException("Email " + student.getEmail() + " already exists");
        }
        anyStudent = repository.findByLogin(student.getLogin());
        if (anyStudent != null) {
            throw new LoginAlreadyExistsException("Login " + student.getLogin() + " already exists");
        }
        Student newStudent = modelMapper.map(student, Student.class);
        newStudent = (Student) repository.save(newStudent);

        return newStudent;
    }

    public void update(Student student) throws Exception {
        try {
            repository.save(student);
        } catch (Exception e) {
            throw new Exception("Something went wrong while updating Student");
        }
    }

    public Student findOne(int id) {
        return repository.findById(id)
                .map(s -> s)
                .orElseThrow();
    }

    public void delete(int id) {
        try {
            var student = this.findOne(id);
            repository.delete(student);
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    public Set<Integer> multipleDelete(Set<Integer> ids) {
        var nonDeletedIds = new HashSet<Integer>();
        ids.stream()
                .forEach(i -> {
                    try {
                        repository.delete(this.findOne(i));
                    } catch (NoSuchElementException e) {
                        nonDeletedIds.add(i);
                    } catch (Exception e) {
                        nonDeletedIds.add(i);
                    }
                });
        return nonDeletedIds;
    }

    /*public Optional<Student> findByLoginAndPassword(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }*/

    /**
     * This method check if login and password exist in data base and return response ok
     *
     * @param login
     * @param password
     * @return
     */
    public UserDto findByLoginAndPassword(String login, String password) {
        Optional<Student> studentOptional = this.repository.findByLoginAndPassword(login, password);
        Student studentFromDB = studentOptional.get();
        UserDto dto = modelMapper.map(studentFromDB, UserDto.class);

        //System.out.println(dto.getAnswer());
        return dto;
    }

    /**
     * This method search if email and answer exist in data base and return a response ok
     *
     * @param email
     * @param answer
     * @return
     */
    public StudentLoginDto findByEmailAndAnswer(String email, String answer) {

        Optional<Student> studentOptional = this.repository.findByEmailAndAnswer(email, answer);
        Student studentFromDB = studentOptional.get();
        StudentLoginDto dto = modelMapper.map(studentFromDB, StudentLoginDto.class);
        return dto;
    }

    /**
     * This method update and save a new password
     *
     * @param email
     * @param password
     */
    public void updatePassword(String email, String password) {
        Optional<Student> optionalStudent = Optional.ofNullable(repository.findByEmail(email));
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setPassword(password);
            repository.save(student);
        } else {
            throw new RuntimeException("Person not found");
        }
    }
}
