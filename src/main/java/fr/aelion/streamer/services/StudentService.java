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
    public UserDto findByLoginAndPassword(String login, String password) {
        // TODO <Je dois utiliser mon dto ici et le retourner

        Optional<Student> studentOptional = this.repository.findByLoginAndPassword(login,password);
        Student studentFromDB = studentOptional.get();
        UserDto dto = modelMapper.map(studentFromDB,UserDto.class );

        //System.out.println(dto.getAnswer());
        return dto;
    }

    public StudentLoginDto findByEmailAndAnswer(String email, String answer) {
        // TODO <Je dois utiliser mon dto ici et le retourner

       Optional<Student> studentOptional = this.repository.findByEmailAndAnswer(email,answer);
       Student studentFromDB = studentOptional.get();
       StudentLoginDto dto = modelMapper.map(studentFromDB,StudentLoginDto.class );

       //System.out.println(dto.getAnswer());
       return dto;
    }


    public void updatePassword(int id, String password) {
        Optional<Student> optionalStudent = repository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setPassword(password);
            repository.save(student);
        } else {
            throw new RuntimeException("Person not found");
        }
    }
}
